#include <ponyo/osc/PnOSC.hpp>
#include <ip/UdpSocket.h>
#include <osc/OscOutboundPacketStream.h>

namespace pn {

Log* PnOSC::LOG = NEW_LOG();

PnOSC::PnOSC() {
	LOG->debug("new PnOSC()");
}

PnOSC::~PnOSC() {
	LOG->debug("~PnOSC()");
}

void PnOSC::startup() {
	LOG->debug("startup()");
    UdpTransmitSocket transmitSocket(IpEndpointName("127.0.0.1", 7000));
    char buffer[1024];
    osc::OutboundPacketStream p(buffer, 1024);
    p << osc::BeginBundleImmediate
        << osc::BeginMessage( "/test1" )
            << true << 23 << (float)3.1415 << "hello" << osc::EndMessage
        << osc::BeginMessage( "/test2" )
            << true << 24 << (float)10.8 << "world" << osc::EndMessage
        << osc::EndBundle;

    transmitSocket.Send(p.Data(), p.Size());
}

}
