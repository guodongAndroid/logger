package com.guodong.android.logger.interceptor;

import android.text.TextUtils;

import com.guodong.android.logger.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created by guodongAndroid on 2024/5/7 13:11.
 */
public final class BlacklistTagsFilterInterceptor extends AbstractFilterInterceptor {

    private final Iterable<String> blacklistTags;

    public BlacklistTagsFilterInterceptor(String... blacklistTags) {
        this(Arrays.asList(blacklistTags));
    }

    public BlacklistTagsFilterInterceptor(@NotNull Iterable<String> blacklistTags) {
        this.blacklistTags = blacklistTags;
    }

    @Override
    public boolean test(Log log) {
        for (String blackTag : blacklistTags) {
            if (TextUtils.equals(blackTag, log.getTag())) {
                return false;
            }
        }
        return true;
    }
}
