//
// Created by catog on 2025/6/17.
//

#ifndef HTTPGQSCREENSAVER_H
#define HTTPGQSCREENSAVER_H

#include <memory>
#include <vector>
#include <string>

class HttpGQScreensaver {

public:
    ~HttpGQScreensaver() = default;
    HttpGQScreensaver(const HttpGQScreensaver&) = delete;
    HttpGQScreensaver& operator=(const HttpGQScreensaver&) = delete;

    static std::unique_ptr<HttpGQScreensaver> newInstance();

    std::string queryScreensaverImageList(const std::string &hostName, int32_t hostPort) const;

private:
    HttpGQScreensaver() = default;
};



#endif //HTTPGQSCREENSAVER_H
