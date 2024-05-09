package com.guodong.android.logger.printer.file.clean;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * Created by guodongAndroid on 2024/5/7 15:56.
 */
public interface CleanStrategy {

    @NotNull
    default List<File> filterCleanFile(@NotNull List<File> files) {
        return files;
    }

    boolean shouldClean(@NotNull File file);
}
