package com.dreamdance.android.th;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Author: dk
 * Date: 12-5-8
 */
public class Utility {
    static final int MIN_REL_WIDTH = 640;
    static final int MIN_REL_HEIGHT = 480;
    static Pair<Integer, Integer> sResolution;

    public static boolean checkPathExists(String path) {
        File f = new File(path);
        return (f.exists());
    }

    public static boolean isSuitableResolution(Activity activity) {
        Pair<Integer, Integer> rel = getResolution(activity);
        return (rel.first >= MIN_REL_WIDTH && rel.second >= MIN_REL_HEIGHT);
    }

    public static Pair<Integer, Integer> getResolution(Activity activity) {
        if (null == sResolution) {
            int width = activity.getWindowManager().getDefaultDisplay().getWidth();
            int height = activity.getWindowManager().getDefaultDisplay().getHeight();
            sResolution = new Pair<Integer, Integer>(width, height);
        }
        return sResolution;
    }

    public static String getDownloadDir() {
        String status = Environment.getExternalStorageState();
        if (status == null || !status.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }

        String path = null;

        // get the sdcard directory
        File sdFile = Environment.getExternalStorageDirectory();
        if (null != sdFile) {
            path = sdFile.toString();
        } else {
            path = "/sdcard/";
        }

        path += "/download";

        File destDir = new File(path);
        if (!destDir.exists()) {
            try {
                if (!destDir.mkdirs()) {
                    Log.e("getDownloadDir", "create folder " + path + " failed");
                    return null;
                }
            } catch (SecurityException e) {
                Log.e("getDownloadDir", "create folder " + path + " failed: " + e.toString());
                return null;
            }
        }

        return path;
    }


    /** AsyncTask for extracting a zip input stream to a directory */
    public static class UnzipStreamTask extends
            AsyncTask<InputStream, Integer, AsyncTaskResult<String>> {
        String unzipTo;

        public UnzipStreamTask(String unzipTo) {
            this.unzipTo = unzipTo;
        }

        @Override
        protected AsyncTaskResult<String> doInBackground(InputStream... streams) {
            ZipInputStream zis = null;
            AsyncTaskResult<String> result = null;
            try {

                File to = new File(unzipTo);
                to.mkdirs();

                int count = 0;

                InputStream is = streams[0];
                zis = new ZipInputStream(new BufferedInputStream(is));
                ZipEntry ze;
                while ((ze = zis.getNextEntry()) != null) {
                    Log.v("UNZIP",
                            "Unzipping " + ze.getName());

                    File f = new File(unzipTo + ze.getName());
                    if (!f.getParentFile().exists()) {
                        f.getParentFile().mkdirs();
                    }

                    if (ze.isDirectory()) {

                        if (!f.isDirectory()) {
                            f.mkdirs();
                        }
                    } else {
                        FileOutputStream fout = new FileOutputStream(unzipTo + ze.getName());

                        byte[] buffer = new byte[1024];
                        int c;

                        while ((c = zis.read(buffer)) != -1) {
                            fout.write(buffer, 0, c);
                        }

                        fout.close();
                    }
                }

            } catch (Exception e) {
                //MobclickAgent.reportError(ctx, e.toString());
                result =  new AsyncTaskResult<String>(e);
            }

            if (null != zis) {
                try {
                    zis.close();
                } catch (Exception e) {
                    result =  new AsyncTaskResult<String>(e);
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            if (null == result) result = new AsyncTaskResult<String>(unzipTo);
            return result;
        }
    }

    /** AsyncTask for extracting a .zip file to a directory */
    public static class UnzipTask extends
            AsyncTask<File, Integer, AsyncTaskResult<String>> {
        String unzipTo;
        File mSourceFile;

        public UnzipTask(String unzipTo) {
            this.unzipTo = unzipTo;
        }

        boolean removeSource() {
            if (null != mSourceFile) return mSourceFile.delete();
            return false;
        }

        @Override
        protected AsyncTaskResult<String> doInBackground(File... files) {
            try {

                File to = new File(unzipTo);
                to.mkdirs();

                ZipFile zf = new ZipFile(files[0]);
                mSourceFile = files[0];
                int entryCount = zf.size();

                Enumeration entries = zf.entries();
                int count = 0;

                while (entries.hasMoreElements()) {
                    ZipEntry ze = (ZipEntry) entries.nextElement();
                    Log.v("UNZIP",
                            "Unzipping " + ze.getName());

                    File f = new File(unzipTo + ze.getName());
                    if (!f.getParentFile().exists()) {
                        f.getParentFile().mkdirs();
                    }

                    if (ze.isDirectory()) {

                        if (!f.isDirectory()) {
                            f.mkdirs();
                        }
                    } else {

                        InputStream zin = zf.getInputStream(ze);

                        FileOutputStream fout = new FileOutputStream(unzipTo
                                + ze.getName());

                        byte[] buffer = new byte[1024];
                        int read;

                        while ((read = zin.read(buffer)) != -1) {
                            fout.write(buffer, 0, read);
                        }

                        zin.close();
                        fout.close();

                    }

                    count++;
                    publishProgress(count * 100 / entryCount);

                }

            } catch (IOException e) {
                //MobclickAgent.reportError(ctx, e.toString());
                return new AsyncTaskResult<String>(e);
            }

            return new AsyncTaskResult<String>(unzipTo);

        }
    }

    /** Copies an asset to a given directory */
    public static void copyAsset(Context ctx, String assetFilename,
                                 String destination) throws IOException {
        AssetManager assetManager = ctx.getAssets();

        InputStream in = null;
        OutputStream out = null;

        in = assetManager.open(assetFilename);

        String newFileName = destination + "/" + assetFilename;

        File dir = new File(newFileName).getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        out = new FileOutputStream(newFileName);

        byte[] buffer = new byte[1024];
        int read;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

        in.close();

        out.flush();

        out.close();

    }
}
