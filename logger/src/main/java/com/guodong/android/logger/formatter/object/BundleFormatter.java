package com.guodong.android.logger.formatter.object;

import android.os.Bundle;

import com.guodong.android.logger.internal.util.ObjectToStringUtil;

/**
 * Created by guodongAndroid on 2024/5/7 13:38.
 */
public final class BundleFormatter implements ObjectFormatter<Bundle> {

    @Override
    public String format(Bundle data) {
        return ObjectToStringUtil.bundleToString(data);
    }
}
