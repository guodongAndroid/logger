package com.guodong.android.logger.formatter.stacktrace;

import com.guodong.android.logger.internal.SystemCompat;

/**
 * Created by guodongAndroid on 2024/5/7 13:45.
 */
public final class DefaultStackTraceFormatter implements StackTraceFormatter {

    @Override
    public String format(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder(256);
        if (stackTrace == null || stackTrace.length == 0) {
            return null;
        } else if (stackTrace.length == 1) {
            return "\t─ " + stackTrace[0].toString();
        } else {
            for (int i = 0, N = stackTrace.length; i < N; i++) {
                if (i != N - 1) {
                    sb.append("\t├ ");
                    sb.append(stackTrace[i].toString());
                    sb.append(SystemCompat.lineSeparator);
                } else {
                    sb.append("\t└ ");
                    sb.append(stackTrace[i].toString());
                }
            }
            return sb.toString();
        }
    }
}
