package com.guodong.android.logger.printer.file.backup;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by guodongAndroid on 2024/5/7 16:51.
 */
public class NeverBackupStrategy implements BackupStrategy {

    @Override
    public boolean shouldBackup(File file) {
        return false;
    }

    @Override
    public int getMaxBackupIndex() {
        return 0;
    }

    @Override
    public @NotNull String getBackupFileName(String fileName, int backupIndex) {
        return "";
    }
}
