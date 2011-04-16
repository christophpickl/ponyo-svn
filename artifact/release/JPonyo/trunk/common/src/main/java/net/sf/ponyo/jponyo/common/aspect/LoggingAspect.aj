package net.sf.ponyo.jponyo.common.aspect;


/**
 * @since 0.1
 */
public aspect LoggingAspect {
	/*
//	pointcut withinScope(Object someone): within(net.sf.josceleton.prototype.midi..*) && target(someone);
//	pointcut callScope(Object someone): call(public net.sf.josceleton.prototype.midi.** *(..))
//		&& target(someone);
	
//    declare warning : withinScope() && 
//    (call(* *(..) throws AppException+)
//     || call(new(..) throws AppException+)) :
//    "fyi, another call to something that can throw IOException";
// * Find catch clauses handling AppException
//declare warning : withinScope() && handler(AppException+):
//    "fyi, code that handles AppException";

		
		
//	pointcut logInfoPointcut(Object someone):
//		execution(public * net.sf.josceleton.prototype.midi.logic.*.*(..))
////		call(public * net.sf.josceleton.prototype.midi.*.*.*(..))
////		call(* net.sf.josceleton.prototype.midi.logic.MappingsParser.* (**))
//		&& target(someone);
	
//	pointcut logInfoPointcut2(Object someone):
//		call(public * net.sf.josceleton.prototype.midi.Model.*(..))
//		&& target(someone);
	
	
//	public pointcut withinJosceleton() :
//        within(net.sf.josceleton.prototype.midi.logic..*);
	
	pointcut anyPublicMethodCall() : call(public * net.sf.josceleton..*.*(..));
//	pointcut isValueObject() : within(net.sf.josceleton.prototype.midi.logic.bindable.BindingSetter.*);
	pointcut isValueObject() :
		call(* net.sf.josceleton.prototype.midi.logic.bindable.BindingSetter.*(..)) ||
		call(* net.sf.josceleton.prototype.midi.logic.preference.TypeUtil.*(..)) ||
		call(* net.sf.josceleton.prototype.midi.logic.preference.TypeConverters.*(..)) ||
		call(* net.sf.josceleton.prototype.midi.logic.preference.TypeConverters.*.*(..)) ||
		call(* net.sf.josceleton.prototype.midi.util.LogUtil.*(..));
	
	before(): anyPublicMethodCall() && !isValueObject() {
		final MethodSignature signature = MethodSignature.class.cast(thisJoinPoint.getSignature());
		LogFactory.getLog(signature.getDeclaringType()).info(
			buildLoggableMethodSignature(signature.getName(), signature.getParameterNames(), thisJoinPoint.getArgs()));
	}
	
	private String buildLoggableMethodSignature(final String methodName, final String[] argNames, final Object[] argValues) {
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
	*/
}
