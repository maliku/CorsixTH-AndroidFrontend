package com.dreamdance.android.th;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import com.dreamdance.th.R;

public class DialogFactory {
	public static Dialog createExternalStorageDialog(final Context ctx,
			boolean finish) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		builder.setMessage(
				ctx.getResources().getString(R.string.no_external_storage))
				.setCancelable(false);
		if (finish) {

			builder.setNeutralButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							((Activity) ctx).finish();
						}

					});

		} else {
			builder.setNeutralButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
						}

					});
		}

		return builder.create();
	}

    public static Dialog createResolutionDialog(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        Resources res = ctx.getResources();

        builder.setMessage(
                res.getString(R.string.not_suitable_resolution))
                .setCancelable(false)
                .setNeutralButton(res.getText(R.string.quit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) ctx).finish();
                    }
                });

        return builder.create();
    }

    public static Dialog createExtractFailedDialog(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        Resources res = ctx.getResources();

        builder.setMessage(
                res.getString(R.string.extracting_failed))
                .setCancelable(false)
                .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.dismiss();
                        ((Activity) ctx).finish();
                    }
                });

        return builder.create();
    }

    public static Dialog createEnvFailedDialog(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        Resources res = ctx.getResources();

        builder.setMessage(
                res.getString(R.string.env_failed))
                .setCancelable(false)
                .setNeutralButton(res.getText(R.string.quit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                        ((Activity) ctx).finish();
                    }
                });

        return builder.create();
    }

    public static Dialog createNotEnoughSpaceDialog(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        Resources res = ctx.getResources();

        builder.setMessage(
                res.getString(R.string.not_enough_space))
                .setCancelable(false)
                .setNeutralButton(res.getText(R.string.quit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                        ((Activity) ctx).finish();
                    }
                });

        return builder.create();
    }
}
