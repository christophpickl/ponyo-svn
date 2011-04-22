/**
 * 
 */
package net.sf.ponyo.jponyo.common.log;

import org.apache.commons.logging.Log;

public enum LogLevel {
	
	INFO {
		@Override public void execute(Log log, String message) { log.info(message); }
		@Override public boolean isEnabled(Log log) { return log.isInfoEnabled(); }
	},
	DEBUG {
		@Override public void execute(Log log, String message) { log.debug(message); }
		@Override public boolean isEnabled(Log log) { return log.isDebugEnabled(); }
	},
	TRACE {
		@Override public void execute(Log log, String message) { log.trace(message); }
		@Override public boolean isEnabled(Log log) { return log.isTraceEnabled(); }
	}
	;
	
	public abstract boolean isEnabled(Log log);
	public abstract void execute(Log log, String message);
}