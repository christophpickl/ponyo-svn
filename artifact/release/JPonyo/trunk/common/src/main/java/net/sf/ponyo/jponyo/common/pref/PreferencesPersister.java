package net.sf.ponyo.jponyo.common.pref;

import java.lang.reflect.Field;
import java.util.Collection;

import net.sf.ponyo.jponyo.common.pref.TypeConverters.TypeConverter;
import net.sf.ponyo.jponyo.common.util.TypeUtil;

/**
 * @since 0.1
 */
public class PreferencesPersister {
	
	@SuppressWarnings("unused")
	private static class Foo {
		private Object notPersisted;
		@PersistAsPreference private String lbl;
		@PersistAsPreference private int age;
		public Foo() { /* empty */ }
		public Foo(String lbl, int age) { this.lbl = lbl; this.age = age; }
		public String getLbl() { return this.lbl; }
		public void setLbl(String lbl) { this.lbl = lbl; }
		@Override public String toString() { return "Foo[lbl=[" + this.lbl + "], age=" + this.age + "]"; }
	}
	
	public static void main(String[] args) {
		final PreferencesPersister p = new PreferencesPersister();
		
		Foo f = new Foo(); p.init(f, "id1"); System.out.println(f);
//		p.persist(new Foo("asdf", 42), "id1");
	}
	
	
	private final TypeConverters converters = new TypeConverters();
	
	public void init(final Object instance, final String prefId) {
		final Class<?> clazz = instance.getClass();
		final PreferencesManager internal = new PreferencesManager(clazz);
		
		for(final Field field : filterPersistAnnotatedFields(instance)) {
			final String fieldPrefKey = buildPrefKey(clazz, prefId, field);
			final TypeConverter converter = this.converters.getConverterFor(field);
			final Object value = converter.load(internal, fieldPrefKey);
			TypeUtil.setField(instance, field, value);
		}
	}
	
	public void persist(final Object instance, final String prefId) {
		final Class<?> clazz = instance.getClass();
		final PreferencesManager internal = new PreferencesManager(clazz);
		
		for(final Field field : filterPersistAnnotatedFields(instance)) {
			final TypeConverter converter = this.converters.getConverterFor(field);
			final String fieldPrefKey = buildPrefKey(clazz, prefId, field);
			final Object value = TypeUtil.getField(instance, field);
			converter.store(internal, fieldPrefKey, value);
		}
	}
	
	private Collection<Field> filterPersistAnnotatedFields(final Object instance) {
		final Class<?> clazz = instance.getClass();
		return TypeUtil.filterAnnotatedFields(clazz, PersistAsPreference.class);
	}
	
	private String buildPrefKey(final Class<?> clazz, final String prefId, final Field field) {
		return clazz.getSimpleName() + "-" + prefId + "." + field.getName();
	}
	
}
