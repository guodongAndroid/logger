package com.guodong.android.logger.printer;

import com.guodong.android.logger.Log;

/**
 * Created by guodongAndroid on 2024/5/7 14:12.
 */
public class AndroidPrinter implements Printer {

    private static final int DEFAULT_MAX_CHUNK_SIZE = 2000;

    private final boolean autoSeparate;

    private final int maxChunkSize;

    public AndroidPrinter() {
        this(false, DEFAULT_MAX_CHUNK_SIZE);
    }

    public AndroidPrinter(boolean autoSeparate) {
        this(autoSeparate, DEFAULT_MAX_CHUNK_SIZE);
    }

    public AndroidPrinter(int maxChunkSize) {
        this(false, maxChunkSize);
    }

    public AndroidPrinter(boolean autoSeparate, int maxChunkSize) {
        this.autoSeparate = autoSeparate;
        this.maxChunkSize = maxChunkSize;
    }

    @Override
    public void println(Log log) {
        int level = log.getLevel();
        String tag = log.getTag();
        String formattedMessage = log.getFormattedMessage();

        int msgLength = formattedMessage.length();
        int start = 0;
        int end;
        while (start < msgLength) {
            if (formattedMessage.charAt(start) == '\n') {
                start++;
                continue;
            }
            end = Math.min(start + maxChunkSize, msgLength);
            if (autoSeparate) {
                int newLine = formattedMessage.indexOf('\n', start);
                end = newLine != -1 ? Math.min(end,newLine) : end;
            } else {
                end = adjustEnd(formattedMessage, start, end);
            }
            writeChunk(level, tag, formattedMessage.substring(start, end));

            start = end;
        }
    }

    private int adjustEnd(String msg, int start, int originEnd) {
        if (originEnd == msg.length()) {
            // Already end of message.
            return originEnd;
        }
        if (msg.charAt(originEnd) == '\n') {
            // Already prior to '\n'.
            return originEnd;
        }
        // Search back for '\n'.
        int last = originEnd - 1;
        while (start < last) {
            if (msg.charAt(last) == '\n') {
                return last;
            }
            last--;
        }
        return originEnd;
    }

    void writeChunk(int level, String tag, String msg) {
        android.util.Log.println(level, tag, msg);
    }
}
