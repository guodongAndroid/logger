package com.guodong.android.logger.printer.file.clean;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by guodongAndroid on 2024/5/7 15:57.
 */
public final class FileLastModifiedCleanStrategy implements CleanStrategy {

    private final long maxTimeMillis;

    public FileLastModifiedCleanStrategy(long maxTimeMillis) {
        this.maxTimeMillis = maxTimeMillis;
    }

    @Override
    public boolean shouldClean(@NotNull File file) {
        long lastModified = file.lastModified();
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis - lastModified > maxTimeMillis;
    }
}
