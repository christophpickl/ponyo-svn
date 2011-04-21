package net.sf.ponyo.jponyo.samples;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.sf.ponyo.jponyo.common.simplepersist.PersistAsPreference;
import net.sf.ponyo.jponyo.common.simplepersist.PersistAsXml;
import net.sf.ponyo.jponyo.common.simplepersist.SimplePersister;
import net.sf.ponyo.jponyo.common.simplepersist.SimplePersisterImpl;

public class PersistenceSample {

	@SuppressWarnings("unused")
	public static class Foo {
		
		private transient Object notPersisted;
		
		@PersistAsPreference
		public String lbl = ""; // must not be null!
		
		@PersistAsXml
		public int age;
		
		public Foo() {
			// empty
		}
		public Foo(String lbl, int age) { 
			this.lbl = lbl;
			this.age = age;
		}
		
		@Override
		public String toString() {
			return "Foo[lbl=[" + this.lbl + "], age=" + this.age + "]";
		}
	}
	
	public static void main(String[] args) {
		try {
			Preferences.userRoot().clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
		final SimplePersister p = new SimplePersisterImpl(new File("xml_data"));
//		Foo f = new Foo("lbl", 42);
		Foo f = new Foo();
//		f.age = 21;
		
//		p.init(f, "id1");
		p.persist(f, "id1");
		
		System.out.println("Main: " + f);
	}
	
}
