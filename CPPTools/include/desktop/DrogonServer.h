//
// Created by catog on 2025/6/17.
//

#ifndef DROGONSERVER_H
#define DROGONSERVER_H
#include <cstdint>

class DrogonServer {
public:
    DrogonServer() = default;
    ~DrogonServer() = default;
    DrogonServer(const DrogonServer &) = delete;
    DrogonServer &operator=(const DrogonServer &) = delete;
    DrogonServer(DrogonServer &&) = delete;

    int32_t startServer(const bool threadDetach) const;
};



#endif //DROGONSERVER_H
