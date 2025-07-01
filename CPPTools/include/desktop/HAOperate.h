//
// Created by catog on 2025/7/1.
//

#ifndef HAOPERATE_H
#define HAOPERATE_H
#include <memory>
#include "desktop/HAMqtt.h"

class HAOperate {

public:
    ~HAOperate() = default;

    static std::unique_ptr<HAOperate> create();

    void start();

private:
    std::unique_ptr<HAMqtt> haMqttPtr{nullptr};
    HAOperate();

};



#endif //HAOPERATE_H
