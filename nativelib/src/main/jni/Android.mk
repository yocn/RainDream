LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := ffmpeg
LOCAL_SRC_FILES := $(LOCAL_PATH)/ffmpeglib/armeabi-v7a/libffmpeg.so
#下面是申明第三方头文件路径
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/ffmpeglib/armeabi-v7a/include
include $(PREBUILT_SHARED_LIBRARY)

# Program
include $(CLEAR_VARS)
LOCAL_MODULE := av_ffmpeg
LOCAL_SRC_FILES :=  \
  $(LOCAL_PATH)/ffmpeglib/ffmpegbin/cmdutils.c  \
  $(LOCAL_PATH)/ffmpeglib/ffmpegbin/ffmpeg_filter.c  \
  $(LOCAL_PATH)/ffmpeglib/ffmpegbin/ffmpeg_hw.c  \
  $(LOCAL_PATH)/ffmpeglib/ffmpegbin/ffmpeg_opt.c  \
  $(LOCAL_PATH)/ffmpeglib/ffmpegbin/ffmpeg.c  \
  $(LOCAL_PATH)/ffmpeglib/ffmpegbin/ffmpeg_jni.c  \
  CMDNative.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)/ffmpeglib/ffmpegbin
#LOCAL_C_INCLUDES += $(LOCAL_PATH)/armeabi-v7a/include

LOCAL_LDLIBS := -llog -lz -lm
LOCAL_SHARED_LIBRARIES := ffmpeg
include $(BUILD_SHARED_LIBRARY)
