package com.guodong.android.logger.printer.file.clean;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by guodongAndroid on 2024/5/7 15:59.
 */
public final class NeverCleanStrategy implements CleanStrategy {

    @Override
    public @NotNull List<File> filterCleanFile(@NotNull List<File> files) {
        return Collections.emptyList();
    }

    @Override
    public boolean shouldClean(@NotNull File file) {
        return false;
    }
}
