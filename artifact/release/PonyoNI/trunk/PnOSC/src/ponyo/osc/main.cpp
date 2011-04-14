#include <ip/UdpSocket.h>
#include <osc/OscOutboundPacketStream.h>
#include <ponyo/PnOpenNI.hpp>

#define OSC_ADDRESS_USERSTATE "/user_state"
#define OSC_ADDRESS_JOINTDATA "/joint_data"
#define OSC_MESSAGE_BUFFER_SIZE 1024

using namespace pn;

Log* LOG = NEW_LOG();

UdpTransmitSocket* g_transmitSocket;
OpenNIFacade g_facade;
char oscMessageBuffer[OSC_MESSAGE_BUFFER_SIZE];

void onUserStateChanged(UserId userId, UserState userState) {
	osc::OutboundPacketStream packet(oscMessageBuffer, OSC_MESSAGE_BUFFER_SIZE);
	packet << osc::BeginBundleImmediate
	       << osc::BeginMessage(OSC_ADDRESS_USERSTATE)
	       << (osc::int32) userId
	       << (osc::int32) userState
	       << osc::EndMessage
	       << osc::EndBundle;

	g_transmitSocket->Send(packet.Data(), packet.Size());
}

void onJointPositionChanged(UserId userId, unsigned int jointId, float x, float y, float z) {
	osc::OutboundPacketStream packet(oscMessageBuffer, OSC_MESSAGE_BUFFER_SIZE);
	packet << osc::BeginBundleImmediate
		   << osc::BeginMessage(OSC_ADDRESS_JOINTDATA)
		   << (osc::int32) userId
		   << (osc::int32) jointId
		   << x
		   << y
		   << z
		   << osc::EndMessage
		   << osc::EndBundle;

	g_transmitSocket->Send(packet.Data(), packet.Size());
}

void tearDown() {
	printf("Ponyo OSC Server is going to shut down.\n");

	g_facade.shutdown(); // TODO try catch

	if(g_transmitSocket != NULL) {
		delete g_transmitSocket;
	}
	exit(0);
}

void onSignalReceived(int signalCode) {
	printf("Received signal %i\n", signalCode);
	tearDown();
}

void startServer() {
	printf("Ponyo OSC Server starting is starting up.\n");

	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)

	g_transmitSocket = new UdpTransmitSocket(IpEndpointName("127.0.0.1", 7000));

	StartOniConfig config("/ponyo/oni.oni", &onUserStateChanged, &onJointPositionChanged);
	g_facade.startRecording(config);

	printf("PnOSC running ... terminate by hitting ENTER\n");
	CommonUtils::waitHitEnter(false);
	tearDown();
}

int main() {
	try {
		startServer();
	} catch(Exception& e) {
		fprintf(stderr, "Ponyo Exception:");
		e.printBacktrace();
	} catch(std::exception& e) {
		fprintf(stderr, "std::exception: %s\n", e.what());
	} catch(...) {
		fprintf(stderr, "Some unkown error occured! DEBUG!"); // TODO how to process varargs?!
	}

	return 0;
}
