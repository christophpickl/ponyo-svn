package net.sf.ponyo.jponyo.common.pref;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 0.1
 */
class TypeConverters {

	private Map<Class<?>, TypeConverter> converters = new HashMap<Class<?>, TypeConverter>();
	{
		this.converters.put(String.class, new TypeConverter() {
			public void store(PreferencesManager prefPersister, String fieldPrefKey, Object value) {
				prefPersister.setString(fieldPrefKey, String.class.cast(value));
			}
			public Object load(PreferencesManager prefPersister, String fieldPrefKey) {
				return prefPersister.getString(fieldPrefKey, null /* default is always null!!! */);
			}
		});

		this.converters.put(int.class, new TypeConverter() {
			public void store(PreferencesManager prefPersister, String fieldPrefKey, Object value) {
				prefPersister.setInteger(fieldPrefKey, Integer.class.cast(value));
			}
			public Object load(PreferencesManager prefPersister, String fieldPrefKey) {
				return prefPersister.getInteger(fieldPrefKey, -1);
			}
		});
	}

	public TypeConverter getConverterFor(final Field field) {
		final Class<?> fieldType = field.getType();
		final TypeConverter converter = this.converters.get(fieldType);
		if(converter == null) {
			throw new RuntimeException("Unhandled type [" + fieldType.getName() + "] for field named [" + field.getName() + "]!");
		}
		return converter;
	}


	static interface TypeConverter {
		void store(PreferencesManager prefPersister, String fieldPrefKey, Object value);
		Object load(PreferencesManager prefPersister, String fieldPrefKey);
	}
	
}
