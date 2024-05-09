package com.guodong.android.logger.printer;

import com.guodong.android.logger.Log;
import com.guodong.android.logger.flattener.Flattener;
import com.guodong.android.logger.internal.DefaultsFactory;

/**
 * Created by guodongAndroid on 2024/5/7 14:10.
 */
public final class ConsolePrinter implements Printer {

    private final Flattener flattener;

    public ConsolePrinter() {
        this(DefaultsFactory.createFlattener());
    }

    public ConsolePrinter(Flattener flattener) {
        this.flattener = flattener;
    }

    @Override
    public void println(Log log) {
        CharSequence flattened = flattener.flatten(log);
        System.out.println(flattened);
    }
}
