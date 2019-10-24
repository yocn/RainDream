//
// Created by 赵英坤 on 2019-10-22.
//

#include "CMDNative.h"
#include <jni.h>
#include <cstdlib>
#include "AndroidLog.h"

#ifndef LOG_TAG
#define LOG_TAG "JNI_SRCNativeJNI"
#endif

using namespace std;
//两个类名，必须要修改 raindream.yocn.nativelib
#define JNI_METHOD_NAME(name) Java_raindream_yocn_nativelib_NativeJNI_##name
static const char *CLASSPATH = "raindream/yocn/nativelib/NativeJNI";

extern "C" {
#include "ffmpeg.h"

JNIEXPORT void JNICALL JNI_METHOD_NAME(helloFFmpeg)(JNIEnv *env, jclass thiz);
JNIEXPORT jint JNICALL JNI_METHOD_NAME(execCmd)(JNIEnv *env, jclass clazz,
                                                jobjectArray commands);
}

JNIEXPORT void JNICALL JNI_METHOD_NAME(helloFFmpeg)(JNIEnv *env, jclass thiz) {
//    ffmpeg_exec();
    const char *path = "mnt/sdcard/Android/data/com.yocn.raindream/files/ogg/wind.ogg";
    FILE *file = fopen(path, "w");
    if (file) {
        LOGEA("success ");
    } else {
        LOGEA("fail---");
    }
}


int debug1 = 1;//log开关

void log_callback_report_for_cmd(void *ptr, int level, const char *fmt, va_list vl) {
    static int print_prefix = 1;
    static char prev[1024];
    char line[1024];

    av_log_format_line(ptr, level, fmt, vl, line, sizeof(line), &print_prefix);
    strcpy(prev, line);
    if (debug1 == 1) {
        int prio;
        if (level <= AV_LOG_ERROR)
            prio = ANDROID_LOG_ERROR;
        else if (level <= AV_LOG_WARNING)
            prio = ANDROID_LOG_WARN;
        else if (level <= AV_LOG_INFO)
            prio = ANDROID_LOG_INFO;
        else if (level <= AV_LOG_VERBOSE)
            prio = ANDROID_LOG_VERBOSE;
        else
            prio = ANDROID_LOG_DEBUG;
        __android_log_print(prio, LOG_TAG, "%s", line);
    }
}

JNIEXPORT jint JNICALL JNI_METHOD_NAME(execCmd)(JNIEnv *env, jclass clazz,
                                                jobjectArray commands) {
    int argc = env->GetArrayLength(commands);
    char *argv[argc];
    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring) env->GetObjectArrayElement(commands, i);
        argv[i] = (char *) env->GetStringUTFChars(js, 0);
    }

    av_log_set_callback(log_callback_report_for_cmd);
    int ret = ffmpeg_exec(argc, argv);
    LOGEA("ret->%d", ret);
    return ret;
}