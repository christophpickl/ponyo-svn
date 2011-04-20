package net.sf.ponyo.jponyo.common.binding;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aspectj.lang.reflect.MethodSignature;

public aspect BindingAspect {

	pointcut bindingAddFor(BindingProvider bp, String key, BindingListener listener):
		call(void *.addListenerFor(String, BindingListener)) &&
		target(bp) &&
		args(key, listener);
		//call(void *.addListenerFor(String, BindingListener));
	
	after(BindingProvider bp, String key, BindingListener listener) returning: bindingAddFor(bp, key, listener) {
		System.out.println("after(BindingProvider bp, String key, BindingListener listener) returning: bindingAddFor(bp, key, listener) { XXXXXXXXXXX");
		final Method setter = findSetterByKey(bp, key);
		final Object newValue = safeInvokeMethod(bp, lookupGetterForSetter(bp.getClass(), setter.getName()));
		this.dispatchValueChanged(bp, key, newValue);
	}
	
	private Method findSetterByKey(BindingProvider bp, String key) {
		for(Method m : bp.getClass().getMethods()) {
			for(Annotation an : m.getAnnotations()) {
				if(an.annotationType() == BindingSetter.class) {
					final BindingSetter bs = BindingSetter.class.cast(an);
					if(bs.Key().equals(key)) {
						return m;
					}
				}
			}
		}
		throw new RuntimeException("Could not find setter on type [" + bp.getClass().getName() + "] for key [" + key + "]!");
	}
	
	pointcut bindingSetterMethod() : call(@BindingSetter * *.*(**));
	

	Object around() : bindingSetterMethod() {
		final MethodSignature signature = (MethodSignature) thisJoinPoint.getSignature();
		
		final Object _target = thisJoinPoint.getTarget();
		if(BindingProvider.class.isAssignableFrom(_target.getClass()) == false) {
			throw new RuntimeException("Anything with @BindingSetter must implement BindingProvider!");
		}
		final BindingProvider bindingProvider = BindingProvider.class.cast(_target);
		
		final Method getter = this.lookupGetterForSetter(bindingProvider.getClass(), signature.getName());
		final Object oldValue = safeInvokeMethod(bindingProvider, getter);
		
		Object proceedResult = proceed();
		// TODO use thisJoinPoint.getArgs instead!
		final Object newValue = safeInvokeMethod(bindingProvider, getter);
		
		if(oldValue == null && newValue == null) {
			return proceedResult;
		}
		if(oldValue != null && newValue != null && oldValue.equals(newValue)) {
			return proceedResult;
		}
		this.dispatchValueChanged(bindingProvider, getKey(signature.getMethod()), newValue);
		return proceedResult;
	}
	
	private void dispatchValueChanged(BindingProvider bindingProvider, final String key, Object newValue) {
		for(BindingListener listener : bindingProvider.getBindingListenersFor(key)) {
			listener.onValueChanged(newValue);
		}
	}
	
	private String getKey(final Method m) {
		for(final Annotation a : m.getAnnotations()) {
			if(a.annotationType() == BindingSetter.class) {
				final BindingSetter bs = BindingSetter.class.cast(a);
				return bs.Key();
			}
		}
		throw new RuntimeException("Could not find @BindingSetter for method: " + m.getName());
	}
	
	private Object safeInvokeMethod(final Object target, final Method method) {
		try {
			return method.invoke(target);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Could not invoke method!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Could not invoke method!", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Could not invoke method!", e);
		}
	}
	
	private Method lookupGetterForSetter(final Class<?> targetClass, final String setterName) {
		final String getterName = "g" + setterName.substring(1);
		for (final Method m : targetClass.getMethods()) {
			if(m.getName().equals(getterName)) {
				return m;
			}
		}
		throw new RuntimeException("Could not find method [" + getterName + "] for type [" + targetClass.getName() + "]!");
	}
	
}
