//
// Created by catog on 2025/6/17.
//

#include "../include/GLog.h"

#ifdef __ANDROID__
#include <android/log.h>
#endif
#include <chrono>
#include <iomanip>
#include <filesystem>
#include <fstream>


bool GLog::writeToFile{false};
std::unique_ptr<std::ofstream> GLog::logFileOfsPtr{nullptr};

void GLog::generateLogFile()
{
    if (!EnableLog) return;
    const auto now = std::chrono::system_clock::now() + std::chrono::hours(8);
    const auto time_t = std::chrono::system_clock::to_time_t(now);
    std::stringstream ss{};
    ss << "log/" << std::put_time(std::gmtime(&time_t), "%Y-%m-%d_%H") << ".log";

   if (!std::filesystem::is_directory("log"))
   {
       std::filesystem::create_directory("log");
   }

    if (!std::filesystem::exists(ss.str()))
    {
        std::fstream fss(ss.str(), std::ios::out | std::ios::trunc);
        fss.close();
    }

    if (auto oss = std::make_unique<std::ofstream>(ss.str(), std::ios::binary | std::ios::app); oss->is_open())
    {
        writeToFile = true;
        logFileOfsPtr = std::move(oss);
    }
}

std::ostream &GLog::log(const int32_t level)
{
    if (!EnableLog) return nullLogStream;
    if (level == LogLevelError)
    {
        if (writeToFile)
        {
            if (logFileOfsPtr != nullptr && logFileOfsPtr->is_open())
            {
                return *logFileOfsPtr << getTimestampFormat() << ": ";
            }
        }
        return std::cerr << getTimestampFormat() << ": ";
    }
    if (level == LogLevelInfo)
    {
        if (writeToFile)
        {
            if (logFileOfsPtr != nullptr && logFileOfsPtr->is_open())
            {
                return *logFileOfsPtr << getTimestampFormat() << ": ";
            }
        }
        return std::cout << getTimestampFormat() << ": ";
    }
    return std::cout << getTimestampFormat() << ": ";
}

void GLog::logD(const char *tag, const char *text)
{
#ifdef WIN32
    std::cout << tag << ":: " << text << std::endl;
# elifdef  __ANDROID__
    if (!EnableLog) return;
    __android_log_write(ANDROID_LOG_DEBUG, tag, text);
#endif
}

void GLog::logE(const char *tag, const char *text)
{
#ifdef WIN32
    std::cerr << tag << ":: " << text << std::endl;
# elifdef  __ANDROID__
    if (!EnableLog) return;
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
