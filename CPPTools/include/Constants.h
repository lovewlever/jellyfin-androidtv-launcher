//
// Created by catog on 2025/6/24.
//

#ifndef CONSTANTS_H
#define CONSTANTS_H
#include <string>


class Constants
{
public:
    static std::string getMobilePackageCacheDirPath();
    static void setMobilePackageCacheDirPath(const std::string &&value);

    static std::string getMobileServerUrl();
    static void setMobileServerUrl(const std::string &&value);
private:
    static std::string mobilePackageCacheDirPath;
    inline static std::string mobileServerUrl{};
};


#endif //CONSTANTS_H
