package com.guodong.android.logger;

/**
 * Created by guodongAndroid on 2024/5/7 11:17.
 */
public final class LogLevel {

    public static final int VERBOSE = 2;

    public static final int DEBUG = 3;

    public static final int INFO = 4;

    public static final int WARN = 5;

    public static final int ERROR = 6;

    public static final int ASSERT = 7;

    public static final int ALL = Integer.MIN_VALUE;

    public static final int NONE = Integer.MAX_VALUE;

    public static String getLevelName(int level) {
        String levelName;
        switch (level) {
            case VERBOSE:
                levelName = "VERBOSE";
                break;
            case DEBUG:
                levelName = "DEBUG";
                break;
            case INFO:
                levelName = "INFO";
                break;
            case WARN:
                levelName = "WARN";
                break;
            case ERROR:
                levelName = "ERROR";
                break;
            case ASSERT:
                levelName = "ASSERT";
                break;
            default:
                if (level < VERBOSE) {
                    levelName = "VERBOSE-" + (VERBOSE - level);
                } else {
                    levelName = "ERROR+" + (level - ERROR);
                }
                break;
        }
        return levelName;
    }

    public static String getShortLevelName(int level) {
        String levelName;
        switch (level) {
            case VERBOSE:
                levelName = "V";
                break;
            case DEBUG:
                levelName = "D";
                break;
            case INFO:
                levelName = "I";
                break;
            case WARN:
                levelName = "W";
                break;
            case ERROR:
                levelName = "E";
                break;
            case ASSERT:
                levelName = "A";
                break;
            default:
                if (level < VERBOSE) {
                    levelName = "V-" + (VERBOSE - level);
                } else {
                    levelName = "E+" + (level - ERROR);
                }
                break;
        }
        return levelName;
    }
}
