package com.guodong.android.logger.flattener;

import com.guodong.android.logger.Logger;

/**
 * Created by guodongAndroid on 2024/5/7 11:25.
 */
public final class ClassicFlattener extends PatternFlattener {

  private static final String DEFAULT_PATTERN = "{d} {l}/{t}: {m}";

  public ClassicFlattener() {
    super(DEFAULT_PATTERN);
  }
}