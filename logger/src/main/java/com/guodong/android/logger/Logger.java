package com.guodong.android.logger;

import com.guodong.android.logger.formatter.border.BorderFormatter;
import com.guodong.android.logger.formatter.object.ObjectFormatter;
import com.guodong.android.logger.interceptor.Interceptor;
import com.guodong.android.logger.internal.DefaultsFactory;
import com.guodong.android.logger.internal.Platform;
import com.guodong.android.logger.internal.SystemCompat;
import com.guodong.android.logger.internal.util.StackTraceUtil;
import com.guodong.android.logger.printer.CompositePrinter;
import com.guodong.android.logger.printer.Printer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by guodongAndroid on 2024/5/7 11:11.
 */
public final class Logger {

    private static LogConfiguration sLogConfiguration;

    private static LogConfiguration.Builder sBuilder;

    private static CompositePrinter sPrinter;

    private static boolean isInitialized;

    public static void init() {
        init(new LogConfiguration.Builder().build(), DefaultsFactory.createPrinter());
    }

    public static void init(int level) {
        init(new LogConfiguration.Builder().level(level).build(),
                DefaultsFactory.createPrinter());
    }

    public static void init(LogConfiguration logConfiguration) {
        init(logConfiguration, DefaultsFactory.createPrinter());
    }

    public static void init(Printer... printers) {
        init(new LogConfiguration.Builder().build(), printers);
    }

    public static void init(int level, Printer... printers) {
        init(new LogConfiguration.Builder().level(level).build(), printers);
    }

    public static void init(String tag, Printer... printers) {
        init(new LogConfiguration.Builder().tag(tag).build(), printers);
    }

    public static void init(int level, String tag, Printer... printers) {
        init(new LogConfiguration.Builder().level(level).tag(tag).build(), printers);
    }

    public static void init(@NotNull LogConfiguration configuration, @NotNull Printer... printers) {
        if (isInitialized) {
            Platform.get().warn("Logger is already initialized, do not initialize again.");
            return;
        }

        isInitialized = true;

        sLogConfiguration = configuration;
        sPrinter = new CompositePrinter(printers);
    }

    public static LogConfiguration configuration() {
        assertInitialization();
        return sLogConfiguration;
    }

    public static LogConfiguration.Builder newBuilder() {
        return getOrCreateBuilder();
    }

    public static void addPrinter(Printer... printers) {
        assertInitialization();
        sPrinter.addPrinter(printers);
    }

    public static void removePrinter(Printer printer) {
        assertInitialization();
        sPrinter.removePrinter(printer);
    }

    public static void v(Object object) {
        println(LogLevel.VERBOSE, null, object);
    }

    public static void v(String tag, Object object) {
        println(LogLevel.VERBOSE, tag, object);
    }

    public static void v(String format, Object... args) {
        println(LogLevel.VERBOSE, null, format, args);
    }

    public static void v(String tag, String format, Object... args) {
        println(LogLevel.VERBOSE, tag, format, args);
    }

    public static void v(String message) {
        println(LogLevel.VERBOSE, null, message);
    }

    public static void v(String tag, String message) {
        println(LogLevel.VERBOSE, tag, message);
    }

    public static void v(String message, Throwable tr) {
        println(LogLevel.VERBOSE, message, tr);
    }

    public static void v(String tag, String message, Throwable tr) {
        println(LogLevel.VERBOSE, tag, message, tr);
    }

    public static void d(Object object) {
        println(LogLevel.DEBUG, null, object);
    }

    public static void d(String tag, Object object) {
        println(LogLevel.DEBUG, tag, object);
    }

    public static void d(String format, Object... args) {
        println(LogLevel.DEBUG, null, format, args);
    }

    public static void d(String tag, String format, Object... args) {
        println(LogLevel.DEBUG, tag, format, args);
    }

    public static void d(String message) {
        println(LogLevel.DEBUG, null, message);
    }

    public static void d(String tag, String message) {
        println(LogLevel.DEBUG, tag, message);
    }

    public static void d(String message, Throwable tr) {
        println(LogLevel.DEBUG, null, message, tr);
    }

    public static void d(String tag, String message, Throwable tr) {
        println(LogLevel.DEBUG, tag, message, tr);
    }

    public static void i(Object object) {
        println(LogLevel.INFO, null, object);
    }

    public static void i(String tag, Object object) {
        println(LogLevel.INFO, tag, object);
    }

    public static void i(String format, Object... args) {
        println(LogLevel.INFO, null, format, args);
    }

    public static void i(String tag, String format, Object... args) {
        println(LogLevel.INFO, tag, format, args);
    }

    public static void i(String message) {
        println(LogLevel.INFO, null, message);
    }

    public static void i(String tag, String message) {
        println(LogLevel.INFO, tag, message);
    }

    public static void i(String message, Throwable tr) {
        println(LogLevel.INFO, null, message, tr);
    }

    public static void i(String tag, String message, Throwable tr) {
        println(LogLevel.INFO, tag, message, tr);
    }

    public static void w(Object object) {
        println(LogLevel.WARN, null, object);
    }

    public static void w(String tag, Object object) {
        println(LogLevel.WARN, tag, object);
    }

    public static void w(String format, Object... args) {
        println(LogLevel.WARN, null, format, args);
    }

    public static void w(String tag, String format, Object... args) {
        println(LogLevel.WARN, tag, format, args);
    }

    public static void w(String message) {
        println(LogLevel.WARN, null, message);
    }

    public static void w(String tag, String message) {
        println(LogLevel.WARN, tag, message);
    }

    public static void w(String message, Throwable tr) {
        println(LogLevel.WARN, null, message, tr);
    }

    public static void w(String tag, String message, Throwable tr) {
        println(LogLevel.WARN, tag, message, tr);
    }

    public static void e(Object object) {
        println(LogLevel.ERROR, null, object);
    }

    public static void e(String tag, Object object) {
        println(LogLevel.ERROR, tag, object);
    }

    public static void e(String format, Object... args) {
        println(LogLevel.ERROR, null, format, args);
    }

    public static void e(String tag, String format, Object... args) {
        println(LogLevel.ERROR, tag, format, args);
    }

    public static void e(String message) {
        println(LogLevel.ERROR, null, message);
    }

    public static void e(String tag, String message) {
        println(LogLevel.ERROR, tag, message);
    }

    public static void e(String message, Throwable tr) {
        println(LogLevel.ERROR, null, message, tr);
    }

    public static void e(String tag, String message, Throwable tr) {
        println(LogLevel.ERROR, tag, message, tr);
    }

    public static void a(Object object) {
        println(LogLevel.ASSERT, null, object);
    }

    public static void a(String tag, Object object) {
        println(LogLevel.ASSERT, tag, object);
    }

    public static void a(String format, Object... args) {
        println(LogLevel.ASSERT, null, format, args);
    }

    public static void a(String tag, String format, Object... args) {
        println(LogLevel.ASSERT, tag, format, args);
    }

    public static void a(String message) {
        println(LogLevel.ASSERT, null, message);
    }

    public static void a(String tag, String message) {
        println(LogLevel.ASSERT, tag, message);
    }

    public static void a(String message, Throwable tr) {
        println(LogLevel.ASSERT, null, message, tr);
    }

    public static void a(String tag, String message, Throwable tr) {
        println(LogLevel.ASSERT, tag, message, tr);
    }

    public static void log(int level, Object object) {
        println(level, null, object);
    }

    public static void log(int level, String tag, Object object) {
        println(level, tag, object);
    }

    public static void log(int level, String format, Object... args) {
        println(level, null, format, args);
    }

    public static void log(int level, String tag, String format, Object... args) {
        println(level, tag, format, args);
    }

    public static void log(int level, String message) {
        println(level, null, message);
    }

    public static void log(int level, String tag, String message) {
        println(level, tag, message);
    }

    public static void log(int level, String message, Throwable tr) {
        println(level, null, message, tr);
    }

    public static void log(int level, String tag, String message, Throwable tr) {
        println(level, tag, message, tr);
    }

    public static void json(String json) {
        json(null, json);
    }

    public static void json(String tag, String json) {
        assertInitialization();
        rebuildLogConfigurationIfNecessary();

        if (LogLevel.DEBUG < sLogConfiguration.getLevel()) {
            return;
        }
        printlnInternal(LogLevel.DEBUG, tag, sLogConfiguration.getJsonFormatter().format(json));
    }

    public static void xml(String xml) {
        xml(null, xml);
    }

    public static void xml(String tag, String xml) {
        assertInitialization();
        rebuildLogConfigurationIfNecessary();

        if (LogLevel.DEBUG < sLogConfiguration.getLevel()) {
            return;
        }
        printlnInternal(LogLevel.DEBUG, tag, sLogConfiguration.getXmlFormatter().format(xml));
    }

    private static <T> void println(int level, @Nullable String tag, T object) {
        assertInitialization();
        rebuildLogConfigurationIfNecessary();

        if (level < sLogConfiguration.getLevel()) {
            return;
        }
        String objectString;
        if (object != null) {
            ObjectFormatter<? super T> objectFormatter = sLogConfiguration.getObjectFormatter(object);
            if (objectFormatter != null) {
                objectString = objectFormatter.format(object);
            } else {
                objectString = object.toString();
            }
        } else {
            objectString = "null";
        }
        printlnInternal(level, tag, objectString);
    }

    private static void println(int level, @Nullable String tag, String format, Object... args) {
        assertInitialization();
        rebuildLogConfigurationIfNecessary();

        if (level < sLogConfiguration.getLevel()) {
            return;
        }
        printlnInternal(level, tag, formatArgs(format, args));
    }

    private static void println(int level, @Nullable String tag, String message) {
        assertInitialization();
        rebuildLogConfigurationIfNecessary();

        if (level < sLogConfiguration.getLevel()) {
            return;
        }
        printlnInternal(level, tag, message != null ? message : "");
    }

    private static void println(int level, @Nullable String tag, String message, Throwable tr) {
        assertInitialization();
        rebuildLogConfigurationIfNecessary();

        if (level < sLogConfiguration.getLevel()) {
            return;
        }
        printlnInternal(level, tag, ((message == null || message.isEmpty())
                ? "" : (message + SystemCompat.lineSeparator))
                + sLogConfiguration.getThrowableFormatter().format(tr));
    }

    private static void printlnInternal(int level, @Nullable String tag, String message) {
        tag = tag != null ? tag : sLogConfiguration.getTag();
        String thread = sLogConfiguration.isWithThread()
                ? sLogConfiguration.getThreadFormatter().format(Thread.currentThread())
                : null;
        String stackTrace = sLogConfiguration.isWithStackTrace()
                ? sLogConfiguration.getStackTraceFormatter().format(
                StackTraceUtil.getCroppedRealStackTrack(new Throwable().getStackTrace(),
                        sLogConfiguration.getStackTraceOrigin(),
                        sLogConfiguration.getStackTraceDepth()))
                : null;

        Log log = new Log(level, tag, message, thread, stackTrace);

        List<Interceptor> interceptors = sLogConfiguration.getInterceptors();
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                log = interceptor.intercept(log);
                if (log == null) {
                    return;
                }

                if (log.getTag() == null || log.getMessage() == null) {
                    Platform.get().error("Interceptor " + interceptor
                            + " should not remove the tag or message of a log,"
                            + " if you don't want to print this log,"
                            + " just return a null when intercept.");
                    return;
                }
            }
        }

        formatMessage(log);

        sPrinter.println(log);
    }

    private static void formatMessage(Log log) {
        String thread = log.getThreadInfo();
        String stackTrace = log.getStackTraceInfo();
        String message = log.getMessage();

        if (sLogConfiguration.isWithBorder()) {
            BorderFormatter borderFormatter = sLogConfiguration.getBorderFormatter();
            String format = borderFormatter.format(new String[]{thread, stackTrace, message});
            log.setFormattedMessage(format);
        } else if (thread != null && stackTrace != null) {
            String formatted = thread + SystemCompat.lineSeparator +
                    stackTrace + SystemCompat.lineSeparator + message;
            log.setFormattedMessage(formatted);
        } else if (thread != null) {
            log.setFormattedMessage(thread + SystemCompat.lineSeparator + message);
        } else if (stackTrace != null) {
            log.setFormattedMessage(SystemCompat.lineSeparator + stackTrace + SystemCompat.lineSeparator + message);
        } else {
            log.setFormattedMessage(message);
        }
    }

    private static String formatArgs(String format, Object... args) {
        if (format != null) {
            return String.format(format, args);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0, N = args.length; i < N; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(args[i]);
            }
            return sb.toString();
        }
    }

    private static void assertInitialization() {
        if (!isInitialized) {
            throw new IllegalStateException("Do you forget to initialize Logger?");
        }
    }

    private static LogConfiguration.Builder getOrCreateBuilder() {
        assertInitialization();

        if (sBuilder == null) {
            sBuilder = sLogConfiguration.newBuilder();
        }

        return sBuilder;
    }

    private static void rebuildLogConfigurationIfNecessary() {
        if (sBuilder != null) {
            sLogConfiguration = sBuilder.build();
            sBuilder = null;
        }
    }
}
