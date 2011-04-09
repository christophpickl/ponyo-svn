#include <ponyo/pnopenni/simplified/ContextX.hpp>

namespace pn {

Log* ContextX::LOG = NEW_LOG(__FILE__)

ContextX::ContextX() : userManager(new UserManager()) {
	LOG->debug("new ContextX()");

	this->mapMode.nFPS = 30;
	this->mapMode.nXRes = XN_VGA_X_RES;
	this->mapMode.nYRes = XN_VGA_Y_RES;
}

ContextX::~ContextX() {
	LOG->debug("~ContextX()");
	delete this->userManager;
}

void ContextX::init() throw(OpenNiException) {
	LOG->debug("init()");
	CHECK_RC(this->context.Init(), "context.Init()");
}

void ContextX::start() throw(OpenNiException) {
	LOG->debug("start()");

//	xn::NodeInfoList depthInfos;
//	CHECK_RC(this->context.EnumerateProductionTrees(XN_NODE_TYPE_DEPTH, NULL, depthInfos, NULL), "enumerate depth nodes");
//	xn::NodeInfo depthInfo = *depthInfos.Begin();
//	CHECK_RC(this->context.CreateProductionTree(depthInfo), "create depth node");
//	CHECK_RC(depthInfo.GetInstance(this->depthGenerator), "create depth instance");

	CHECK_RC(this->depthGenerator.Create(this->context), "create depth");
	CHECK_RC(this->depthGenerator.SetMapOutputMode(this->mapMode), "set depth mode"); // mandatory, otherwise will fail

	this->userManager->init(this->context);
	this->userManager->start();

//	this->depth.GetMetaData(metaData)
	CHECK_RC(this->depthGenerator.StartGenerating(), "depth start");
}

void ContextX::waitAndUpdate() {
	this->context.WaitAnyUpdateAll();
}

void ContextX::shutdown() {
	LOG->debug("shutdown()");

	if(this->userManager->isRunning()) {
		this->userManager->stop();
	}
	if(this->depthGenerator.IsGenerating()) {
		CHECK_RC(this->depthGenerator.StopGenerating(), "depth stop"); // TODO soft fail!
	}
	CHECK_RC(this->context.StopGeneratingAll(), "context stop all"); // TODO soft fail!

	this->context.Shutdown();
}

}
