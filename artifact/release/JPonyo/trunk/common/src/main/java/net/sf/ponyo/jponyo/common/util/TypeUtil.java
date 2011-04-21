package net.sf.ponyo.jponyo.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @since 0.1
 */
public class TypeUtil {
	
	public static void setField(final Object instance, final Field field, Object value) {
		field.setAccessible(true);
		try {
			field.set(instance, value);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Wow!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Wow!", e);
		}
	}
	
	public static Object getField(final Object instance, final Field field) {
		field.setAccessible(true);
		try {
			return field.get(instance);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Wow!", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Wow!", e);
		}
	}
	
	public static Collection<Field> filterAnnotatedFields(Class<?> clazz, final Class<? extends Annotation> searchingForAnnotation) {
		final Collection<Field> result = new LinkedList<Field>();
		for(final Field field : clazz.getDeclaredFields()) {
			if(lookFieldForAnnotation(field, searchingForAnnotation) == true) {
				result.add(field);
			}
		}
		return result;
	}
	
	private static boolean lookFieldForAnnotation(final Field field, final Class<? extends Annotation> searchingForAnnotation) {
		for(final Annotation annotation : field.getAnnotations()) {
			if(annotation.annotationType() == searchingForAnnotation) {
				return true;
			}
		}
		return false;
	}

	public static Map<Class<?>, Collection<Field>> getAllDeclaredFieldsByClass(Class<?> clazz) {
		Map<Class<?>, Collection<Field>> result = new HashMap<Class<?>, Collection<Field>>();
		
		result.put(clazz, Arrays.asList(clazz.getDeclaredFields()));
		
		Class<?> superClazz = clazz.getSuperclass();
//		System.out.println("superClazz: " + superClazz);
		while(superClazz != null && superClazz != Object.class) {
			
//			System.out.println("processing superclass: " + superClazz.getName() + " => " + Arrays.toString(superClazz.getDeclaredFields()));
			result.put(superClazz, Arrays.asList(superClazz.getDeclaredFields()));
			superClazz = superClazz.getSuperclass();
		}
		
		return result;
	}
	
}
