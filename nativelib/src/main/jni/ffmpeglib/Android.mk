LOCAL_PATH := $(call my-dir)
ARMV7_NATIVE_LIBPATH := $(call my-dir)/armeabi-v7a/lib
ARMV5_NATIVE_LIBPATH := $(call my-dir)/armeabi/lib

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
NATIVE_LIBPATH := $(ARMV7_NATIVE_LIBPATH)
include $(CLEAR_VARS)
LOCAL_MODULE := ffmpeg
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/../libffmpeg.so
include $(PREBUILT_SHARED_LIBRARY)
endif













#armeabi平台基本上不用了，且现在直接引用so by danny

ifeq ($(TARGET_ARCH_ABI),armeabi)
NATIVE_LIBPATH := $(ARMV5_NATIVE_LIBPATH)

include $(CLEAR_VARS)
LOCAL_MODULE := avformat
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libavformat.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avcodec
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libavcodec.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swscale
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libswscale.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swresample
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libswresample.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avresample
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libavresample.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avutil
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libavutil.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avfilter
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libavfilter.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := fdk-aac
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libfdk-aac.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := speex
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libspeex.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := x264
LOCAL_SRC_FILES := $(NATIVE_LIBPATH)/libx264.a
include $(PREBUILT_STATIC_LIBRARY)

endif

