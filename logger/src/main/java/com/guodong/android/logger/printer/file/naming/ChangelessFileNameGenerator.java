package com.guodong.android.logger.printer.file.naming;

import com.guodong.android.logger.Log;

import org.jetbrains.annotations.Nullable;

/**
 * Created by guodongAndroid on 2024/5/7 15:54.
 */
public final class ChangelessFileNameGenerator implements FileNameGenerator {

    private final String fileName;

    public ChangelessFileNameGenerator(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean isFileNameChangeable() {
        return false;
    }

    @Override
    public @Nullable String generate(Log log) {
        return fileName;
    }
}
