package com.hardcoded.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A simple Logging utility class.
 * 
 * @author HardCoded
 */
public final class Log {
	private static final Log GLOBAL = new Log(Log.class);
	private static int LOG_LEVEL = 900;
	
	public static void setLogLevel(Level level) {
		LOG_LEVEL = level.level;
	}
	
	private final String customName;
	private Log(Class<?> clazz) {
		this(clazz.getName());
	}
	
	private Log(String name) {
		this.customName = name;
	}
	
	public static Log getLogger(Class<?> clazz) {
		return new Log(clazz);
	}
	
	public static Log getGlobal() {
		return GLOBAL;
	}
	
	public static Log getLogger() {
		StackTraceElement[] stack = Thread.getAllStackTraces().get(Thread.currentThread());
		if(stack == null) return GLOBAL;
		StackTraceElement last = stack[3];
		return new Log(last.getClassName());
	}
	
	public static Log getLogger(String name) {
		return new Log(name);
	}
	
	private void internal_log(Level level, String format, Object... args) {
		if(!shouldLog(level)) return;
		System.out.printf("%s\n", formatString(level, customName, format == null ? "":format, args));
	}
	
	public void log(Level level, int caller_offset, String format, Object... args) {
		internal_log(level, format, args);
	}
	
	public void log(Level level, String format, Object... args) {
		internal_log(level, format, args);
	}
	
	public void info() {
		internal_log(Level.INFO, "");
	}
	
	public void info(String format, Object... args) {
		internal_log(Level.INFO, format, args);
	}
	
	public void debug() {
		internal_log(Level.DEBUG, "");
	}
	
	public void debug(Object format, Object... args) {
		internal_log(Level.DEBUG, Objects.toString(format), args);
	}
	
	public void warn(String format, Object... args) {
		internal_log(Level.WARNING, format, args);
	}
	
	public void error(String format, Object... args) {
		internal_log(Level.ERROR, format, args);
	}
	
	public void throwing(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		internal_log(Level.ERROR, sw.toString());
	}
	
	private boolean shouldLog(Level level) {
		return level.level <= LOG_LEVEL;
	}
	
	private static final long time_offset = ZonedDateTime.now().getOffset().getTotalSeconds() * 1000L;
	private static String fastTime() {
		char[] time = { '0', '0', ':', '0', '0', ':', '0', '0', ' ', '0', '0', '0' };
		long millis = System.currentTimeMillis() + time_offset;
		long hours = (millis / 3600000L) % 24L;
		long minutes = (millis / 60000L) % 60L;
		long seconds = (millis / 1000L) % 60L;
		millis = millis % 1000L;
		time[0] += (hours / 10);
		time[1] += (hours % 10);
		time[3] += (minutes / 10);
		time[4] += (minutes % 10);
		time[6] += (seconds / 10);
		time[7] += (seconds % 10);
		time[9] += (millis / 100);
		time[11] += (millis % 10);
		time[10] += (millis / 10) % 10;
		return new String(time);
	}
	
	private String formatString(Level level, String customName, String format, Object... args) {
		String header = String.format("[%s] [%s] [%s]: ", fastTime(), level.name(), customName);
		String message = String.format(format, args);
		
		if(message.indexOf('\n') != -1) {
			return header + message.replaceAll("\n", "\n" + header);
		}
		
		return String.format("%s%s", header, message);
	}
	
	public enum Level {
		ALL(10000),
		
		DEBUG(1000),
		INFO(900),
		WARNING(200),
		ERROR(100);
		
		private final int level;
		private Level(int level) {
			this.level = level;
		}
	}
}
