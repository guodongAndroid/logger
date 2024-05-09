package com.guodong.android.logger.interceptor;

import android.text.TextUtils;

import com.guodong.android.logger.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created by guodongAndroid on 2024/5/7 13:11.
 */
public final class WhitelistTagsFilterInterceptor extends AbstractFilterInterceptor {

    private final Iterable<String> whitelistTags;

    public WhitelistTagsFilterInterceptor(String... whitelistTags) {
        this(Arrays.asList(whitelistTags));
    }

    public WhitelistTagsFilterInterceptor(@NotNull Iterable<String> whitelistTags) {
        this.whitelistTags = whitelistTags;
    }

    @Override
    public boolean test(Log log) {
        for (String blackTag : whitelistTags) {
            if (TextUtils.equals(blackTag, log.getTag())) {
                return true;
            }
        }
        return false;
    }
}
