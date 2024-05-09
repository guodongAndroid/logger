package com.guodong.android.logger.printer.file.backup;

import org.jetbrains.annotations.NotNull;

/**
 * Created by guodongAndroid on 2024/5/7 16:50.
 */
public abstract class AbstractBackupStrategy implements BackupStrategy {

    @Override
    public @NotNull String getBackupFileName(String fileName, int backupIndex) {
        return fileName + ".bak." + backupIndex;
    }
}
