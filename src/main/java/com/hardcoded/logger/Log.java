package com.hardcoded.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A simple Logging utility class.
 * 
 * @author HardCoded
 * @since 0.2.0
 */
public final class Log {
	private static final DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");  
	
	private static class MSG {
		
		@SuppressWarnings("unused")
		public MSG(Level level, String logger_name, int line_index, String format, Object[] args) {
			this.level = level;
			this.logger_name = logger_name;
			this.message = String.format(format, args);
			this.line_index = line_index;
			this.time = LocalDateTime.now();
		}
		
		private final Level level;
		private final LocalDateTime time;
		private final String logger_name;
		private final String message;
		private final int line_index;
		
		public String toMessage() {
			String header;
			
			if(SHOW_LINE_INDEX) {
				header = String.format("[%s] [%s] [%s:%d]: ", date_formatter.format(time), level.name(), logger_name, line_index);
			} else {
				header = String.format("[%s] [%s] [%s]: ", date_formatter.format(time), level.name(), logger_name);
			}
			
			if(message.indexOf('\n') != -1) {
				return header + message.replaceAll("\n", "\n" + header);
			}
			
			return String.format("%s%s", header, message);
		}
	}
	
	private static final ConcurrentLinkedDeque<MSG> messages = new ConcurrentLinkedDeque<>();
	private static final Log GLOBAL = new Log(Log.class);
	private static boolean SHOW_LINE_INDEX = true;
	private static int LOG_LEVEL = 900;
	private static Thread logger_thread;
	
	static {
		logger_thread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(10);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
				
				StringBuilder sb = new StringBuilder();
				while(!messages.isEmpty()) {
					MSG msg = messages.poll();
					sb.append(msg.toMessage()).append("\n");
				}
				
				if(sb.length() > 0) {
					String str = sb.toString().trim();
					System.out.print(str);
				}
			}
		});
		logger_thread.setDaemon(true);
		logger_thread.start();
	}
	
	public static void setLogLevel(Level level) {
		LOG_LEVEL = level.level;
	}
	
	public static void showLineIndex(boolean enable) {
		SHOW_LINE_INDEX = enable;
	}
	
	@SuppressWarnings("unused")
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
	
	private void internal_log(Level level, int line_index, String format, Object... args) {
		if(!shouldLog(level)) return;
		if(format == null) format = "";
		
		//messages.add(new MSG(level, customName, line_index, format, args));
		// [%s]: 
		System.out.printf("%s\n", String.format(format, args)); //formatString(level, line_index, format, args));
	}
	
	public void log(Level level, String format, Object... args) {
		internal_log(level, getLineIndex(), format, args);
	}
	
	public void info() {
		internal_log(Level.INFO, getLineIndex(), "");
	}
	
	public void info(String format, Object... args) {
		internal_log(Level.INFO, getLineIndex(), format, args);
	}
	
	public void debug() {
		internal_log(Level.DEBUG, getLineIndex(), "");
	}
	
	public void debug(Object format, Object... args) {
		internal_log(Level.DEBUG, getLineIndex(), Objects.toString(format), args);
	}
	
	public void warn(String format, Object... args) {
		internal_log(Level.WARNING, getLineIndex(), format, args);
	}
	
	public void error(String format, Object... args) {
		internal_log(Level.ERROR, getLineIndex(), format, args);
	}
	
	public void throwing(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		internal_log(Level.ERROR, getLineIndex(), sw.toString());
	}
	
	private boolean shouldLog(Level level) {
		return level.level <= LOG_LEVEL;
	}
	
	private int getLineIndex() {
		if(!SHOW_LINE_INDEX) return -1;
		
		StackTraceElement[] stack = Thread.getAllStackTraces().get(Thread.currentThread());
		if(stack == null) return -1;
		StackTraceElement last = stack[4];
		return last.getLineNumber();
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
