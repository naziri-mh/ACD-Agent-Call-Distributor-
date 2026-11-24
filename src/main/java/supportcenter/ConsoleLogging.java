package supportcenter;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/** Sets up java.util.logging to print plain one-line messages to stdout. */
public final class ConsoleLogging {

    private ConsoleLogging() {
    }

    public static void configure() {
        Logger root = Logger.getLogger("");
        for (Handler handler : root.getHandlers()) {
            root.removeHandler(handler);
        }
        Handler handler = new StdoutHandler();
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
                return logRecord.getMessage() + System.lineSeparator();
            }
        });
        root.addHandler(handler);
    }

    /** Writes to stdout in UTF-8 and flushes after every record. */
    private static final class StdoutHandler extends StreamHandler {
        StdoutHandler() {
            setOutputStream(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        }

        @Override
        public synchronized void publish(LogRecord logRecord) {
            super.publish(logRecord);
            flush();
        }
    }
}