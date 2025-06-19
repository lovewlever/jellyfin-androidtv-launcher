//
// Created by catog on 2025/6/17.
//

#include "../include/GLog.h"
#ifdef __ANDROID__
#include <android/log.h>
#endif

std::ostream &GLog::log(const int32_t level)
{
    if (level == LogLevelError)
    {
        return std::cerr;
    }
    if (level == LogLevelInfo)
    {
        return std::cout;
    }
    return std::cout;
}

void GLog::logD(const char *tag, const char *text)
{
#ifdef WIN32
    std::cout << tag << ":: " << text << std::endl;
# elifdef  __ANDROID__
    __android_log_write(ANDROID_LOG_DEBUG, tag, text);
#endif
}

void GLog::logE(const char *tag, const char *text)
{
#ifdef WIN32
    std::cerr << tag << ":: " << text << std::endl;
# elifdef  __ANDROID__
    __android_log_write(ANDROID_LOG_ERROR, tag, text);
#endif
}
