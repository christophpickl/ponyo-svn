#OpenNI errors you might encounter.

# OpenNI error codes #

# "Bad Parameter Set" #
Happens: while starting generator
Solution: Check if you have set MapOutputMode.
// forum entry already created: https://groups.google.com/group/openni-dev/browse_thread/thread/67e3e47792f00964?fwc=2

## "Failed to set USB interface" / XN\_STATUS\_USB\_SET\_INTERFACE\_FAILED (131188) ##
Happens: when programatically/non-XML initializing OpenNI, single kinect, and creating _second_ generator node
Code: context.CreateProductionTree(depthOrImageNodeItDoesntMatterJustTheSecondFails)
Solution: ???
see my openni forum thread: "Failed to set USB interface!" error message => http://groups.google.com/group/openni-dev/browse_frm/thread/742ac6dc41abf613
// this error is OS specific (as it is also declared in XnOS instead of XnStatus)

## "The node is locked for changes!" / XN\_STATUS\_NODE\_IS\_LOCKED ##
Happens: when stopping generators and ... ?
Code: context.StopGeneratingAll()
Solutions:
  * Make sure no background thread runs in background and waits for updates.
  * Additionally unregister any callbacks (is nice to do anyway).

## "Can't create any node of the requested type!" / XN\_STATUS\_NO\_NODE\_PRESENT ##
Happens: trying to enumerate connected devices
Code: context.EnumerateProductionTrees(XN\_NODE\_TYPE\_DEVICE, NULL, imageNodes, NULL)
Solution: check if the kinect's power cord is plugged in ;)

## Just "Error!" ##
Happens: ... hm ....
eg: when trying to get joint data, whereas user is not being tracked => skeletonCapability.IsTracking(userId)
eg: when invoking skeletonCapability.GetSkeletonJointPosition(..); but user is being tracked, then... ???

# Wrong behaviour #

## UserGenerator callbacks are not invoked but they should be ##
Make sure you are invoking depthGenerator.GetMetaData(depthMetaData), even when you dont need the data itself,
it is necessary to call it after WaitXXX() so the user generator will dispatch events.