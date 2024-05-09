package com.guodong.android.logger.formatter.object;

import android.content.Intent;

import com.guodong.android.logger.internal.util.ObjectToStringUtil;

/**
 * Created by guodongAndroid on 2024/5/7 13:39.
 */
public final class IntentFormatter implements ObjectFormatter<Intent> {

    @Override
    public String format(Intent data) {
        return ObjectToStringUtil.intentToString(data);
    }
}
