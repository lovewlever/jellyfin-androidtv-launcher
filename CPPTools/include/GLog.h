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
    inline static int32_t EnableLog{true};

    static void generateLogFile();

    static std::ostream & log(int32_t level = LogLevelInfo);

    static void logD(const char* tag, const char* text);
    static void logE(const char* tag, const char* text);

    static int64_t getTimestamp();
    static std::string getTimestampFormat();
    class NullLogStream final : public std::ostream
    {
    public:
        NullLogStream() : std::ostream(nullptr) {}
    };

    inline static NullLogStream nullLogStream{};

private:


    static bool writeToFile;
    static std::unique_ptr<std::ofstream> logFileOfsPtr;
};

#endif //GLOG_H
