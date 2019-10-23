//
// Created by 赵英坤 on 2019-10-21.
//

#ifndef TESTDEMO_ANDROIDLOG_H
#define TESTDEMO_ANDROIDLOG_H

#include <android/log.h>
#define LOG_TAG "Native"

#define LOGVA(...)       __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGDA(...)       __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGIA(...)       __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGWA(...)       __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGEA(...)       __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#endif //TESTDEMO_ANDROIDLOG_H

#ifndef ToJBool
#define ToJBool(value) value ? JNI_TRUE : JNI_FALSE;
#endif
