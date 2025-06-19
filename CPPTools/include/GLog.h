//
// Created by catog on 2025/6/17.
//

#ifndef GLOG_H
#define GLOG_H
#include <cstdint>
#include <iostream>

class GLog {
public:
    GLog() = delete;
    ~GLog() = delete;
    GLog(const GLog &) = delete;
    GLog &operator=(const GLog &) = delete;

    inline static int32_t LogLevelInfo{0};
    inline static int32_t LogLevelError{1};

    static std::ostream & log(int32_t level = LogLevelInfo);

    static void logD(const char* tag, const char* text);
    static void logE(const char* tag, const char* text);

    static int64_t getTimestamp();
    static std::string getTimestampFormat();
private:

};

#endif //GLOG_H
