package com.guodong.android.logger.flattener;

import com.guodong.android.logger.Log;
import com.guodong.android.logger.LogLevel;
import com.guodong.android.logger.internal.SystemCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by guodongAndroid on 2024/5/7 11:30.
 */
public class PatternFlattener implements Flattener {

    private static final String PARAM = "[^{}]*";

    private static final Pattern PARAM_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");

    private static final String PARAMETER_DATE = "d";

    private static final String PARAMETER_LEVEL_SHORT = "l";

    private static final String PARAMETER_LEVEL_LONG = "L";

    private static final String PARAMETER_TAG = "t";

    private static final String PARAMETER_LINE_BREAKS = "n";

    private static final String PARAMETER_MESSAGE = "m";

    static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private final String pattern;

    private final List<ParameterFiller> parameterFillers;

    public PatternFlattener(String pattern) {
        if (pattern == null) {
            throw new NullPointerException("Pattern should not be null");
        }
        this.pattern = pattern;

        List<String> parameters = parsePattern(pattern);
        parameterFillers = parseParameters(parameters);
        if (parameterFillers.isEmpty()) {
            throw new IllegalArgumentException("No recognizable parameter found in the pattern "
                    + pattern);
        }
    }

    static List<String> parsePattern(String pattern) {
        List<String> parameters = new ArrayList<>(4);
        Matcher matcher = PARAM_REGEX.matcher(pattern);
        while (matcher.find()) {
            parameters.add(matcher.group(1));
        }
        return parameters;
    }

    private static List<ParameterFiller> parseParameters(List<String> parameters) {
        List<ParameterFiller> parameterFillers = new ArrayList<>(parameters.size());
        for (String parameter : parameters) {
            ParameterFiller parameterFiller = parseParameter(parameter);
            if (parameterFiller != null) {
                parameterFillers.add(parameterFiller);
            }
        }
        return parameterFillers;
    }

    private static ParameterFiller parseParameter(String parameter) {
        String wrappedParameter = "{" + parameter + "}";
        String trimmedParameter = parameter.trim();
        ParameterFiller parameterFiller = parseDateParameter(wrappedParameter, trimmedParameter);
        if (parameterFiller != null) {
            return parameterFiller;
        }

        parameterFiller = parseLevelParameter(wrappedParameter, trimmedParameter);
        if (parameterFiller != null) {
            return parameterFiller;
        }

        parameterFiller = parseTagParameter(wrappedParameter, trimmedParameter);
        if (parameterFiller != null) {
            return parameterFiller;
        }

        parameterFiller = parseLineBreaksParameter(wrappedParameter, trimmedParameter);
        if (parameterFiller != null) {
            return parameterFiller;
        }

        parameterFiller = parseMessageParameter(wrappedParameter, trimmedParameter);
        return parameterFiller;
    }

    static DateFiller parseDateParameter(String wrappedParameter, String trimmedParameter) {
        if (trimmedParameter.startsWith(PARAMETER_DATE + " ")
                && trimmedParameter.length() > PARAMETER_DATE.length() + 1) {
            String dateFormat = trimmedParameter.substring(PARAMETER_DATE.length() + 1);
            return new DateFiller(wrappedParameter, trimmedParameter, dateFormat);
        } else if (trimmedParameter.equals(PARAMETER_DATE)) {
            return new DateFiller(wrappedParameter, trimmedParameter, DEFAULT_DATE_FORMAT);
        }
        return null;
    }

    static LevelFiller parseLevelParameter(String wrappedParameter, String trimmedParameter) {
        if (trimmedParameter.equals(PARAMETER_LEVEL_SHORT)) {
            return new LevelFiller(wrappedParameter, trimmedParameter, false);
        } else if (trimmedParameter.equals(PARAMETER_LEVEL_LONG)) {
            return new LevelFiller(wrappedParameter, trimmedParameter, true);
        }
        return null;
    }

    static TagFiller parseTagParameter(String wrappedParameter, String trimmedParameter) {
        if (trimmedParameter.equals(PARAMETER_TAG)) {
            return new TagFiller(wrappedParameter, trimmedParameter);
        }
        return null;
    }

    static LineBreaksFiller parseLineBreaksParameter(String wrappedParameter, String trimmedParameter) {
        if (PARAMETER_LINE_BREAKS.equals(trimmedParameter)) {
            return new LineBreaksFiller(wrappedParameter, trimmedParameter);
        }
        return null;
    }

    static MessageFiller parseMessageParameter(String wrappedParameter, String trimmedParameter) {
        if (trimmedParameter.equals(PARAMETER_MESSAGE)) {
            return new MessageFiller(wrappedParameter, trimmedParameter);
        }
        return null;
    }

    @Override
    public CharSequence flatten(Log log) {
        long timestamp = log.getTimestamp();
        int level = log.getLevel();
        String tag = log.getTag();
        String message = log.getFormattedMessage();

        String flattenedLog = pattern;
        for (ParameterFiller parameterFiller : parameterFillers) {
            flattenedLog = parameterFiller.fill(flattenedLog, timestamp, level, tag, message);
        }
        return flattenedLog;
    }

    static class DateFiller extends ParameterFiller {

        String dateFormat;

        private final ThreadLocal<SimpleDateFormat> threadLocalDateFormat = new ThreadLocal<SimpleDateFormat>() {

            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat(dateFormat, Locale.US);
            }
        };

        DateFiller(String wrappedParameter, String trimmedParameter, String dateFormat) {
            super(wrappedParameter, trimmedParameter);
            this.dateFormat = dateFormat;

            try {
                // Test the format, will throw an exception if it is a bad format.
                Objects.requireNonNull(threadLocalDateFormat.get()).format(new Date());
            } catch (Exception e) {
                throw new IllegalArgumentException("Bad date pattern: " + dateFormat, e);
            }
        }

        @Override
        protected String fill(String pattern, long timestamp, int level, String tag, String message) {
            return pattern.replace(wrappedParameter, Objects.requireNonNull(threadLocalDateFormat.get()).format(new Date(timestamp)));
        }
    }

    static class LevelFiller extends ParameterFiller {

        boolean useLongName;

        LevelFiller(String wrappedParameter, String trimmedParameter, boolean useLongName) {
            super(wrappedParameter, trimmedParameter);
            this.useLongName = useLongName;
        }

        @Override
        protected String fill(String pattern, long timestamp, int level, String tag, String message) {
            if (useLongName) {
                return pattern.replace(wrappedParameter, LogLevel.getLevelName(level));
            } else {
                return pattern.replace(wrappedParameter, LogLevel.getShortLevelName(level));
            }
        }
    }

    static class TagFiller extends ParameterFiller {

        TagFiller(String wrappedParameter, String trimmedParameter) {
            super(wrappedParameter, trimmedParameter);
        }

        @Override
        protected String fill(String pattern, long timestamp, int level, String tag, String message) {
            return pattern.replace(wrappedParameter, tag);
        }
    }

    static class LineBreaksFiller extends ParameterFiller {

        LineBreaksFiller(String wrappedParameter, String trimmedParameter) {
            super(wrappedParameter, trimmedParameter);
        }

        @Override
        protected String fill(String pattern, long timestamp, int level, String tag, String message) {
            return pattern.replace(wrappedParameter, SystemCompat.lineSeparator);
        }
    }

    static class MessageFiller extends ParameterFiller {

        MessageFiller(String wrappedParameter, String trimmedParameter) {
            super(wrappedParameter, trimmedParameter);
        }

        @Override
        protected String fill(String pattern, long timestamp, int level, String tag, String message) {
            return pattern.replace(wrappedParameter, message);
        }
    }

    abstract static class ParameterFiller {

        String wrappedParameter;

        String trimmedParameter;

        ParameterFiller(String wrappedParameter, String trimmedParameter) {
            this.wrappedParameter = wrappedParameter;
            this.trimmedParameter = trimmedParameter;
        }

        protected abstract String fill(String pattern, long timestamp, int level, String tag, String message);
    }
}
