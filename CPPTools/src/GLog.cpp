//
// Created by catog on 2025/6/17.
//

#include "../include/GLog.h"

#ifdef __ANDROID__
#include <android/log.h>
#endif
#include <chrono>


std::ostream &GLog::log(const int32_t level)
{
    if (level == LogLevelError)
    {
        return std::cerr << getTimestampFormat() << ": ";
    }
    if (level == LogLevelInfo)
    {
        return std::cout << getTimestampFormat() << ": ";
    }
    return std::cout << getTimestampFormat() << ": ";
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

int64_t GLog::getTimestamp()
{
    std::chrono::time_point<std::chrono::system_clock> now = std::chrono::system_clock::now();
    const auto timestamp = std::chrono::time_point_cast<std::chrono::milliseconds>(now).time_since_epoch().count();
    return timestamp;
}

std::string GLog::getTimestampFormat()
{
    const auto now = std::chrono::system_clock::now() + std::chrono::hours(8);
    const auto time_t = std::chrono::system_clock::to_time_t(now);
    // const auto time_t = getTimestamp();
    std::stringstream ss;
    ss << std::put_time(std::gmtime(&time_t), "%Y-%m-%d %H:%M:%S");
    return ss.str();
}
