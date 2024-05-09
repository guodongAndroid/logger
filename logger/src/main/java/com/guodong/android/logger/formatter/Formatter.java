package com.guodong.android.logger.formatter;

/**
 * Created by guodongAndroid on 2024/5/7 13:19.
 */
public interface Formatter<T> {

    String format(T data);
}
