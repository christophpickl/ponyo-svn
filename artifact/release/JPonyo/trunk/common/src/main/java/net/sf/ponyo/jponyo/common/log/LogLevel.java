/**
 * 
 */
package net.sf.ponyo.jponyo.common.log;

import org.apache.commons.logging.Log;

public enum LogLevel {
	TRACE {
		@Override public void execute(Log log, String message) { log.trace(message); }
		@Override public boolean isEnabled(Log log) { return log.isTraceEnabled(); }
	},
	DEBUG {
		@Override public void execute(Log log, String message) { log.debug(message); }
		@Override public boolean isEnabled(Log log) { return log.isDebugEnabled(); }
	},
	INFO {
		@Override public void execute(Log log, String message) { log.info(message); }
		@Override public boolean isEnabled(Log log) { return log.isInfoEnabled(); }
	}
	;
	
	public abstract boolean isEnabled(Log log);
	public abstract void execute(Log log, String message);
}