package com.guodong.android.logger.printer.file;

import com.guodong.android.logger.Log;
import com.guodong.android.logger.flattener.Flattener;
import com.guodong.android.logger.internal.DefaultsFactory;
import com.guodong.android.logger.internal.Platform;
import com.guodong.android.logger.internal.util.BackupUtil;
import com.guodong.android.logger.printer.Printer;
import com.guodong.android.logger.printer.file.backup.BackupStrategy;
import com.guodong.android.logger.printer.file.clean.CleanStrategy;
import com.guodong.android.logger.printer.file.naming.FileNameGenerator;
import com.guodong.android.logger.printer.file.writer.Writer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by guodongAndroid on 2024/5/7 15:33.
 */
public class FilePrinter implements Printer {

    private static final boolean USE_WORKER = true;

    private final String folderPath;

    private final FileNameGenerator fileNameGenerator;

    private final BackupStrategy backupStrategy;

    private final CleanStrategy cleanStrategy;

    private final Flattener flattener;

    private final Writer writer;

    private volatile Worker worker;

    private FilePrinter(Builder builder) {
        folderPath = builder.folderPath;
        fileNameGenerator = builder.fileNameGenerator;
        backupStrategy = builder.backupStrategy;
        cleanStrategy = builder.cleanStrategy;
        flattener = builder.flattener;
        writer = builder.writer;

        if (USE_WORKER) {
            worker = new Worker();
        }

        checkLogFolder();
    }

    private void checkLogFolder() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public void println(Log log) {
        if (USE_WORKER) {
            if (!worker.isStarted()) {
                worker.start();
            }
            worker.enqueue(log);
        } else {
            doPrintln(log);
        }
    }

    private void doPrintln(Log log) {
        String lastFileName = writer.getOpenedFileName();
        boolean isWriterClosed = !writer.isOpened();
        if (lastFileName == null || isWriterClosed || fileNameGenerator.isFileNameChangeable()) {
            String newFileName = fileNameGenerator.generate(log);
            if (newFileName == null || newFileName.trim().isEmpty()) {
                Platform.get().error("File name should not be empty, ignore log: " + log.getMessage());
                return;
            }
            if (!newFileName.equals(lastFileName) || isWriterClosed) {
                writer.close();
                cleanLogFilesIfNecessary();
                if (!writer.open(new File(folderPath, newFileName))) {
                    return;
                }
                lastFileName = newFileName;
            }
        }

        File lastFile = writer.getOpenedFile();
        if (backupStrategy.shouldBackup(lastFile)) {
            // Backup the log file, and create a new log file.
            writer.close();
            BackupUtil.backup(lastFile, backupStrategy);
            if (!writer.open(new File(folderPath, lastFileName))) {
                return;
            }
        }
        String flattenedLog = flattener.flatten(log).toString();
        writer.write(flattenedLog);
    }

    private void cleanLogFilesIfNecessary() {
        File logDir = new File(folderPath);
        File[] files = logDir.listFiles();
        if (files == null) {
            return;
        }

        List<File> filteredFiles = cleanStrategy.filterCleanFile(Arrays.asList(files));
        if (filteredFiles.isEmpty()) {
            return;
        }

        for (File file : filteredFiles) {
            if (cleanStrategy.shouldClean(file)) {
                file.delete();
            }
        }
    }

    public static class Builder {

        @NotNull
        String folderPath;

        FileNameGenerator fileNameGenerator;

        BackupStrategy backupStrategy;

        CleanStrategy cleanStrategy;

        Flattener flattener;

        Writer writer;

        public Builder(@NotNull String folderPath) {
            this.folderPath = folderPath;
        }

        public Builder fileNameGenerator(FileNameGenerator fileNameGenerator) {
            this.fileNameGenerator = fileNameGenerator;
            return this;
        }

        public Builder backupStrategy(BackupStrategy backupStrategy) {
            this.backupStrategy = backupStrategy;
            BackupUtil.verifyBackupStrategy(this.backupStrategy);
            return this;
        }

        public Builder cleanStrategy(CleanStrategy cleanStrategy) {
            this.cleanStrategy = cleanStrategy;
            return this;
        }

        public Builder flattener(Flattener flattener) {
            this.flattener = flattener;
            return this;
        }

        public Builder writer(Writer writer) {
            this.writer = writer;
            return this;
        }

        public FilePrinter build() {
            fillEmptyFields();
            return new FilePrinter(this);
        }

        private void fillEmptyFields() {
            if (fileNameGenerator == null) {
                fileNameGenerator = DefaultsFactory.createFileNameGenerator();
            }
            if (backupStrategy == null) {
                backupStrategy = DefaultsFactory.createBackupStrategy();
            }
            if (cleanStrategy == null) {
                cleanStrategy = DefaultsFactory.createCleanStrategy();
            }
            if (flattener == null) {
                flattener = DefaultsFactory.createFlattener();
            }
            if (writer == null) {
                writer = DefaultsFactory.createWriter();
            }
        }
    }

    private class Worker implements Runnable {

        private final BlockingQueue<Log> logs = new LinkedBlockingQueue<>();

        private volatile boolean started;

        /**
         * Enqueue the log.
         *
         * @param log the log to be written to file
         */
        void enqueue(Log log) {
            try {
                logs.put(log);
            } catch (InterruptedException e) {
                Platform.get().error("Log enqueue failure: " + e.getMessage() + ", ignore log: " + log.getMessage());
            }
        }

        /**
         * Whether the worker is started.
         *
         * @return true if started, false otherwise
         */
        boolean isStarted() {
            synchronized (this) {
                return started;
            }
        }

        /**
         * Start the worker.
         */
        void start() {
            synchronized (this) {
                if (started) {
                    return;
                }
                new Thread(this).start();
                started = true;
            }
        }

        @Override
        public void run() {
            Log log;
            try {
                while ((log = logs.take()) != null) {
                    doPrintln(log);
                }
            } catch (InterruptedException e) {
                Platform.get().error("log write failure: " + e.getMessage());
                synchronized (this) {
                    started = false;
                }
            }
        }
    }
}
