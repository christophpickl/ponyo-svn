#include <osc/OscOutboundPacketStream.h>
#include <ip/UdpSocket.h>
#include <stdio.h>

#define OUTPUT_BUFFER_SIZE 1024

int main(int argc, char* argv[]) {
	printf("A 1\n");
    UdpTransmitSocket transmitSocket(IpEndpointName("127.0.0.1", 7000));
	printf("A 2\n");
    
    char buffer[OUTPUT_BUFFER_SIZE];
	printf("A 3\n");
    osc::OutboundPacketStream p(buffer, OUTPUT_BUFFER_SIZE);
	printf("A 4\n");
    
	p << osc::BeginBundleImmediate;
	p << osc::BeginMessage("/new_user");
	p << 3;
	p << osc::EndMessage;
	p << osc::EndBundle;
	/*
    p << osc::BeginBundleImmediate
      << osc::BeginMessage( "/test1" ) 
      << true << 23 << (float)3.1415 << "hello" << osc::EndMessage
      << osc::BeginMessage( "/test2" ) 
      << true << 24 << (float)10.8 << "world" << osc::EndMessage
      << osc::EndBundle;
    */
	
	printf("transmitSocket.Send(p.Data(), p.Size())\n");
    transmitSocket.Send(p.Data(), p.Size());
	printf("A END\n");
}

