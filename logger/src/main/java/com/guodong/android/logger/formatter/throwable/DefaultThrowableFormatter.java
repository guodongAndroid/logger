package com.guodong.android.logger.formatter.throwable;

import com.guodong.android.logger.internal.util.StackTraceUtil;

/**
 * Created by guodongAndroid on 2024/5/7 13:40.
 */
public final class DefaultThrowableFormatter implements ThrowableFormatter {

    @Override
    public String format(Throwable data) {
        return StackTraceUtil.getStackTraceString(data);
    }
}
