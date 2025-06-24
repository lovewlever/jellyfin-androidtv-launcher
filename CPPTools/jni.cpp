#ifdef __ANDROID__
#include "jni.h"

#include <android/log.h>
#include <mutex>

#include "Constants.h"
#include "GLog.h"
#include "mobile/HttpGQScreensaver.h"
#include "mobile/ScreensaverImageList.h"
#include "nlohmann/json.hpp"


/**
 * 初始化
 * @param env
 * @param thiz
 * @param packageFolder 包目录路径
 * @return
 */
extern "C"
JNIEXPORT jint JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_init(JNIEnv *env, jobject thiz, jstring cacheDirPath, jboolean debug)
{
    const auto pf = env->GetStringUTFChars(cacheDirPath, JNI_FALSE);
    std::string copy{pf};
    GLog::EnableLog = debug;
    GLog::logD("JNIInit: packageFolder: ", pf);
    Constants::setMobilePackageCacheDirPath(std::move(copy));
    env->ReleaseStringUTFChars(cacheDirPath, pf);
    GLog::logD("JNIInit: packageFolder: Return 0", "");
    return 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_setCurrentServerUrl(JNIEnv *env, jobject thiz, jstring serverUrl)
{
    const auto su = env->GetStringUTFChars(serverUrl, JNI_FALSE);
    std::string copy{su};
    GLog::logD("JNIInit: setCurrentServerUrl: ", su);
    Constants::setMobileServerUrl(std::move(copy));
    env->ReleaseStringUTFChars(serverUrl, su);
    GLog::logD("JNIInit: setCurrentServerUrl", "Return 0");
    return 0;
}

// org.jellyfin.androidtv.ui.gqcustom.JNICommon
extern "C"
JNIEXPORT void JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_test(JNIEnv *env, jobject thiz)
{
    __android_log_write(ANDROID_LOG_DEBUG, "Test", "jniCall Jellyfin Cpp Tools");
}

/**
 * 获取从远程服务器查询到的图片列表
 * @param env
 * @param thiz
 * @return
 */
extern "C"
JNIEXPORT jobject JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_getScreensaverRemoteImageList(JNIEnv *env, jobject thiz)
{
    const auto arrLstClass = env->FindClass("java/util/ArrayList");
    const auto arrLstMethodId = env->GetMethodID(arrLstClass, "<init>", "()V");
    const auto arrLstObject = env->NewObject(arrLstClass, arrLstMethodId);
    const auto arrLstAddMethodId = env->GetMethodID(arrLstClass, "add", "(Ljava/lang/Object;)Z");

    const auto listStr = ScreensaverImageList::getScreensaverRemoteImageList();
    if (listStr != nullptr)
    {
        GLog::logD("getScreensaverRemoteImageList: ", listStr->c_str());
        try
        {
            for (const auto jsonArr = nlohmann::json::parse(listStr->c_str()); const auto &item: jsonArr)
            {
                const auto javaStr = env->NewStringUTF(item.get<std::string>().c_str());
                env->CallBooleanMethod(arrLstObject, arrLstAddMethodId, javaStr);
                env->DeleteLocalRef(javaStr);
            }

            return arrLstObject;
        } catch (const std::exception &e)
        {
            return arrLstObject;
        }
    }
    GLog::logD("getScreensaverRemoteImageList: ", "nullptr");
    return arrLstObject;
}

/**
 * 从远程服务器查询图片列表
 * @param env
 * @param thiz
 * @param hostName
 * @param hostPort
 * @return
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_queryScreensaverImageUrlList(
    JNIEnv *env, jobject thiz, jstring hostName, jint hostPort)
{
    const auto hostN = env->GetStringUTFChars(hostName, JNI_FALSE);
    const int32_t hostP = hostPort;
    const auto jsonStr = HttpGQScreensaver::newInstance()->queryScreensaverImageList(hostN, hostP);
    ScreensaverImageList::updateScreensaverRemoteImageList(jsonStr);
    env->ReleaseStringUTFChars(hostName, hostN);
    return env->NewStringUTF(jsonStr.c_str());
}

/**
 * 下载远程图片到本地目录
 * init(cacheDirPath: String, debug: Boolean)的cacheDirPath
 * @param env
 * @param thiz
 * @return
 */
extern "C"
JNIEXPORT jint JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_downloadScreensaverRemoteImageListToLocalPath(JNIEnv *env, jobject thiz)
{
    try
    {
        const auto list = ScreensaverImageList::getScreensaverRemoteImageList();
        const auto str = *list;
        const auto imageList = nlohmann::json::parse(*list);
        ScreensaverImageList::downloadScreensaverRemoteImageListToLocalPath(imageList);
    } catch (const std::exception &e)
    {
        GLog::logE("下载远程图片到本地目录: ", ("ERROR: " + std::string{e.what()}).c_str());
        return -1;
    }

    GLog::logD("下载远程图片到本地目录: ", "Success");
    return 0;
}

#endif
