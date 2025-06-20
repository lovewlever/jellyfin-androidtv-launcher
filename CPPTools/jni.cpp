#ifdef __ANDROID__
#include "jni.h"

#include <complex>
#include <HttpGQScreensaver.h>
#include <android/log.h>
#include <mutex>

#include "GLog.h"
#include "nlohmann/json.hpp"

static auto screensaverRemoteImageList{std::make_shared<std::string>("")};
static std::mutex screensaverMutex{};

std::shared_ptr<std::string> getScreensaverRemoteImageList()
{
    GLog::logD("getImageList: ", "====");
    std::lock_guard<std::mutex> lock(screensaverMutex);
    return screensaverRemoteImageList;
}

void updateScreensaverRemoteImageList(const std::string &imagesStr)
{
    GLog::logD("updateScreensaverRemoteImageList: ", imagesStr.c_str());
    std::lock_guard<std::mutex> lock(screensaverMutex);
    screensaverRemoteImageList = std::make_shared<std::string>(imagesStr);
    GLog::logD("updateScreensaverRemoteImageList: ", "Done");
}

// org.jellyfin.androidtv.ui.gqcustom.JNICommon
extern "C"
JNIEXPORT void JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_test(JNIEnv *env, jobject thiz)
{
    __android_log_write(ANDROID_LOG_DEBUG, "Test", "jniCall Jellyfin Cpp Tools");
}

extern "C"
JNIEXPORT jobject JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_getScreensaverRemoteImageList(JNIEnv *env, jobject thiz)
{
    const auto arrLstClass = env->FindClass("java/util/ArrayList");
    const auto arrLstMethodId = env->GetMethodID(arrLstClass, "<init>", "()V");
    const auto arrLstObject = env->NewObject(arrLstClass, arrLstMethodId);
    const auto arrLstAddMethodId = env->GetMethodID(arrLstClass, "add", "(Ljava/lang/Object;)Z");

    const auto listStr = getScreensaverRemoteImageList();
    if (listStr != nullptr)
    {
        GLog::logD("getScreensaverRemoteImageList: ", listStr->c_str());
        try
        {
            for (const auto jsonArr = nlohmann::json::parse(listStr->c_str()); const auto &item : jsonArr)
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

extern "C"
JNIEXPORT jstring JNICALL
Java_org_jellyfin_androidtv_ui_gqcustom_JNICommon_queryScreensaverImageUrlList(
    JNIEnv *env, jobject thiz, jstring hostName, jint hostPort)
{
    const auto hostN = env->GetStringUTFChars(hostName, JNI_FALSE);
    const int32_t hostP = hostPort;
    const auto jsonStr = HttpGQScreensaver::newInstance()->queryScreensaverImageList(hostN, hostP);
    updateScreensaverRemoteImageList(jsonStr);
    env->ReleaseStringUTFChars(hostName, hostN);
    return env->NewStringUTF(jsonStr.c_str());
}


#endif
