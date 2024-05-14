#include <iostream>

#include "DataManager.hpp"

const char *hallway = "172.16.56.0";
const char *stream1 = "172.16.56.01";
const char *stream2 = "172.16.56.02";

int main()
{
    wsldata::WslDataManager manager(hallway, stream1, stream2);

    while (true)
        ;
}
