package com.guodong.android.logger.printer.file.backup;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by guodongAndroid on 2024/5/7 16:49.
 */
public interface BackupStrategy {

    int NO_LIMIT = 0;

    boolean shouldBackup(File file);

    int getMaxBackupIndex();

    @NotNull
    String getBackupFileName(String fileName, int backupIndex);
}
