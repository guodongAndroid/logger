package com.guodong.android.logger.printer.file.clean;

import java.io.File;
import java.util.Comparator;

/**
 * Created by guodongAndroid on 2024/5/7 16:32.
 */
public final class FileLastModifiedComparator implements Comparator<File> {

    private final boolean reverseOrder;

    public FileLastModifiedComparator() {
        this(false);
    }

    public FileLastModifiedComparator(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public int compare(File o1, File o2) {
        return reverseOrder ? (int) (o2.lastModified() - o1.lastModified()) : (int) (o1.lastModified() - o2.lastModified());
    }
}
