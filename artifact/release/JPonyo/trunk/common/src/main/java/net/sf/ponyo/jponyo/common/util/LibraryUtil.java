package net.sf.ponyo.jponyo.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.jpotpourri.tools.PtUserSniffer;

public class LibraryUtil {
	
	private static final Log LOG = LogFactory.getLog(LibraryUtil.class);
	
	public static final Collection<String> JOGL_LIBS_MANDATORY = Collections.unmodifiableList(Arrays.asList(
		"jogl",
		"jogl_awt"
	));

	public static final Collection<String> JOGL_LIBS_OPTIONAL = Collections.unmodifiableList(Arrays.asList(
//		"jogl_cg", // MINOR although jogl_cg.jnilib is existing, java cant find it, but makes no difference anyway as it works somehow
		"gluegen-rt"
	));

	private static final String OS_SPECIFIC_LIB_PREFIX;
	private static final String OS_SPECIFIC_LIB_SUFFIX;
	static {
		if(PtUserSniffer.isMacOSX()) {
			OS_SPECIFIC_LIB_PREFIX = "lib";
			OS_SPECIFIC_LIB_SUFFIX = ".jnilib";
		} else if(PtUserSniffer.isWindows()) {
			OS_SPECIFIC_LIB_PREFIX = "";
			OS_SPECIFIC_LIB_SUFFIX = ".dll";
		} else if(PtUserSniffer.isLinux()) {
			OS_SPECIFIC_LIB_PREFIX = "";
			OS_SPECIFIC_LIB_SUFFIX = ".so";
		} else {
			OS_SPECIFIC_LIB_PREFIX = "";
			OS_SPECIFIC_LIB_SUFFIX = "";
		}
	}
	
	public static void main(String[] args) {
		Collection<String> notFoundLibs = checkLibrariesExisting(LibraryUtil.JOGL_LIBS_MANDATORY);
		if(notFoundLibs.isEmpty() == true) {
			System.out.println("Everything okay!");
		} else {
			System.out.println("ERROR: Not found libs: " + Arrays.toString(notFoundLibs.toArray()));
		}
		System.out.println("Not found optional jogl libs: " + Arrays.toString(checkLibrariesExisting(LibraryUtil.JOGL_LIBS_OPTIONAL).toArray()));
		
	}
	
	public static Collection<String> checkLibrariesExisting(Collection<String> libraryNames) {
		Collection<String> notFoundLibraryNames = new LinkedList<String>();
		
		for (String libName : libraryNames) {
			boolean loaded = LibraryUtil.loadLibrary(libName);
			if(loaded == false) {
				notFoundLibraryNames.add(OS_SPECIFIC_LIB_PREFIX + libName + OS_SPECIFIC_LIB_SUFFIX);
			}
		}
		return notFoundLibraryNames;
	}
	
	/**
	 * @return true if library was successfully loaded
	 */
	public static boolean loadLibrary(String libraryName) {
		try {
			System.loadLibrary(libraryName);
			LOG.debug("Checking library: " + libraryName + " ... found");
			return true;
		} catch(UnsatisfiedLinkError e) {
			LOG.warn("Checking library: " + libraryName + " ... NOT found!!!");
			return false;
		}
	}
}
