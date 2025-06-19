#ifdef __ANDROID__
#include "jni.h"
#include <HttpGQScreensaver.h>
#include <android/log.h>

// org.jellyfin.androidtv.ui.gqcustom.JNICommon
extern "C"
JNIEXPORT void JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_test(JNIEnv *env, jobject thiz)
{
    __android_log_write(ANDROID_LOG_DEBUG, "Test", "jniCall Jellyfin Cpp Tools");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_queryScreensaverImageUrlList(
    JNIEnv *env, jobject thiz, jstring hostName, jint hostPort)
{
    const auto hostN = env->GetStringUTFChars(hostName, JNI_FALSE);
    const int32_t hostP = hostPort;
    const auto jsonStr = HttpGQScreensaver::newInstance()->queryScreensaverImageList(hostN, hostP);
    env->ReleaseStringUTFChars(hostName, hostN);
    return env->NewStringUTF(jsonStr.c_str());
}

#endif
