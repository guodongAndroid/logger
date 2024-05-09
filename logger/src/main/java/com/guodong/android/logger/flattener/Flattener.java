package com.guodong.android.logger.flattener;

import com.guodong.android.logger.Log;

/**
 * Created by guodongAndroid on 2024/5/7 11:23.
 */
public interface Flattener {

    CharSequence flatten(Log log);

}
