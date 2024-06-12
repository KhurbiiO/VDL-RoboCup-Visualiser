#include "DataStream.hpp"
#include "Utils.hpp"
#include "Config.hpp"

namespace datastrm
{
   using namespace custom_exception;
   using namespace stream_config;

   UdpStream::UdpStream(const int port) : sockfd(-1), isRun(false)
   {
      // create socket for udp communication
      sockfd = socket(AF_INET, SOCK_DGRAM, 0);
      if (sockfd < 0)
      {
         // throw ObjConstructedFailure("fail to construct DataStream");
      }

      // filling server information /destination id/port
      serverAddr.sin_family = AF_INET;
      serverAddr.sin_port = htons(port);
      serverAddr.sin_addr.s_addr = INADDR_ANY; // inet_addr(DEVICE_ADRESS);

      // bind the socket with ther server address
      if (bind(int(sockfd), (const struct sockaddr *)&serverAddr, sizeof(serverAddr)) < 0)
      {
         // throw BindSocketFailure("udp bind socket failure");
         std::cout << " bind socket failure " << std::endl;
      }

      std::cout << " port: " << port << std::endl;

      recieveDataTask = new std::thread(&UdpStream::RecieveDataTask, this);
   }

   UdpStream::~UdpStream()
   {
   }

   void UdpStream::SwitchThreadMode(bool isOn)
   // FIXME - what if turn on or off 2 times continuously
   {
      std::cout << " thread is " << isOn << std::endl;
      if (isOn && !isRun)
      {
         isRun = true;
         recieveDataTask->detach();
      }
   }

   int UdpStream::GetSockfd()
   {
      return sockfd;
   }

   uint32_t UdpStream::GetAddress()
   {
      return serverAddr.sin_addr.s_addr;
   }

   size_t UdpStream::GetSize()
   {

      return dataQ.size();
   }

   void UdpStream::PushData(std::string dataPkg)
   {
      std::unique_lock lock(queueAccessMutex);
      dataQ.push(dataPkg);

      std::cout << "done pushed ======" << std::endl;
      // std::cout << "get size: " << GetSize() << std::endl;
      // std::cout << "port: " << port << std::endl;
   }

   std::string UdpStream::PullData()
   {
      std::string dataPkg = "";
      std::cout << "get size: " << dataQ.size() << std::endl;

      if (dataQ.size() > 0)
      // only read the queue if there is member init
      {
         std::unique_lock lock(queueAccessMutex);

         // copy the data and delete it from the package
         auto &refData = dataQ.front();
         dataPkg = refData;
         dataQ.pop();
         std::cout << "done Pull ======" << std::endl;
      }

      return dataPkg;
   }

   json UdpStream::GetStreamInfo()
   {
      json infoPackage = {
          {"type", "wsl_data_stream"},
          {"id", GetSockfd()},
          {"addr", GetAddress()}};

      return infoPackage;
   }

   std::string UdpStream::RecieveData(int sockfd)
   // perform the reading task and return
   {
      struct sockaddr_in clientAddr;

      int n;
      char buffer[1000];
      socklen_t len = sizeof(clientAddr);
      n = recvfrom(sockfd, buffer, 1000, MSG_WAITALL, (struct sockaddr *)&clientAddr, &len);
      buffer[n] = '\0';

      std::string recievedata = std::string(buffer);
      std::cout << recievedata << std::endl;

      return recievedata;
   }

   void UdpStream::RecieveDataTask()
   {
      std::string dataPackage;

      while (true)
      {
         dataPackage = RecieveData(GetSockfd());

         // wait until the message come and push the message in queue
         PushData(dataPackage);
      }
   }

}
