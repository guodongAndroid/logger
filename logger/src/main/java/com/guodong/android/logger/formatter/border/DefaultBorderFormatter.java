package com.guodong.android.logger.formatter.border;

import com.guodong.android.logger.internal.SystemCompat;

/**
 * Created by guodongAndroid on 2024/5/7 13:25.
 */
public final class DefaultBorderFormatter implements BorderFormatter {

    private static final char VERTICAL_BORDER_CHAR = '║';

    // Length: 100.
    private static final String TOP_HORIZONTAL_BORDER =
            "╔═════════════════════════════════════════════════" +
                    "══════════════════════════════════════════════════";

    // Length: 99.
    private static final String DIVIDER_HORIZONTAL_BORDER =
            "╟─────────────────────────────────────────────────" +
                    "──────────────────────────────────────────────────";

    // Length: 100.
    private static final String BOTTOM_HORIZONTAL_BORDER =
            "╚═════════════════════════════════════════════════" +
                    "══════════════════════════════════════════════════";

    @Override
    public String format(String[] segments) {
        if (segments == null || segments.length == 0) {
            return "";
        }

        String[] nonNullSegments = new String[segments.length];
        int nonNullCount = 0;
        for (String segment : segments) {
            if (segment != null) {
                nonNullSegments[nonNullCount++] = segment;
            }
        }
        if (nonNullCount == 0) {
            return "";
        }

        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append(SystemCompat.lineSeparator)
                .append(TOP_HORIZONTAL_BORDER)
                .append(SystemCompat.lineSeparator);
        for (int i = 0; i < nonNullCount; i++) {
            msgBuilder.append(appendVerticalBorder(nonNullSegments[i]));
            if (i != nonNullCount - 1) {
                msgBuilder.append(SystemCompat.lineSeparator).append(DIVIDER_HORIZONTAL_BORDER)
                        .append(SystemCompat.lineSeparator);
            } else {
                msgBuilder.append(SystemCompat.lineSeparator).append(BOTTOM_HORIZONTAL_BORDER);
            }
        }
        return msgBuilder.toString();
    }

    private static String appendVerticalBorder(String msg) {
        StringBuilder borderedMsgBuilder = new StringBuilder(msg.length() + 10);
        String[] lines = msg.split(SystemCompat.lineSeparator);
        for (int i = 0, N = lines.length; i < N; i++) {
            if (i != 0) {
                borderedMsgBuilder.append(SystemCompat.lineSeparator);
            }
            String line = lines[i];
            borderedMsgBuilder.append(VERTICAL_BORDER_CHAR).append(line);
        }
        return borderedMsgBuilder.toString();
    }
}
