package net.sf.ponyo.jponyo.common.simplepersist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.ponyo.jponyo.common.async.DefaultAsyncFor;
import net.sf.ponyo.jponyo.common.io.IoUtil;
import net.sf.ponyo.jponyo.common.util.TypeUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

class XmlPersister extends AbstractPersister {
	
	private static final Log LOG = LogFactory.getLog(XmlPersister.class);
	
	public void init(Object instance, String persistId) {
		LOG.debug("init(..)");

		File targetFile = new File(persistId + ".xml");
		if(targetFile.exists() == false) {
			LOG.debug("nothing to persist, as XML file does not exist at: " + targetFile.getAbsolutePath());
			return;
		}
		
		XStream xstream = new XStream();
		LOG.debug("reading from: " + targetFile.getAbsolutePath());
		
		FileReader fileReader = null;
		Object loadedInstance = null;
		try {
			fileReader = new FileReader(targetFile);
			loadedInstance = xstream.fromXML(fileReader);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not read from file: " + targetFile.getAbsolutePath(), e);
		} finally {
			IoUtil.close(fileReader);
		}
		
//		System.out.println("loaded: " + loaded);
		
		for(final Field field : filterPersistAnnotatedFields(instance, PersistAsXml.class)) {
			
			Object loadedValue = TypeUtil.getField(loadedInstance, field);
			TypeUtil.setField(instance, field, loadedValue);
			LOG.trace("Initialized field: " + field + "; new value is [" + loadedValue + "]");
		}
	}

	public void persist(Object instance, String persistId) {
		LOG.debug("persist(..)");
		
//		XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
		XStream xstream = new XStream();
		/*
	new XppDriver() {
	    public HierarchicalStreamWriter createWriter(Writer out) {
		return new PrettyPrintWriter(out) {
		    boolean cdata = false;
		    public void startNode(String name, Class clazz){
			super.startNode(name, clazz);
			cdata = (name.equals("description") || name.equals("name"));
		    }
		    protected void writeText(QuickWriter writer, String text) {
			if(cdata) {
			    writer.write("<![CDATA[");
			    writer.write(text);
			    writer.write("]]>");
			} else {
			    writer.write(text);
			}
		    }
		};
	    }
	}
		 */
		this.omitNonXmlFields(xstream, instance);
		String xmlString = xstream.toXML(instance);
//		System.out.println(xmlString);
		
		File targetFile = new File(persistId + ".xml");
		File parentFile = targetFile.getParentFile();
		
		if(parentFile.exists() == false) {
			boolean createdParent = parentFile.mkdirs();
			if(createdParent == false) {
				LOG.warn("Could not create parent directory!");
			}
		}
		
		LOG.debug("saving to: " + targetFile.getAbsolutePath());
		FileWriter writer = null;
		try {
			writer = new FileWriter(targetFile);
			writer.write(xmlString);
		} catch (IOException e) {
			throw new RuntimeException("Could not write to file: " + targetFile.getAbsolutePath(), e);
		} finally {
			IoUtil.close(writer);
		}
	}
	
	private void omitNonXmlFields(XStream xstream, Object instance) {
		final Class<?> clazz = instance.getClass();
		Collection<Field> yesFields = this.filterPersistAnnotatedFields(instance, PersistAsXml.class);
		
		Map<Class<?>, Collection<Field>> allFieldsByClass = TypeUtil.getAllDeclaredFieldsByClass(clazz);
		for(Entry<Class<?>, Collection<Field>> entry : allFieldsByClass.entrySet()) {
			Class<?> declaringClass = entry.getKey();
			Collection<Field> allFields = entry.getValue();
			
			for (Field currentField : allFields) {
				if(yesFields.contains(currentField) == false) {
					LOG.trace("Omitting field: " + declaringClass.getSimpleName() + "." + currentField.getName());
					xstream.omitField(declaringClass, currentField.getName());
				} else {
					LOG.trace("Non-omitting field: " + declaringClass.getSimpleName() + "." + currentField.getName());
				}
			}
			
		}
	}

}
