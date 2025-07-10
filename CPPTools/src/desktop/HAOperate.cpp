//
// Created by catog on 2025/7/1.
//

#include "../../include/desktop/HAOperate.h"

#include <iostream>
#include <ostream>

HAOperate::HAOperate(): haMqttPtr{std::make_unique<HAMqtt>()}
{
}

std::unique_ptr<HAOperate> HAOperate::create()
{
    return std::unique_ptr<HAOperate>(new HAOperate());
}

void HAOperate::start()
{
    std::cout << "HAOperate::start()" << std::endl;
    haMqttPtr->listen();
}


