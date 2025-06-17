//
// Created by catog on 2025/6/17.
//

#include "../include/GLog.h"

std::ostream & GLog::log(const int32_t level)
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


