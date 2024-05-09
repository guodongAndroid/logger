package com.guodong.android.logger.printer.file.clean;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by guodongAndroid on 2024/5/7 16:28.
 */
public final class FileCountCleanStrategy implements CleanStrategy {

    private static final int FILE_COUNT_THRESHOLD = 20;

    private final int fileCountThreshold;

    private final Comparator<File> fileComparator = new FileLastModifiedComparator(true);

    public FileCountCleanStrategy() {
        this(FILE_COUNT_THRESHOLD);
    }

    public FileCountCleanStrategy(int fileCountThreshold) {
        this.fileCountThreshold = fileCountThreshold;
    }

    @Override
    public @NotNull List<File> filterCleanFile(@NotNull List<File> files) {
        if (files.size() < fileCountThreshold) {
            return files;
        }

        List<File> filteredFiles = new ArrayList<>();

        Collections.sort(files, fileComparator);

        for (int i = files.size() - 1; i >= fileCountThreshold - 1; i--) {
            filteredFiles.add(files.get(i));
        }

        return filteredFiles;
    }

    @Override
    public boolean shouldClean(@NotNull File file) {
        return true;
    }
}
