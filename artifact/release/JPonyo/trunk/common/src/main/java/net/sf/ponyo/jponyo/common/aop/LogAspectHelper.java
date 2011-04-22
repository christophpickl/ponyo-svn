package net.sf.ponyo.jponyo.common.aop;

import net.sf.ponyo.jponyo.common.log.LogLevel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.ConstructorSignature;

public class LogAspectHelper {
	
	// TODO add possibility to set log level
	protected final void maybeLog(final Signature signature, Object[] args, LogLevel level) {
		Class<?> declaringType = signature.getDeclaringType();
		
		if(declaringType.isAnonymousClass() == true && signature instanceof ConstructorSignature) {
			// avoid logging constructor invocation of anonymous classes
			return;
		}
		
		Log log = LogFactory.getLog(declaringType);
		if(level.isEnabled(log)) {
			String logMessage = buildLoggableMethodSignature((CodeSignature) signature, args);
			level.execute(log, logMessage);
		}
	}
	
	private String buildLoggableMethodSignature(final CodeSignature signature, final Object[] argValues) {
		final String methodName = signature.getName();
		final String[] argNames = signature.getParameterNames();
		
		final StringBuilder logMessage = new StringBuilder();
		final String constructorSafeMethodName;
		if("<init>".equals(methodName)) {
			constructorSafeMethodName = "new " + signature.getDeclaringType().getSimpleName();
		} else {
			constructorSafeMethodName = methodName;
		}
		logMessage.append(constructorSafeMethodName).append("(");
		
		assert (argValues.length == argNames.length);
		for (int i = 0; i < argNames.length; i++) {
			if(i != 0) logMessage.append(", ");
			final String argName = argNames[i];
			final Object argValue = argValues[i];

			// MINOR does not work; null check fails ... :-(
//			final String argValueFormatted;
//			if(argValue == null) {
//				argValueFormatted = "null";
//			} else {
//				argValueFormatted = String.valueOf(argValue).replace("\n", "\\n");
//			}
			logMessage.append(argName).append("=[").append(argValue).append("]");
		}
		logMessage.append(")");
		
		return logMessage.toString();
	}
	
}
