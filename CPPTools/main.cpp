#include "DrogonConfig.h"
#include <drogon/drogon.h>
#include <HttpGQScreensaver.h>

#include "DrogonServer.h"
#include "GLog.h"

using namespace drogon;

int main()
{
    const auto ds = std::make_unique<DrogonServer>();
    return ds->startServer(false);
}
