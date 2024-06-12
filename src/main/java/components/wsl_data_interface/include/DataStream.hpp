#ifndef DATASTREAM_HPP
#define DATASTREAM_HPP

#include <iostream>

#ifdef _WIN32
#include <winsock2.h>

#endif

#ifdef __linux
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#endif

#include <queue>
#include <utility>
#include <thread>
#include <mutex>
#include <atomic>
#include "Json.hpp"

namespace datastrm
{
  using json = nlohmann::json;

  /*UDP message
   - the UDP header only contain the address and port of the destination
   - the port and the address on the sender side are dynamically assigned by the system,
   or it can also be bound manually by the application.
  */
  class StreamIO
  {
  public:
    virtual void PushData(std::string) = 0;

    virtual std::string PullData() = 0;
  };

  class UdpStream : public StreamIO
  {
  private:
    // udp communication socket
    std::atomic<int> sockfd;
    struct sockaddr_in serverAddr; // FIXME - need toe be threadsafe handle

    // task management initalisation
    std::atomic<bool> isRun;
    std::mutex queueAccessMutex;

    // queue of stream data waiting to be processed
    std::queue<std::string> dataQ;
    std::thread *recieveDataTask;

  public:
    UdpStream(const int);

    ~UdpStream();

    // converting the info of socket add and port into json object
    json GetStreamInfo();

    void SwitchThreadMode(bool isOn);

    int GetSockfd();

    uint32_t GetAddress();

    size_t GetSize();

    // push data into sent queue
    void PushData(std::string) override;

    // pull received data from queue
    std::string PullData() override;

    void RecieveDataTask();

    static std::string RecieveData(int sockfd);
  };
}

#endif