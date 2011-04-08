#pragma once
#ifndef MYNI_INC_H_
#define MYNI_INC_H_

#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <XnCppWrapper.h>


#define CHECK_RCX(returnCode, actionDescription) \
	printf(actionDescription/*no endl*/); \
	CHECK_RC(returnCode, actionDescription)

#define CHECK_RC(returnCode, actionDescription) \
	printf(" ... returned %i\n", returnCode); \
	if(returnCode != XN_STATUS_OK) { \
		fprintf(stderr, "actionDescription: %s\nxnGetStatusString(returnCode): %s\n", actionDescription, xnGetStatusString(returnCode)); \
		exit(1); \
	}

#define ENUMERATE_NODES(ctx, nodeTypeLabel, nodeTypeId, nodes, nodeInfoList) { \
	printf("\nENUMERATE_NODES(%s)\n", nodeTypeLabel); \
	printf("EnumerateProductionTrees(..)"); CHECK_RC(ctx.EnumerateProductionTrees(nodeTypeId, NULL, nodeInfoList, NULL), "EnumerateProductionTrees(..)"); \
	for (xn::NodeInfoList::Iterator it = nodeInfoList.Begin(); it != nodeInfoList.End(); ++it) { nodes.push_back(*it); } \
	printf("nodes.size()=%i\n", (int) nodes.size()); \
}


#endif // MYNI_INC_H_
