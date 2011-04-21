package net.sf.ponyo.jponyo.common.simplepersist;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;

import net.sf.ponyo.jponyo.common.simplepersist.TypeConverters.TypeConverter;
import net.sf.ponyo.jponyo.common.util.TypeUtil;

/**
 * @since 0.1
 */
public class SimplePersisterImpl implements SimplePersister {
	
	private final PreferencesPersister prefPersister = new PreferencesPersister();
	private final XmlPersister xmlPersister = new XmlPersister();
	
	private final File basePath;
	
	public SimplePersisterImpl(File basePath) {
		this.basePath = basePath;
	}

	public void init(final Object instance, final String persistId) {
		this.prefPersister.init(instance, persistId);
		this.xmlPersister.init(instance, new File(this.basePath, persistId).getAbsolutePath());
	}
	
	public void persist(final Object instance, final String persistId) {
		this.prefPersister.persist(instance, persistId);
		this.xmlPersister.persist(instance, new File(this.basePath, persistId).getAbsolutePath());
	}
	
}
