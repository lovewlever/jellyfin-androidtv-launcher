//
// Created by catog on 2025/6/24.
//

#include "../include/Constants.h"


std::string Constants::mobilePackageCacheDirPath;

std::string Constants::getMobilePackageCacheDirPath()
{
    return mobilePackageCacheDirPath;
}

void Constants::setMobilePackageCacheDirPath(const std::string &&value)
{
    mobilePackageCacheDirPath = value;
}

std::string Constants::getMobileScreensaverImageListDirPath()
{
    return mobilePackageCacheDirPath + mobilePkgCacheDirPathChildFolder;
}

std::string Constants::getMobileServerUrl()
{
    return mobileServerUrl;
}

void Constants::setMobileServerUrl(const std::string &&value)
{
    mobileServerUrl = value;
}
