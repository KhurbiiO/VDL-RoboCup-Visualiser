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
  //  TODO - thread management with isRun: bool.
  //  TODO - handle threadsafe for setAddress (is necessary?)
  //  TODO - becareful when object is accientally deleted, resource is gone but thread  still runs

  //  TODO - check if needing to use memset(&serverAddr, 0, sizeof(serverAddr)); for initialisation
  //  TODO - check necessary flag in sendto(GetSockfd(),dataPackage.second.c_str(),dataPackage.second.length(), 0, (const struct sockaddr *) &GEE, sizeof(serverAddr));

  class StreamIO
  {
  public:
    virtual void PushData(std::pair<uint32_t, std::string>) = 0;

    virtual std::pair<uint32_t, std::string> PullData() = 0;
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
    std::queue<std::pair<uint32_t, std::string>> dataQ;

  public:
    std::thread *processDataTask;

    UdpStream();

    UdpStream(const int);

    ~UdpStream();

    void SwitchThreadMode(bool isOn);

    int GetSockfd();

    struct sockaddr_in *GetSocket();


    void SetAddress(uint32_t);

    uint32_t GetAddress();

    std::mutex &GetMutex();

    // push data into sent queue
    void PushData(std::pair<uint32_t, std::string>) override;

    // pull received data from queue
    std::pair<uint32_t, std::string> PullData() override;

    // virtual void ProcessDataTask() = 0;
  };

  class UdpInputStream : public UdpStream
  {
  private:
  public:
    UdpInputStream(const int);

    ~UdpInputStream();

    // converting the info of socket add and port into json object
    json GetStreamInfo();

  private:
    // implementing thread for reading the udp data
    void ProcessDataTask();

    // reading the udp message and returning the info of sender addr and data
    static std::pair<uint32_t, std::string> RecieveData(int sockfd);
  };

  class UdpOutputStream : public UdpStream
  {
  private:
  public:
    UdpOutputStream();

    ~UdpOutputStream();

  private:
    // implementing thread for sending the udp data
    void ProcessDataTask();
    // sending the the udp message to the provided address
    // NOTE - should be called by ProcessDataTask for thread safe - due to address setting
    bool SendData(std::pair<uint32_t, std::string>);
  };
}
#endif