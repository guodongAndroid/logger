package com.guodong.android.logger;

/**
 * Created by guodongAndroid on 2024/5/7 11:14.
 */
public final class Log {

    private final int level;

    private final String tag;

    private final String message;

    private final String threadInfo;

    private final String stackTraceInfo;

    private String formattedMessage;

    private final long timestamp = System.currentTimeMillis();

    public Log(int level, String tag, String message) {
        this.level = level;
        this.tag = tag;
        this.message = message;
        this.threadInfo = null;
        this.stackTraceInfo = null;
    }

    public Log(int level, String tag, String message, String threadInfo, String stackTraceInfo) {
        this.level = level;
        this.tag = tag;
        this.message = message;
        this.threadInfo = threadInfo;
        this.stackTraceInfo = stackTraceInfo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getLevel() {
        return level;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    public String getThreadInfo() {
        return threadInfo;
    }

    public String getStackTraceInfo() {
        return stackTraceInfo;
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }

    public void setFormattedMessage(String formattedMessage) {
        this.formattedMessage = formattedMessage;
    }
}
