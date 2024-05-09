package com.guodong.android.logger.formatter;

/**
 * Created by guodongAndroid on 2024/5/7 13:20.
 */
public class FormatException extends RuntimeException {

    private static final long serialVersionUID = -3471986730023367651L;

    public FormatException() {
    }

    public FormatException(String message) {
        super(message);
    }

    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormatException(Throwable cause) {
        super(cause);
    }
}
