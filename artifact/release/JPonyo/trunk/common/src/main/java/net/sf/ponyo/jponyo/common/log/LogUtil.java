package net.sf.ponyo.jponyo.common.log;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogUtil {
	
	private static final String DEFAULT_PATTERN = "%d [%t] [%-5p] %-30c{1} - %m%n";
	
	public static void ensureDefaultLogger() {
		Logger log4jLog = Logger.getRootLogger();
		
		if(log4jLog.getAllAppenders().hasMoreElements() == false) {
			PatternLayout layout = new PatternLayout(DEFAULT_PATTERN);
			Appender newAppender = new ConsoleAppender(layout);
			log4jLog.addAppender(newAppender);
		}
	}
	
}
