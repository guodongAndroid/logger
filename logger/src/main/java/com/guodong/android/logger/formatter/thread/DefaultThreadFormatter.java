package com.guodong.android.logger.formatter.thread;

import java.util.Locale;

/**
 * Created by guodongAndroid on 2024/5/7 13:47.
 */
public final class DefaultThreadFormatter implements ThreadFormatter {

    private static final String CLEAN_FORMAT = "Thread(name=%s)";

    private static final String FORMAT = "Thread(name=%s, id=%d, priority=%d, state=%s)";

    private final boolean useClean;

    public DefaultThreadFormatter(boolean useClean) {
        this.useClean = useClean;
    }

    @Override
    public String format(Thread thread) {
        String name = thread.getName();
        if (useClean) {
            return String.format(Locale.US, CLEAN_FORMAT, name);
        } else {
            long id = thread.getId();
            int priority = thread.getPriority();
            String state = thread.getState().name();
            return String.format(Locale.US, FORMAT, name, id, priority, state);
        }
    }
}
