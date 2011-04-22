package net.sf.ponyo.jponyo.common.aop;

public abstract class LogAspectHelper {

	protected final String buildLoggableMethodSignature(final String methodName, final String[] argNames, final Object[] argValues) {
		
		final StringBuilder logMessage = new StringBuilder();
		logMessage.append(methodName).append("(");
		assert (argValues.length == argNames.length);
		
		for (int i = 0; i < argNames.length; i++) {
			if(i != 0) logMessage.append(", ");
			final String argName = argNames[i];
			final Object argValue = argValues[i];
			
			final String argValueFormatted = String.valueOf(argValue).replace("\n", "\\n");
			logMessage.append(argName).append("=[").append(argValueFormatted).append("]");
		}
		logMessage.append(")");
		
		return logMessage.toString();
	}
	
}
