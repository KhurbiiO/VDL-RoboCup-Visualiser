#ifndef DATA_MANAGER_HPP
#define DATA_MANAGER_HPP

#include "DataStream.hpp"

namespace wsldata
{
  using namespace datastrm;
  enum State
  {
    IDLE,
    HANDSHAKE,
    PROCESSDATA,
    HANDLEERROR

  };

  class WslDataManager
  {
  private:
    State state;

    // // // general thread - hallway
    UdpInputStream hallwayStream;
    // // data threads of both teams
    UdpInputStream inputStream1;
    UdpInputStream inputStream2;

    UdpOutputStream outputStream1;
    UdpOutputStream outputStream2;

    // NOTE - optimize desire of verification code
    // std::thread check1;
    // std::thread check2;

  public:
    WslDataManager(const char *hallWayAddr, const char *streamAddr1, const char *streamAdd2);

    void Processing();

  private:
    // thread for verifying the data and continue the process
    static void VerifyDataTask(StreamIO *inputStream, StreamIO *outputStream);

    static bool VerifyWslData(std::string data);
  };
}
#endif
