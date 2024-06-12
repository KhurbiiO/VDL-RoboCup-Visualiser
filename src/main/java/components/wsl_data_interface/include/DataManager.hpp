#ifndef DATA_MANAGER_HPP
#define DATA_MANAGER_HPP

#include "DataStream.hpp"
#include <array>
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

    // general thread - hallway
    UdpStream hallwayStream;

    // data threads of both teams
    std::array<UdpStream, 2> streams;

  public:
    WslDataManager(const int hallWayAddr, const int streamAddr1, const int streamAdd2);

    void Processing();
    
    std::string GetDataStream(uint8_t streamCode);
  private:
    static bool VerifyWslData(std::string data);
  };
}
#endif
