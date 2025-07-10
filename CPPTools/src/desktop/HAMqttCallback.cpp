//
// Created by catog on 2025/7/10.
//

#include "desktop/HAMqttCallback.h"
#include "desktop/HAMqtt.h"

void HAMqttCallback::connected(const string& cause)
{
    std::cout << "connected: " << cause << std::endl;
    if (haMqtt != nullptr)
    {
        haMqtt->publish("Hello");
        const auto topicName = haMqtt->TOPIC_NAME;
        haMqtt->subscribe(std::move(topicName));
    }
}
/**
 * This method is called when the connection to the server is lost.
 */
void HAMqttCallback::connection_lost(const string& cause)
{
    std::cout << "connection lost: " << cause << std::endl;
}
/**
 * This method is called when a message arrives from the server.
 */
void HAMqttCallback::message_arrived(const_message_ptr msg)
{
    std::cout << "message_arrived: " << msg->get_payload_str() << std::endl;
    if (haMqtt != nullptr)
    {
        haMqtt->message_arrived(msg);
    }
}
/**
 * Called when delivery for a message has been completed, and all
 * acknowledgments have been received.
 */
void HAMqttCallback::delivery_complete(delivery_token_ptr tok)
{
    std::cout << "delivery_complete: " << tok->get_message() << std::endl;
}