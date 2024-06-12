#include "DataManager.hpp"
#include "Config.hpp"

std::string GetLibraryInfo()
{
    return std::string("hello from data manager library");
}

namespace wsldata
{
    std::string GetNamespaceInfo()
    {
        return std::string("hello from wsldata namspace");
    }

    using namespace stream_config;
    WslDataManager::WslDataManager(const int hallwayPort, const int port1, const int port2) : hallwayStream(hallwayPort), streams{UdpStream(port1), UdpStream(port2)}
    {
        // int sockfd = socket(AF_INET, SOCK_DGRAM, 0);

        // NOTE - for testing purpose - test stream data processing and verification - stream should only be turn on when connection is completely established
        streams[0].SwitchThreadMode(true);
        std::cout << "hello!" << std::endl;
    }
    
    std::string GetInfo()
    {
        return std::string("hello from wsl data manager!");
    }

    void WslDataManager::Processing()
    {
        switch (state)
        {
        case IDLE:
            // wait for the connection fromthe software

            break;
        case HANDSHAKE:
            // handshake with the reflebox to establish the data stream

            break;
        case PROCESSDATA:
            // proccessing the data steams

            break;
        case HANDLEERROR:
            // handle the connection error
            break;
        }
    }

    std::string WslDataManager::GetDataStream(uint8_t streamCode)
    {
        if (streamCode >= 2)
        {
            return "";
        }

        std::string data = "";
        bool result = false;

        while (streams[0].GetSize() > 0 && !result)
        {
            data = streams[0].PullData();
            // verify data - keep geting new data if old data is not valid
            result = WslDataManager::VerifyWslData(data);
        }

        return data;
    }

    bool WslDataManager::VerifyWslData(std::string data)
    {
        if (!json::accept(data))
        {
            return false;
        }

        json wsl_data = json::parse(data);
        return true;
    }
    
}