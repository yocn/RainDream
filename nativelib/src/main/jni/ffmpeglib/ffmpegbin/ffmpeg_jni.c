#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include "ffmpeg.h"

#define LOG_TAG "ffmpeg_bin_jni"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

FILE *file;
int debug = 0;//log开关

JavaVM *jvm = NULL;
jclass m_clazz = NULL;//当前类(面向java)
jobject m_instance = NULL;//当前类实例(面向java)

void calculate_process_time(char *buf) {

    char str[100];
    sscanf(buf, "%*[^t]time=%8s", str);

    if (strlen(str) == 8 && str[2] == ':' && str[5] == ':') {
        int hour, minute, second;
        sscanf(str, "%d:%d:%d", &hour, &minute, &second);
        if (hour < 0 || minute < 0 || second < 0)return;
        int total = hour * 3600 + minute * 60 + second;

        JNIEnv *env;
        (*jvm)->AttachCurrentThread(jvm, (void **) &env, NULL);

        if (m_clazz == NULL) {
            LOGE("---------------clazz isNULL---------------");
            return;
        }
        jmethodID methodID = (*env)->GetMethodID(env, m_clazz, "processCallback", "(I)V");
        if (methodID == NULL || m_instance == NULL) {
            LOGE("---------------methodID isNULL---------------");
            return;
        }
        (*env)->CallVoidMethod(env, m_instance, methodID, total);
    }

}


void log_callback_report_for_cmd_with_log(void *ptr, int level, const char *fmt, va_list vl) {
    static int print_prefix = 1;
    static int count;
    static char prev[1024];
    char line[1024];
    static int is_atty;

    av_log_format_line(ptr, level, fmt, vl, line, sizeof(line), &print_prefix);

    strcpy(prev, line);

    if (level == AV_LOG_INFO) {
        calculate_process_time(line);
    }

    if (file != NULL) {
        fputs(line, file);
        fputs("\n", file);
        fflush(file);
    }

    if (debug == 1) {
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
        __android_log_print(prio, "ffmpeg", "%s", line);
    }
}

JNIEXPORT jint JNICALL
Java_com_yoyo_jni_avffmpeg_FFmpegCMD_execCmdWithLog(JNIEnv *env, jclass clazz,
                                             jobjectArray commands, jobject instance) {

    (*env)->GetJavaVM(env, &jvm);
    m_clazz = (*env)->NewGlobalRef(env, clazz);
    m_instance = (*env)->NewGlobalRef(env, instance);
    int argc = (*env)->GetArrayLength(env, commands);
    char *argv[argc];
    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring) (*env)->GetObjectArrayElement(env, commands, i);
        argv[i] = (char *) (*env)->GetStringUTFChars(env, js, 0);
    }

    struct timeval tv;
    gettimeofday(&tv, NULL);
    char f_name[100];

    pid_t pid = getpid();
    char path[64] = {0};
    sprintf(path, "/proc/%d/cmdline", pid);
    FILE *cmdline = fopen(path, "r");
    char application_id[64] = {0};
    if (cmdline) {
        fread(application_id, sizeof(application_id), 1, cmdline);
        fclose(cmdline);
    }

    sprintf(f_name, "/sdcard/Android/data/%s/cache/ffmpeg_log/ffmpeg_%ld.log",
            application_id, (tv.tv_sec * 1000. + tv.tv_usec / 1000.));
    file = fopen(f_name, "w+");
    av_log_set_callback(log_callback_report_for_cmd_with_log);
    int ret = ffmpeg_exec(argc, argv);
    if (file != NULL) {
        fclose(file);
        file = NULL;
    }
    if (ret == 0) {
        remove(f_name);
    }
    m_instance = NULL;
    m_clazz = NULL;
    return ret;
}



void log_callback_report_for_cmd(void *ptr, int level, const char *fmt, va_list vl) {
    static int print_prefix = 1;
    static char prev[1024];
    char line[1024];

    av_log_format_line(ptr, level, fmt, vl, line, sizeof(line), &print_prefix);
    strcpy(prev, line);
    if (debug == 1) {
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

JNIEXPORT jint JNICALL
Java_com_yoyo_jni_avffmpeg_FFmpegCMD_execCmd(JNIEnv *env, jclass clazz,
                                             jobjectArray commands) {
    int argc = (*env)->GetArrayLength(env, commands);
    char *argv[argc];
    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring) (*env)->GetObjectArrayElement(env, commands, i);
        argv[i] = (char *) (*env)->GetStringUTFChars(env, js, 0);
    }

    av_log_set_callback(log_callback_report_for_cmd);
    int ret = ffmpeg_exec(argc, argv);
    av_log_set_callback(NULL);
    return ret;
}

JNIEXPORT jint JNICALL
Java_com_yoyo_jni_avffmpeg_FFmpegCMD_setDebugLevel(JNIEnv *env, jclass type,
                                                   jint debuglevel) {
    debug = debuglevel;
    return 0;
}

