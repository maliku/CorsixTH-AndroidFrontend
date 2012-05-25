LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := SDL_gfx

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/ \
	$(LOCAL_PATH)/../SDL/include \


LOCAL_SRC_FILES := \
	SDL_rotozoom.c \
	SDL_framerate.c
			
LOCAL_SHARED_LIBRARIES := SDL
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
