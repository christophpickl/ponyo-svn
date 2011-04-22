package net.sf.ponyo.jponyo.common.simplepersist;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.common.simplepersist.TypeConverters.TypeConverter;
import net.sf.ponyo.jponyo.common.util.TypeUtil;

class PreferencesPersister extends AbstractPersister {

	private static final Log LOG = LogFactory.getLog(PreferencesPersister.class);
	
	private final TypeConverters converters = new TypeConverters();
	
	public void init(Object instance, String persistId) {
		LOG.debug("init(..)");
		final Class<?> clazz = instance.getClass();
		final PreferencesManager preferencesManager = new PreferencesManager(clazz);
		
		for(final Field field : filterPersistAnnotatedFields(instance, PersistAsPreference.class)) {
			final String fieldPrefKey = buildPrefKey(clazz, persistId, field);
			final TypeConverter converter = this.converters.getConverterFor(field);
			final Object value = converter.load(preferencesManager, fieldPrefKey);
			TypeUtil.setField(instance, field, value);
			LOG.trace("Initialized field: " + field + "; new value is: " + value);
		}
	}

	public void persist(Object instance, String persistId) {
		LOG.debug("persist(..)");
		final Class<?> clazz = instance.getClass();
		final PreferencesManager preferencesManager = new PreferencesManager(clazz);
		
		for(final Field field : filterPersistAnnotatedFields(instance, PersistAsPreference.class)) {
			final TypeConverter converter = this.converters.getConverterFor(field);
			final String fieldPrefKey = buildPrefKey(clazz, persistId, field);
			Object value = TypeUtil.getFieldValue(instance, field);
			LOG.trace("Persisting field: " + field + ", with value: [" + value + "]");
			converter.store(preferencesManager, fieldPrefKey, value);
		}
	}
	
	private String buildPrefKey(final Class<?> clazz, final String prefId, final Field field) {
		return clazz.getSimpleName() + "-" + prefId + "." + field.getName();
	}

}
