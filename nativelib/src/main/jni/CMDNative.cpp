//
// Created by 赵英坤 on 2019-10-22.
//

#include "CMDNative.h"
#include <jni.h>
#include <cstdlib>
#include <ffmpeglib/ffmpegbin/ffmpeg.h>
#include "ffmpeg.h"

#ifndef LOG_TAG
#define LOG_TAG "JNI_SRCNativeJNI"
#endif

using namespace std;
//两个类名，必须要修改 raindream.yocn.nativelib
#define JNI_METHOD_NAME(name) Java_raindream_yocn_nativelib_NativeJNI_##name
static const char *CLASSPATH = "raindream/yocn/nativelib/NativeJNI";

extern "C" {
JNIEXPORT void JNICALL JNI_METHOD_NAME(helloFFmpeg)(JNIEnv *env, jobject thiz);
JNIEXPORT jint JNICALL JNI_METHOD_NAME(execCmd)(JNIEnv *env, jclass clazz,
                                                jobjectArray commands);
}

JNIEXPORT void JNICALL JNI_METHOD_NAME(helloFFmpeg)(JNIEnv *env, jobject thiz) {
//    ffmpeg_exec();
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

//    av_log_set_callback(ffp_log_callback_report);
//    int ret = ffmpeg_exec(argc, argv);
    return 0;
}