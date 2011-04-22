package net.sf.ponyo.jponyo.adminconsole;

import net.sf.ponyo.jponyo.common.aop.LogAspectHelper;

import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.reflect.MethodSignature;

public aspect ConsoleLogAspect extends LogAspectHelper {
	
	pointcut anyPublicMethod(Object target): call(public * net.sf.ponyo.jponyo.adminconsole..*(..)) && target(target);
//	pointcut anyPublicMethod(): call(public * net.sf.ponyo.jponyo.adminconsole..*(..));
	
	// within(net.sf.ponyo.jponyo.adminconsole..*)
	
	before(Object target): anyPublicMethod(target) {
		final MethodSignature signature = MethodSignature.class.cast(thisJoinPoint.getSignature());
		
//		Class<?> targetClass = thisJoinPoint.getTarget().getClass();
		Class<?> targetClass = target.getClass();
//		System.out.println("ASPECT: " + targetClass.getName() + "#" + signature.getName());
		
		String logMessage = buildLoggableMethodSignature(signature.getName(), signature.getParameterNames(), thisJoinPoint.getArgs());
		LogFactory.getLog(signature.getDeclaringType()).info(logMessage);
	}

}
