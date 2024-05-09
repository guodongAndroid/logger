package com.guodong.android.logger.printer.file.naming;

import com.guodong.android.logger.Log;
import com.guodong.android.logger.LogLevel;

import org.jetbrains.annotations.Nullable;

/**
 * Created by guodongAndroid on 2024/5/7 15:55.
 */
public final class LevelFileNameGenerator implements FileNameGenerator {

    @Override
    public boolean isFileNameChangeable() {
        return true;
    }

    @Override
    public @Nullable String generate(Log log) {
        return LogLevel.getLevelName(log.getLevel());
    }
}
