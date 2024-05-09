package com.guodong.android.logger.internal.util;

import com.guodong.android.logger.printer.file.backup.BackupStrategy;

import java.io.File;

/**
 * Created by guodongAndroid on 2024/5/7 13:04.
 */
public class BackupUtil {

    public static void backup(File loggingFile, BackupStrategy backupStrategy) {
        String loggingFileName = loggingFile.getName();
        String path = loggingFile.getParent();
        File backupFile;
        File nextBackupFile;
        int maxBackupIndex = backupStrategy.getMaxBackupIndex();
        if (maxBackupIndex > 0) {
            backupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, maxBackupIndex));
            if (backupFile.exists()) {
                backupFile.delete();
            }
            for (int i = maxBackupIndex - 1; i > 0; i--) {
                backupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, i));
                if (backupFile.exists()) {
                    nextBackupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, i + 1));
                    backupFile.renameTo(nextBackupFile);
                }
            }
            nextBackupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, 1));
            loggingFile.renameTo(nextBackupFile);
        } else if (maxBackupIndex == BackupStrategy.NO_LIMIT) {
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                nextBackupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, i));
                if (!nextBackupFile.exists()) {
                    loggingFile.renameTo(nextBackupFile);
                    break;
                }
            }
        }
    }

    public static void verifyBackupStrategy(BackupStrategy backupStrategy) {
        int maxBackupIndex = backupStrategy.getMaxBackupIndex();
        if (maxBackupIndex < 0) {
            throw new IllegalArgumentException("Max backup index should not be less than 0");
        } else if (maxBackupIndex == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Max backup index too big: " + maxBackupIndex);
        }
    }
}
