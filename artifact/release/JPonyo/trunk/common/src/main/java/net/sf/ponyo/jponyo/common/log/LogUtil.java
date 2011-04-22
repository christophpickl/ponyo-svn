package net.sf.ponyo.jponyo.common.log;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogUtil {
	
//	private static final String DEFAULT_PATTERN = "%d [%t] [%-5p] %-30c{1} - %m%n";
	private static final String DEFAULT_PATTERN = "%d [%-16t] [%-5p] %-50c - %m%n";
	
	public static void ensureDefaultLogger() {
		Logger rootLogger = Logger.getRootLogger();
		
		if(rootLogger.getAllAppenders().hasMoreElements() == false) {
			System.out.println("[INFO ] setting up default logger ...");
			rootLogger.setLevel(Level.TRACE);
			
			PatternLayout layout = new PatternLayout(DEFAULT_PATTERN);
			Appender newAppender = new ConsoleAppender(layout);

			rootLogger.addAppender(newAppender);
			
			/* TODO set warn for all, and only trace for this project!
			log4j.rootLogger=warn, stdout
			
			log4j.category.net.sf.ponyo.jponyo.adminconsole=trace, stdout
			log4j.additivity.net.sf.ponyo.jponyo.adminconsole=false
			
			log4j.appender.stdout=org.apache.log4j.ConsoleAppender
			log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
			log4j.appender.stdout.layout.ConversionPattern=%d [%-5p] [%-16t] %-30c - %m%n
			 */
			
//			LevelMatchFilter levelFilter = new LevelMatchFilter();
//			levelFilter.setLevelToMatch(Level.DEBUG.toString());
//			newAppender.addFilter(levelFilter);
			

//			Logger projectLogger = Logger.getLogger("net.sf.ponyo.jponyo.adminconsole");
//			projectLogger.setAdditivity(false);
//			projectLogger.setLevel(Level.ALL);
//			projectLogger.addAppender(newAppender);
		}
	}
	
}
