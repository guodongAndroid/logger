package com.guodong.android.logger.printer;

import com.guodong.android.logger.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by guodongAndroid on 2024/5/7 14:06.
 */
public final class CompositePrinter implements Printer {

    private final CopyOnWriteArrayList<Printer> writers;

    public CompositePrinter(@NotNull Printer... printers) {
        this(Arrays.asList(printers));
    }

    public CompositePrinter(@NotNull List<Printer> writers) {
        this.writers = new CopyOnWriteArrayList<>(writers);
    }

    public void addPrinter(Printer... printers) {
        for (Printer printer : printers) {
            if (!this.writers.contains(printer)) {
                this.writers.add(printer);
            }
        }
    }

    public void removePrinter(Printer printer) {
        this.writers.remove(printer);
    }

    @Override
    public void println(Log log) {
        for (Printer printer : writers) {
            printer.println(log);
        }
    }
}
