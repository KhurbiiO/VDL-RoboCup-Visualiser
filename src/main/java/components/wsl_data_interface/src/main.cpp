#include <iostream>

#include "DataManager.hpp"

const int hallway_port = 8000;
const int stream1_port = 8001;
const int stream2_port = 8002;

const char *test = "192.168.68.107";

int main()
{
  // Test test;
    wsldata::WslDataManager manager(hallway_port, stream1_port, stream2_port);

   while(true);
  // struct sockaddr_in serverAddr;

  // // create socket for udp communication
  // int sockfd = socket(AF_INET, SOCK_DGRAM, 0);
  // if (sockfd < 0)
  // {
  //   std::cout << "create socket fail! " << std::endl;
  //   return 1;
  // }

  // //  filling server information /destination id/port
  // serverAddr.sin_family = AF_INET;
  // serverAddr.sin_port = htons(8001);
  // serverAddr.sin_addr.s_addr = inet_addr(test);

  // // bind the socket with ther server address

  // if (bind(sockfd, (const struct sockaddr *)&serverAddr, sizeof(serverAddr)) < 0)
  // {
  //   std::cout << "bin address fail! " << std::endl;
  //   return 1;
  //   //  throw BindSocketFailure("udp bind socket failure");
  // }
  // std::cout << "create server addr: " << inet_ntoa(serverAddr.sin_addr) << std::endl;

  // while (true)
  // {
  //  static struct sockaddr_in clientAddr;

  //   int n;
  //   char buffer[1000];
  //   socklen_t len = sizeof(clientAddr);
  //   n = recvfrom(sockfd, (char *)buffer, 1000, MSG_WAITALL, (struct sockaddr *)&clientAddr, &len);
  //   std::cout << "recieved: " << buffer << std::endl;
  //   std::cout << "address: " << inet_ntoa(clientAddr.sin_addr) << std::endl;
  // }

  return 0;
}
