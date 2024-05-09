package com.guodong.android.logger.interceptor;

import com.guodong.android.logger.Log;

/**
 * Created by guodongAndroid on 2024/5/7 13:04.
 */
public abstract class AbstractFilterInterceptor implements Interceptor {
    @Override
    public Log intercept(Log log) {
        if (test(log)) {
            return log;
        }
        return null;
    }

    public abstract boolean test(Log log);
}
