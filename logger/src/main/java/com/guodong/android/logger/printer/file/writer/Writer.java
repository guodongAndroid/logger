package com.guodong.android.logger.printer.file.writer;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.File;

/**
 * Created by guodongAndroid on 2024/5/7 15:35.
 */
public interface Writer extends Closeable {

    boolean open(@NotNull File file);

    boolean isOpened();

    File getOpenedFile();

    String getOpenedFileName();

    void write(@NotNull String log);

    void close();
}
