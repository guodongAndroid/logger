package com.guodong.android.logger.interceptor;

import com.guodong.android.logger.Log;

import org.jetbrains.annotations.Nullable;

/**
 * Created by guodongAndroid on 2024/5/7 13:04.
 */
public interface Interceptor {

    @Nullable
    Log intercept(Log log);
}
