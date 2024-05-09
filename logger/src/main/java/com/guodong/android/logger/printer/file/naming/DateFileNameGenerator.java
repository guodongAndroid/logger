package com.guodong.android.logger.printer.file.naming;

import com.guodong.android.logger.Log;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by guodongAndroid on 2024/5/7 15:51.
 */
public final class DateFileNameGenerator implements FileNameGenerator {

    private final ThreadLocal<SimpleDateFormat> localDateFormat = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        }
    };

    @Override
    public boolean isFileNameChangeable() {
        return true;
    }

    @Override
    public @Nullable String generate(Log log) {
        SimpleDateFormat sdf = localDateFormat.get();
        if (sdf == null) {
            return null;
        }

        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(log.getTimestamp()));
    }
}
