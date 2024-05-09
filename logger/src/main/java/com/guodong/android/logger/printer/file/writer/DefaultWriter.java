package com.guodong.android.logger.printer.file.writer;

import com.guodong.android.logger.internal.Platform;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by guodongAndroid on 2024/5/7 15:37.
 */
public class DefaultWriter implements Writer {

    private String logFileName;

    private File logFile;

    private BufferedWriter bufferedWriter;

    @Override
    public boolean open(@NotNull File file) {
        logFileName = file.getName();
        logFile = file;

        boolean isNewFile = false;

        // Create log file if not exists.
        if (!logFile.exists()) {
            try {
                File parent = logFile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                logFile.createNewFile();
                isNewFile = true;
            } catch (Exception e) {
                e.printStackTrace();
                close();
                return false;
            }
        }

        // Create buffered writer.
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
            if (isNewFile) {
                onNewFileCreated(logFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return false;
        }

        return true;
    }

    public void onNewFileCreated(File file) {
    }

    @Override
    public boolean isOpened() {
        return bufferedWriter != null && logFile.exists();
    }

    @Override
    public File getOpenedFile() {
        return logFile;
    }

    @Override
    public String getOpenedFileName() {
        return logFileName;
    }

    @Override
    public void write(@NotNull String log) {
        try {
            bufferedWriter.write(log);
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            Platform.get().warn("write log failed: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException ignore) {
            }
        }
        bufferedWriter = null;
        logFileName = null;
        logFile = null;
    }
}
