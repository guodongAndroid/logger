package com.guodong.android.logger;

import com.guodong.android.logger.formatter.border.BorderFormatter;
import com.guodong.android.logger.formatter.json.JsonFormatter;
import com.guodong.android.logger.formatter.object.ObjectFormatter;
import com.guodong.android.logger.formatter.stacktrace.StackTraceFormatter;
import com.guodong.android.logger.formatter.thread.ThreadFormatter;
import com.guodong.android.logger.formatter.throwable.ThrowableFormatter;
import com.guodong.android.logger.formatter.xml.XmlFormatter;
import com.guodong.android.logger.interceptor.Interceptor;
import com.guodong.android.logger.internal.DefaultsFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guodongAndroid on 2024/5/8 9:00.
 */
public final class LogConfiguration {

    private final int level;

    private final String tag;

    private final boolean withThread;

    private final boolean withStackTrace;

    private final String stackTraceOrigin;

    private final int stackTraceDepth;

    private final boolean withBorder;

    @NotNull
    private final JsonFormatter jsonFormatter;

    @NotNull
    private final XmlFormatter xmlFormatter;

    @NotNull
    private final ThreadFormatter threadFormatter;

    @NotNull
    private final ThrowableFormatter throwableFormatter;

    @NotNull
    private final StackTraceFormatter stackTraceFormatter;

    @NotNull
    private final BorderFormatter borderFormatter;

    @NotNull
    private final Map<Class<?>, ObjectFormatter<?>> objectFormatters;

    @Nullable
    private final List<Interceptor> interceptors;

    private LogConfiguration(Builder builder) {
        level = builder.level;

        tag = builder.tag;

        withThread = builder.withThread;
        withStackTrace = builder.withStackTrace;
        stackTraceOrigin = builder.stackTraceOrigin;
        stackTraceDepth = builder.stackTraceDepth;
        withBorder = builder.withBorder;

        jsonFormatter = builder.jsonFormatter;
        xmlFormatter = builder.xmlFormatter;
        throwableFormatter = builder.throwableFormatter;
        threadFormatter = builder.threadFormatter;
        stackTraceFormatter = builder.stackTraceFormatter;
        borderFormatter = builder.borderFormatter;

        objectFormatters = builder.objectFormatters;

        interceptors = builder.interceptors;
    }

    public int getLevel() {
        return level;
    }

    public String getTag() {
        return tag;
    }

    public boolean isWithThread() {
        return withThread;
    }

    public boolean isWithStackTrace() {
        return withStackTrace;
    }

    public String getStackTraceOrigin() {
        return stackTraceOrigin;
    }

    public int getStackTraceDepth() {
        return stackTraceDepth;
    }

    public boolean isWithBorder() {
        return withBorder;
    }

    @NotNull
    public JsonFormatter getJsonFormatter() {
        return jsonFormatter;
    }

    @NotNull
    public XmlFormatter getXmlFormatter() {
        return xmlFormatter;
    }

    @NotNull
    public ThreadFormatter getThreadFormatter() {
        return threadFormatter;
    }

    @NotNull
    public ThrowableFormatter getThrowableFormatter() {
        return throwableFormatter;
    }

    @NotNull
    public StackTraceFormatter getStackTraceFormatter() {
        return stackTraceFormatter;
    }

    @NotNull
    public BorderFormatter getBorderFormatter() {
        return borderFormatter;
    }

    @Nullable
    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    @Nullable
    public <T> ObjectFormatter<? super T> getObjectFormatter(T obj) {
        Class<? super T> clazz;
        // noinspection unchecked
        Class<? super T> superClazz = (Class<? super T>) obj.getClass();
        ObjectFormatter<? super T> formatter;
        do {
            clazz = superClazz;
            // noinspection unchecked
            formatter = (ObjectFormatter<? super T>) objectFormatters.get(clazz);
            superClazz = clazz.getSuperclass();
        } while (formatter == null && superClazz != null);

        return formatter;
    }

    /*package*/ boolean isLoggable(int level) {
        return level >= this.level;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static final class Builder {

        private static final int DEFAULT_LOG_LEVEL = LogLevel.ALL;

        private static final String DEFAULT_TAG = "Logger";

        private static final int DEFAULT_STACKTRACE_DEPTH = 0;

        private int level = DEFAULT_LOG_LEVEL;

        private String tag = DEFAULT_TAG;

        private boolean withThread;

        private boolean withStackTrace;

        private String stackTraceOrigin;

        private int stackTraceDepth = DEFAULT_STACKTRACE_DEPTH;

        private boolean withBorder;

        private JsonFormatter jsonFormatter;

        private XmlFormatter xmlFormatter;

        private ThrowableFormatter throwableFormatter;

        private ThreadFormatter threadFormatter;

        private StackTraceFormatter stackTraceFormatter;

        private BorderFormatter borderFormatter;

        private Map<Class<?>, ObjectFormatter<?>> objectFormatters;

        private List<Interceptor> interceptors;

        public Builder() {
        }

        public Builder(@NotNull LogConfiguration logConfiguration) {
            level = logConfiguration.level;

            tag = logConfiguration.tag;

            withThread = logConfiguration.withThread;
            withStackTrace = logConfiguration.withStackTrace;
            stackTraceOrigin = logConfiguration.stackTraceOrigin;
            stackTraceDepth = logConfiguration.stackTraceDepth;
            withBorder = logConfiguration.withBorder;

            jsonFormatter = logConfiguration.jsonFormatter;
            xmlFormatter = logConfiguration.xmlFormatter;
            throwableFormatter = logConfiguration.throwableFormatter;
            threadFormatter = logConfiguration.threadFormatter;
            stackTraceFormatter = logConfiguration.stackTraceFormatter;
            borderFormatter = logConfiguration.borderFormatter;

            objectFormatters = new HashMap<>(logConfiguration.objectFormatters);

            if (logConfiguration.interceptors != null) {
                interceptors = new ArrayList<>(logConfiguration.interceptors);
            }
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder withThread(boolean with) {
            this.withThread = with;
            return this;
        }

        public Builder withStackTrace(boolean with) {
            this.withStackTrace = with;
            return this;
        }

        public Builder stackTraceOrigin(String origin) {
            this.stackTraceOrigin = origin;
            return this;
        }

        public Builder stackTraceDepth(int depth) {
            this.stackTraceDepth = depth;
            return this;
        }

        public Builder withBorder(boolean with) {
            this.withBorder = with;
            return this;
        }

        public Builder jsonFormatter(JsonFormatter jsonFormatter) {
            this.jsonFormatter = jsonFormatter;
            return this;
        }

        public Builder xmlFormatter(XmlFormatter xmlFormatter) {
            this.xmlFormatter = xmlFormatter;
            return this;
        }

        public Builder throwableFormatter(ThrowableFormatter throwableFormatter) {
            this.throwableFormatter = throwableFormatter;
            return this;
        }

        public Builder threadFormatter(ThreadFormatter threadFormatter) {
            this.threadFormatter = threadFormatter;
            return this;
        }

        public Builder stackTraceFormatter(StackTraceFormatter stackTraceFormatter) {
            this.stackTraceFormatter = stackTraceFormatter;
            return this;
        }

        public Builder borderFormatter(BorderFormatter borderFormatter) {
            this.borderFormatter = borderFormatter;
            return this;
        }

        public <T> Builder addObjectFormatter(Class<T> objectClass,
                                              ObjectFormatter<? super T> objectFormatter) {
            if (objectFormatters == null) {
                objectFormatters = new HashMap<>(DefaultsFactory.builtinObjectFormatters());
            }
            objectFormatters.put(objectClass, objectFormatter);
            return this;
        }

        public Builder objectFormatters(Map<Class<?>, ObjectFormatter<?>> objectFormatters) {
            this.objectFormatters = objectFormatters;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            interceptors.add(interceptor);
            return this;
        }

        public Builder interceptors(List<Interceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public LogConfiguration build() {
            fillEmptyFields();
            return new LogConfiguration(this);
        }

        private void fillEmptyFields() {
            if (jsonFormatter == null) {
                jsonFormatter = DefaultsFactory.createJsonFormatter();
            }
            if (xmlFormatter == null) {
                xmlFormatter = DefaultsFactory.createXmlFormatter();
            }
            if (throwableFormatter == null) {
                throwableFormatter = DefaultsFactory.createThrowableFormatter();
            }
            if (threadFormatter == null) {
                threadFormatter = DefaultsFactory.createThreadFormatter();
            }
            if (stackTraceFormatter == null) {
                stackTraceFormatter = DefaultsFactory.createStackTraceFormatter();
            }
            if (borderFormatter == null) {
                borderFormatter = DefaultsFactory.createBorderFormatter();
            }
            if (objectFormatters == null) {
                objectFormatters = new HashMap<>(DefaultsFactory.builtinObjectFormatters());
            }
        }
    }
}
