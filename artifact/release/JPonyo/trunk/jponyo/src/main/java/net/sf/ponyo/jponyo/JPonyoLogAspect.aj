package net.sf.ponyo.jponyo;

import net.sf.ponyo.jponyo.common.aop.LogAspectHelper;
import net.sf.ponyo.jponyo.common.log.LogLevel;

public aspect JPonyoLogAspect extends LogAspectHelper {
	
	pointcut withinJPonyo():
		within(net.sf.ponyo.jponyo..*);

	pointcut anyConstructor():
		withinJPonyo() &&
		execution(*.new(..)) &&
		!within(net.sf.ponyo.jponyo.user.UserState) &&
		!within(net.sf.ponyo.jponyo.entity.Joint) &&
		!within(JPonyoLogAspect); // avoid pointcutting yourself ;)

	pointcut infoMethod():
		withinJPonyo() &&
		(
				execution(* *.Context.start(..)) ||
				execution(* *.Context.shutdown(..))
		);

	pointcut debugMethod():
		withinJPonyo() &&
		(
			execution(public * *.GlobalSpace.*(..)) ||
			execution(* *.AbstractPose.startDetecting(..)) ||
			execution(* *.AbstractPose.stopDetecting(..)) ||
			
			execution(* *.Context.processUserStateChange(..)) ||
			execution(* *.Context.onUserMessage(..)) ||
			execution(* *.Context.initializeContinuousMotionStream(..))
		);

	
	before(): anyConstructor() {
		this.maybeLog(thisJoinPoint.getSignature(), thisJoinPoint.getArgs(), LogLevel.TRACE);
	}

	before(): infoMethod() {
		this.maybeLog(thisJoinPoint.getSignature(), thisJoinPoint.getArgs(), LogLevel.INFO);
	}

	before(): debugMethod() {
		this.maybeLog(thisJoinPoint.getSignature(), thisJoinPoint.getArgs(), LogLevel.DEBUG);
	}
}
