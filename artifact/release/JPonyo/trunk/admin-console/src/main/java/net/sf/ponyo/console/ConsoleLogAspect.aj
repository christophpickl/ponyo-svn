package net.sf.ponyo.console;

import net.sf.ponyo.jponyo.common.aop.LogAspectHelper;
import net.sf.ponyo.jponyo.common.log.LogLevel;

public aspect ConsoleLogAspect extends LogAspectHelper {
	
	pointcut withinConsole():
		within(net.sf.ponyo.console..*);

	pointcut anyConstructor():
		withinConsole() &&
		execution(*.new(..)) &&
		!within(ConsoleLogAspect); // avoid pointcutting yourself ;)

	pointcut infoMethod():
		withinConsole() &&
		(
			execution(public * *.ConsoleApp.*(..))
		);

	pointcut debugMethod():
		withinConsole() &&
		(
			execution(public * *.GLRenderer.setUser(..))
		);

//	pointcut anyPublicMethod(): call(public * net.sf.ponyo.jponyo.adminconsole..*(..));

	before(): infoMethod() {
		this.maybeLog(thisJoinPoint.getSignature(), thisJoinPoint.getArgs(), LogLevel.INFO);
	}
	
	before(): anyConstructor() {
		this.maybeLog(thisJoinPoint.getSignature(), thisJoinPoint.getArgs(), LogLevel.TRACE);
	}
}
