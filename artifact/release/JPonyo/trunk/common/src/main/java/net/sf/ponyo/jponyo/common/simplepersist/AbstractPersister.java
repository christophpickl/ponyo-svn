package net.sf.ponyo.jponyo.common.simplepersist;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

import net.sf.ponyo.jponyo.common.util.TypeUtil;

abstract class AbstractPersister implements SimplePersister {

	protected final Collection<Field> filterPersistAnnotatedFields(final Object instance, final Class<? extends Annotation> searchingForAnnotation) {
		final Class<?> clazz = instance.getClass();
		return TypeUtil.filterAnnotatedFields(clazz, searchingForAnnotation);
	}

}
