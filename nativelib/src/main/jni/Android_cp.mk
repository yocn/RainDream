LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := av_ffmpeg

FFMPEGLIBS_INCDIR   := $(LOCAL_PATH)/ffmpeglib/armeabi-v7a/include
AV_FFMPEG_SRCDIR := $(LOCAL_PATH)/ffmpeglib

GLOBAL_C_INCLUDES := \
  $(AV_FFMPEG_SRCDIR)/ffmpegbin/ \
  $(FFMPEGLIBS_INCDIR)/

LOCAL_SRC_FILES := \
  $(AV_FFMPEG_SRCDIR)/ffmpegbin/cmdutils.c  \
  $(AV_FFMPEG_SRCDIR)/ffmpegbin/ffmpeg_filter.c  \
  $(AV_FFMPEG_SRCDIR)/ffmpegbin/ffmpeg_hw.c  \
  $(AV_FFMPEG_SRCDIR)/ffmpegbin/ffmpeg_opt.c  \
  $(AV_FFMPEG_SRCDIR)/ffmpegbin/ffmpeg.c  \
  $(AV_FFMPEG_SRCDIR)/ffmpegbin/ffmpeg_jni.c \
  CMDNative.cpp

LOCAL_DISABLE_FATAL_LINKER_WARNINGS := true
LOCAL_CPPFLAGS += -DANDROID -DBUILD_CROSS_PLATFORM -DFIXED_POINT -DARM -O3
LOCAL_CFLAGS += -DANDROID -DBUILD_CROSS_PLATFORM -DFIXED_POINT -DARM -O3

LOCAL_SHARED_LIBRARIES := ffmpeg

LOCAL_LDLIBS    := -lm -llog -lz -ljnigraphics -lEGL -lGLESv2 -lOpenSLES
LOCAL_C_INCLUDES := $(GLOBAL_C_INCLUDES)

include $(BUILD_SHARED_LIBRARY)

include $(call all-makefiles-under,$(LOCAL_PATH))


