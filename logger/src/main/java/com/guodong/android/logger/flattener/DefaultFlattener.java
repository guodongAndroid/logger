package com.guodong.android.logger.flattener;

import com.guodong.android.logger.Log;
import com.guodong.android.logger.LogLevel;

/**
 * Created by guodongAndroid on 2024/5/7 11:25.
 */
public final class DefaultFlattener implements Flattener {

    @Override
    public CharSequence flatten(Log log) {
        return Long.toString(log.getTimestamp())
                + '|' + LogLevel.getShortLevelName(log.getLevel())
                + '|' + log.getTag()
                + '|' + log.getMessage();
    }
}
