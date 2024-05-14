#include "DataStream.hpp"
#include "Utils.hpp"
#include "Config.hpp"

namespace utils
{
   uint32_t ConvertStringToAddr(const char *strAddress)
   {
#ifdef _WIN32
      return inet_addr(strAddress);
#endif
#ifdef __LINUX__
      return inet_pton(strAddress);
#endif
   }

}

namespace datastrm
{
   using namespace custom_exception;
   using namespace stream_config;

   /// UDP STREAM ///
   UdpStream::UdpStream(bool isInput): processDataTask(&UdpStream::ProcessDataTask,this)
   {
      // create socket for udp communication
      int sockfd = socket(AF_INET, SOCK_DGRAM, 0);
      if (sockfd < 0)
      {
         throw ObjConstructedFailure("fail to construct DataStream");
      }

      // filling server information /destination id/port
      serverAddr.sin_family = AF_INET;
      serverAddr.sin_port = htons(DEFAULT_PORT);
      serverAddr.sin_addr.s_addr = INADDR_ANY;

      // bind the socket with ther server address
      if (isInput)
      {
         if (bind(sockfd, (const struct sockaddr *)&serverAddr, sizeof(serverAddr)) < 0)
         {
            throw BindSocketFailure("udp bind socket failure");
         }
      }

   }

   UdpStream::~UdpStream()
   {
   }

   void UdpStream::SwitchThreadMode(bool isOn)
   // FIXME - what if turn on or off 2 times continuously
   {
      if (isOn && !isRun)
      {
         isRun = true;
         processDataTask.detach(); 
      }
   }

   int UdpStream::GetSockfd()
   {
      return sockfd;
   }

   void UdpStream::SetAddress(uint32_t newAddress)
   {
      serverAddr.sin_addr.s_addr = newAddress;
   }

   uint32_t UdpStream::GetAddress()
   {
      return serverAddr.sin_addr.s_addr;
   }

   sockaddr_in *UdpStream::GetSocket()
   {
      return &serverAddr;
   }

   std::mutex &UdpStream::GetMutex()
   {
      return queueAccessMutex;
   }

   void UdpStream::PushData(std::pair<uint32_t, std::string> dataPkg)
   {
      std::unique_lock lock(queueAccessMutex);
      dataQ.push(dataPkg);
   }

   std::pair<uint32_t, std::string> UdpStream::PullData()
   {
      std::pair<uint32_t, std::string> dataPkg = {0, "none"};

      if (dataQ.size() > 0)
      // only read the queue if there is member init
      {
         std::unique_lock lock(queueAccessMutex);
         // copy the data and delete it from the package
         auto &refData = dataQ.front();
         dataPkg = refData;
         dataQ.pop();
      }

      return dataPkg;
   }

   /// UDP INPUT STREAM ///

   UdpInputStream::UdpInputStream(const char *address) : UdpStream(true)
   // construction of Input stream
   {
      SetAddress(utils::ConvertStringToAddr(address));
   }

   json UdpInputStream::GetStreamInfo()
   {
      json infoPackage = {
          {"type", "wsl_data_stream"},
          {"id", GetSockfd()},
          {"addr", GetAddress()}};

      return infoPackage;
   }

   std::pair<uint32_t, std::string> UdpInputStream::RecieveData(int sockfd)
   // perform the reading task and return
   {
      sockaddr_in clientAddr;

      int n;
      char buffer[1000];
      int len = sizeof(clientAddr);

      n = recvfrom(sockfd, buffer, 1000, MSG_WAITALL, (struct sockaddr *)&clientAddr, &len);
      buffer[n] = '\0';
      std::pair<uint32_t, std::string> recievedata = {clientAddr.sin_addr.s_addr, std::string(buffer)};

      return recievedata;
   }

   void UdpInputStream::ProcessDataTask()
   {
      while (true)
      {
         std::pair<uint32_t, std::string> dataPackage = UdpInputStream::RecieveData(GetSockfd());

         // wait until the message come and push the message in queue
         PushData(dataPackage);
      }
   }

   UdpOutputStream::UdpOutputStream() : UdpStream(false)
   {
   }

   /// UDP OUTPUT STREAM ///
   bool UdpOutputStream::SendData(std::pair<uint32_t, std::string> dataPackage)
   {
      SetAddress(dataPackage.first);
      sendto(GetSockfd(), dataPackage.second.c_str(), dataPackage.second.length(), 0, (const struct sockaddr *)GetSocket(), sizeof(*GetSocket()));
   }

   void UdpOutputStream::ProcessDataTask()
   {
      std::pair<uint32_t, std::string> dataPackage = {0, "none"};
      while (true)
      {
         dataPackage = PullData();
         if (dataPackage.second != "None")
         {
            SendData(dataPackage);
         }
      }
   }
}


