package com.guodong.android.logger.printer.file.backup;

import java.io.File;

/**
 * Created by guodongAndroid on 2024/5/7 16:52.
 */
public final class FileSizeBackupStrategy extends AbstractBackupStrategy {

    private final long maxLength;

    private final int maxBackupIndex;

    public FileSizeBackupStrategy(long maxLength, int maxBackupIndex) {
        this.maxLength = maxLength;
        this.maxBackupIndex = maxBackupIndex;
    }

    @Override
    public boolean shouldBackup(File file) {
        return file.length() > maxLength;
    }

    @Override
    public int getMaxBackupIndex() {
        return maxBackupIndex;
    }
}
