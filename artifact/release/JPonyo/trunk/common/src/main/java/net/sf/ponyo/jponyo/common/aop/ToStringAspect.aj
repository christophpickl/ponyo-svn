package net.sf.ponyo.jponyo.common.aop;

import java.lang.reflect.Field;
import java.util.Collection;

import net.sf.ponyo.jponyo.common.util.TypeUtil;

public aspect ToStringAspect extends ToStringAspectHelper {

	// declare parents : @ManagedComponent * implements Lifecycle;
	
//	pointcut transactionalMethodExecution(Tx tx) :
//        execution(* *(..)) && @annotation(tx);
	
	pointcut foo(AopToString aopToString):
		execution(@AopToString String *..toString()) && @annotation(aopToString);
//		execution(public String *.Model.toString());

	String around(AopToString aopToString): foo(aopToString) {
		Object target = thisJoinPoint.getTarget();
		Class<?> targetClass = target.getClass();
		
		StringBuilder result = new StringBuilder();
		result.append(targetClass.getSimpleName());
		result.append("[");
		Collection<Field> toStringFields = TypeUtil.filterAnnotatedFields(targetClass, AopToStringField.class);
		boolean first = true;
		for (Field field : toStringFields) {
			if(first) first = false;
			else result.append(", ");
			
			result.append(field.getName());
			result.append("=");
			result.append(TypeUtil.getFieldValue(target, field));
		}
		result.append("]");
		
		// no proceed() as we override the toString result ;)
//		result.append(" => " + proceed(aopToString));
		
		return result.toString();
	}
}
