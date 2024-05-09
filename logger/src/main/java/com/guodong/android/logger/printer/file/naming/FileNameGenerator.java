package com.guodong.android.logger.printer.file.naming;

import com.guodong.android.logger.Log;

import org.jetbrains.annotations.Nullable;

/**
 * Created by guodongAndroid on 2024/5/7 15:47.
 */
public interface FileNameGenerator {

    boolean isFileNameChangeable();

    @Nullable
    String generate(Log log);

}
