//
// Created by catog on 2025/7/10.
//

#ifndef HAMQTTCALLBACK_H
#define HAMQTTCALLBACK_H
#include "mqtt/async_client.h"
#include "mqtt/message.h"
#include <iostream>

class HAMqtt;

using namespace mqtt;
class HAMqttCallback final : public mqtt::callback
{

public:
    explicit HAMqttCallback(HAMqtt *ha_mqtt): haMqtt{ha_mqtt} {}
    ~HAMqttCallback() override = default;
    void connected(const string& cause) override;
    /**
     * This method is called when the connection to the server is lost.
     */
    void connection_lost(const string& cause) override;
    /**
     * This method is called when a message arrives from the server.
     */
    void message_arrived(const_message_ptr msg) override;
    /**
     * Called when delivery for a message has been completed, and all
     * acknowledgments have been received.
     */
    void delivery_complete(delivery_token_ptr tok) override;


private:
    HAMqtt *haMqtt{nullptr};
};



#endif //HAMQTTCALLBACK_H
