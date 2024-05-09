package com.guodong.android.logger.internal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.guodong.android.logger.formatter.object.BundleFormatter;
import com.guodong.android.logger.formatter.object.IntentFormatter;
import com.guodong.android.logger.formatter.object.ObjectFormatter;
import com.guodong.android.logger.printer.AndroidPrinter;
import com.guodong.android.logger.printer.ConsolePrinter;
import com.guodong.android.logger.printer.Printer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Platform {

  private static final Platform PLATFORM = findPlatform();

  public static Platform get() {
    return PLATFORM;
  }

  @SuppressLint("NewApi")
  String lineSeparator() {
    return System.lineSeparator();
  }

  Printer defaultPrinter() {
    return new ConsolePrinter();
  }

  Map<Class<?>, ObjectFormatter<?>> builtinObjectFormatters() {
    return Collections.emptyMap();
  }

  public void warn(String msg) {
    System.out.println(msg);
  }

  public void error(String msg) {
    System.out.println(msg);
  }

  private static Platform findPlatform() {
    try {
      Class.forName("android.os.Build");
      if (Build.VERSION.SDK_INT != 0) {
        return new Android();
      }
    } catch (ClassNotFoundException ignored) {
    }
    return new Platform();
  }

  static class Android extends Platform {

    private static final Map<Class<?>, ObjectFormatter<?>> BUILTIN_OBJECT_FORMATTERS;

    static {
      Map<Class<?>, ObjectFormatter<?>> objectFormatters = new HashMap<>();
      objectFormatters.put(Bundle.class, new BundleFormatter());
      objectFormatters.put(Intent.class, new IntentFormatter());
      BUILTIN_OBJECT_FORMATTERS = Collections.unmodifiableMap(objectFormatters);
    }

    @Override
    String lineSeparator() {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return "\n";
      }
      return System.lineSeparator();
    }

    @Override
    Printer defaultPrinter() {
      return new AndroidPrinter();
    }

    @Override
    Map<Class<?>, ObjectFormatter<?>> builtinObjectFormatters() {
      return BUILTIN_OBJECT_FORMATTERS;
    }

    @Override
    public void warn(String msg) {
      android.util.Log.w("Logger", msg);
    }

    @Override
    public void error(String msg) {
      android.util.Log.e("Logger", msg);
    }
  }
}
