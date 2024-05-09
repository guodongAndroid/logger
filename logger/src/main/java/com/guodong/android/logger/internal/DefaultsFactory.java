package com.guodong.android.logger.internal;

import com.guodong.android.logger.flattener.DefaultFlattener;
import com.guodong.android.logger.flattener.Flattener;
import com.guodong.android.logger.formatter.border.BorderFormatter;
import com.guodong.android.logger.formatter.border.DefaultBorderFormatter;
import com.guodong.android.logger.formatter.json.DefaultJsonFormatter;
import com.guodong.android.logger.formatter.json.JsonFormatter;
import com.guodong.android.logger.formatter.object.ObjectFormatter;
import com.guodong.android.logger.formatter.stacktrace.DefaultStackTraceFormatter;
import com.guodong.android.logger.formatter.stacktrace.StackTraceFormatter;
import com.guodong.android.logger.formatter.thread.DefaultThreadFormatter;
import com.guodong.android.logger.formatter.thread.ThreadFormatter;
import com.guodong.android.logger.formatter.throwable.DefaultThrowableFormatter;
import com.guodong.android.logger.formatter.throwable.ThrowableFormatter;
import com.guodong.android.logger.formatter.xml.DefaultXmlFormatter;
import com.guodong.android.logger.formatter.xml.XmlFormatter;
import com.guodong.android.logger.printer.Printer;
import com.guodong.android.logger.printer.file.backup.BackupStrategy;
import com.guodong.android.logger.printer.file.backup.FileSizeBackupStrategy;
import com.guodong.android.logger.printer.file.clean.CleanStrategy;
import com.guodong.android.logger.printer.file.clean.NeverCleanStrategy;
import com.guodong.android.logger.printer.file.naming.ChangelessFileNameGenerator;
import com.guodong.android.logger.printer.file.naming.FileNameGenerator;
import com.guodong.android.logger.printer.file.writer.DefaultWriter;
import com.guodong.android.logger.printer.file.writer.Writer;

import java.util.Map;

/**
 * Created by guodongAndroid on 2024/5/7 15:13.
 */
public final class DefaultsFactory {

    private static final String DEFAULT_LOG_FILE_NAME = "log";

    private static final long DEFAULT_BACKUP_MAX_LENGTH = 1024 * 1024 * 10; // 10M bytes;

    private static final int DEFAULT_BACKUP_MAX_INDEX = 20;

    public static JsonFormatter createJsonFormatter() {
        return new DefaultJsonFormatter();
    }

    public static XmlFormatter createXmlFormatter() {
        return new DefaultXmlFormatter();
    }

    public static ThrowableFormatter createThrowableFormatter() {
        return new DefaultThrowableFormatter();
    }

    public static ThreadFormatter createThreadFormatter() {
        return new DefaultThreadFormatter(true);
    }

    public static StackTraceFormatter createStackTraceFormatter() {
        return new DefaultStackTraceFormatter();
    }

    /**
     * Create the default border formatter.
     */
    public static BorderFormatter createBorderFormatter() {
        return new DefaultBorderFormatter();
    }

    /**
     * Create the default {@link Flattener}.
     */
    public static Flattener createFlattener() {
        return new DefaultFlattener();
    }

    public static Printer createPrinter() {
        return Platform.get().defaultPrinter();
    }

    public static FileNameGenerator createFileNameGenerator() {
        return new ChangelessFileNameGenerator(DEFAULT_LOG_FILE_NAME);
    }

    public static BackupStrategy createBackupStrategy() {
        return new FileSizeBackupStrategy(DEFAULT_BACKUP_MAX_LENGTH, DEFAULT_BACKUP_MAX_INDEX);
    }

    public static CleanStrategy createCleanStrategy() {
        return new NeverCleanStrategy();
    }

    public static Writer createWriter() {
        return new DefaultWriter();
    }

    public static Map<Class<?>, ObjectFormatter<?>> builtinObjectFormatters() {
        return Platform.get().builtinObjectFormatters();
    }
}
