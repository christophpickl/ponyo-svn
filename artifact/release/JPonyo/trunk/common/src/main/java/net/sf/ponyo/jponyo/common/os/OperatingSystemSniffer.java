package net.sf.ponyo.jponyo.common.os;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class OperatingSystemSniffer {

    private static final Log LOG = LogFactory.getLog(OperatingSystemSniffer.class);

    private static OperatingSystem os;

    private OperatingSystemSniffer() {
    	// no instantiation
    }
    
    public static OperatingSystem getOS() {
        if(OperatingSystemSniffer.os == null) {
        	OperatingSystemSniffer.lazyInitialize();
        }
    	return OperatingSystemSniffer.os;
    }
    
    private static void lazyInitialize() {
    	 final String osname = System.getProperty("os.name");

         if (osname.contains("Mac")) {
             OperatingSystemSniffer.os = OperatingSystem.MAC;
         } else if (osname.contains("Windows")) {
             OperatingSystemSniffer.os = OperatingSystem.WIN;
         } else if (osname.contains("Linux")) {
             OperatingSystemSniffer.os = OperatingSystem.LINUX;
         } else if (osname.contains("Unix") || osname.contains("UNIX")  || osname.contains("HP-UX")
                 || osname.contains("AIX") || osname.contains("BSD") || osname.contains("Irix")) {
             OperatingSystemSniffer.os = OperatingSystem.UNIX;
         } else if (osname.contains("SunOS") || osname.contains("Solaris")) {
             OperatingSystemSniffer.os = OperatingSystem.SOLARIS;
         } else if (osname.contains("OS/2")) {
             OperatingSystemSniffer.os = OperatingSystem.OS2;
         } else {
             OperatingSystemSniffer.LOG.warn("Could not detect user's operating system: '" + osname + "'!");
             OperatingSystemSniffer.os = OperatingSystem.UNKOWN;
         }
         
         assert(OperatingSystemSniffer.os != null);
         LOG.debug("Seems as user is running '" + OperatingSystemSniffer.os + "'.");
    }
    
    /**
     *
     * @param os
     * @return
     */
    public static boolean isOperatingSystem(final OperatingSystem isOs) {
        return OperatingSystemSniffer.getOS().equals(isOs);
    }

    public static boolean isWindows() {
        return OperatingSystemSniffer.getOS().equals(OperatingSystem.WIN);
    }

    public static boolean isMacOSX() {
        return OperatingSystemSniffer.getOS().equals(OperatingSystem.MAC);
    }

    public static boolean isLinux() {
        return OperatingSystemSniffer.getOS().equals(OperatingSystem.LINUX);
    }

    public static boolean isUnix() {
        return OperatingSystemSniffer.getOS().equals(OperatingSystem.UNIX);
    }

    public static boolean isOS2() {
        return OperatingSystemSniffer.getOS().equals(OperatingSystem.OS2);
    }

    public static boolean isUnkown() {
        return OperatingSystemSniffer.getOS().equals(OperatingSystem.UNKOWN);
    }

}
