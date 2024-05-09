package com.guodong.android.logger.internal.util;

import com.guodong.android.logger.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * Created by guodongAndroid on 2024/5/7 11:23.
 */
public class StackTraceUtil {

    private static final String LOGGER_STACK_TRACE_ORIGIN = Logger.class.getName();

    /* static {
        String loggerClassName = Logger.class.getName();
        LOGGER_STACK_TRACE_ORIGIN = loggerClassName.substring(0, loggerClassName.lastIndexOf('.') + 1);
    } */

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static StackTraceElement[] getCroppedRealStackTrack(StackTraceElement[] stackTrace,
                                                               String stackTraceOrigin,
                                                               int maxDepth) {
        return cropStackTrace(getRealStackTrack(stackTrace, stackTraceOrigin), maxDepth);
    }

    private static StackTraceElement[] getRealStackTrack(StackTraceElement[] stackTrace,
                                                         String stackTraceOrigin) {
        int ignoreDepth = 0;
        int allDepth = stackTrace.length;
        String className;
        for (int i = allDepth - 1; i >= 0; i--) {
            className = stackTrace[i].getClassName();
            if (className.startsWith(LOGGER_STACK_TRACE_ORIGIN)
                    || (stackTraceOrigin != null && className.startsWith(stackTraceOrigin))) {
                ignoreDepth = i + 1;
                break;
            }
        }
        int realDepth = allDepth - ignoreDepth;
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        System.arraycopy(stackTrace, ignoreDepth, realStack, 0, realDepth);
        return realStack;
    }

    private static StackTraceElement[] cropStackTrace(StackTraceElement[] callStack,
                                                      int maxDepth) {
        int realDepth = callStack.length;
        if (maxDepth > 0) {
            realDepth = Math.min(maxDepth, realDepth);
        }
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        System.arraycopy(callStack, 0, realStack, 0, realDepth);
        return realStack;
    }
}
