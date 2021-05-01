package com.hardcoded.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A simple Logging utility class.
 * 
 * @author HardCoded
 */
public final class Log {
	private static final DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");  
	
	private static final Log GLOBAL = new Log(Log.class);
	private static boolean READ_TREAD_STACK = true;
	private static boolean SHOW_LINE_INDEX = false;
	private static int LOG_LEVEL = 900;
	
	public static void setLogLevel(Level level) {
		LOG_LEVEL = level.level;
	}
	
	public static void readThreadStack(boolean enable) {
		READ_TREAD_STACK = enable;
	}
	
	public static void showLineIndex(boolean enable) {
		SHOW_LINE_INDEX = enable;
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
	
	private void internal_log(Level level, StackTraceElement stack, String format, Object... args) {
		if(!shouldLog(level)) return;
		if(format == null) format = "null";
		
		String msg;
		if(stack == null) {
			msg = formatString(level, customName, -1, format, args);
		} else {
			msg = formatString(level, stack.getClassName(), stack.getLineNumber(), format, args);
		}
		
		System.out.printf("%s\n", msg);
	}
	
	public void log(Level level, int caller_offset, String format, Object... args) {
		internal_log(level, getStack(caller_offset), format, args);
	}
	
	public void log(Level level, String format, Object... args) {
		internal_log(level, getStack(0), format, args);
	}
	
	public void info() {
		internal_log(Level.INFO, getStack(0), "");
	}
	
	public void info(String format, Object... args) {
		internal_log(Level.INFO, getStack(0), format, args);
	}
	
	public void debug() {
		internal_log(Level.DEBUG, getStack(0), "");
	}
	
	public void debug(Object format, Object... args) {
		internal_log(Level.DEBUG, getStack(0), Objects.toString(format), args);
	}
	
	public void warn(String format, Object... args) {
		internal_log(Level.WARNING, getStack(0), format, args);
	}
	
	public void error(String format, Object... args) {
		internal_log(Level.ERROR, getStack(0), format, args);
	}
	
	public void throwing(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		internal_log(Level.ERROR, getStack(0), sw.toString());
	}
	
	private boolean shouldLog(Level level) {
		return level.level <= LOG_LEVEL;
	}
	
	private String formatHeader(Level level, String customName, int line_index) {
		if(SHOW_LINE_INDEX && line_index != -1) {
			return String.format("[%s] [%s] [%s:%d]: ", date_formatter.format(LocalDateTime.now()), level.name(), customName, line_index);
		}
		
		return String.format("[%s] [%s] [%s]: ", date_formatter.format(LocalDateTime.now()), level.name(), customName);
	}
	
	private String formatString(Level level, String customName, int line_index, String format, Object... args) {
		String header = formatHeader(level, customName, line_index);
		String message = String.format(format, args);
		
		if(message.indexOf('\n') != -1) {
			return header + message.replaceAll("\n", "\n" + header);
		}
		
		return String.format("%s%s", header, message);
	}
	
	private StackTraceElement getStack(int offset) {
		if(!READ_TREAD_STACK) return null;
		
		StackTraceElement[] stack = Thread.getAllStackTraces().get(Thread.currentThread());
		if(stack == null) return null;
		int idx = offset + 4;
		if(idx < 0 || idx >= stack.length) return null;
		
		return stack[idx];
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
