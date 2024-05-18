#include "DataManager.hpp"
#include "Config.hpp"

namespace wsldata
{
    using namespace stream_config;
    WslDataManager::WslDataManager(const int hallwayPort, const int stream1port, const int stream2port) : hallwayStream(hallwayPort), inputStream1(stream1port), inputStream2(stream2port)
    {
        // int sockfd = socket(AF_INET, SOCK_DGRAM, 0);

        // // NOTE - for testing purpose - test stream data processing and verification - stream should only be turn on when connection is completely established
        inputStream1.SwitchThreadMode(true);
        //  inputStream2.SwitchThreadMode(true);

        outputStream1.SwitchThreadMode(true);
        // outputStream2.SwitchThreadMode(true);

        std::thread check1(&WslDataManager::VerifyDataTask, this, (StreamIO *)&inputStream1, (StreamIO *)&outputStream1);
        // std::thread check2(&WslDataManager::VerifyDataTask,&inputStream2, &outputStream2);

        check1.detach();
        std::cout << "hello!" << std::endl;
        // check2.detach();
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

    void WslDataManager::VerifyDataTask(StreamIO *inputStream, StreamIO *outputStream)
    {
        if (inputStream == nullptr || outputStream == nullptr)
        {
            return;
        }

        std::pair<uint32_t, std::string> wslData = {0, "none"};

        while (true)
        {
            wslData = inputStream->PullData();
            if (wslData.second != "None" && WslDataManager::VerifyWslData(wslData.second))
            {

                outputStream->PushData(wslData);
                std::cout << wslData.second << std::endl;

                wslData = {0, "none"};
            }
        }
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