#include <drogon/drogon.h>
#include <mobile/HttpGQScreensaver.h>

#include "include/desktop/DrogonServer.h"

using namespace drogon;

int main()
{
    const auto ds = std::make_unique<DrogonServer>();
    return ds->startServer(false);
}
