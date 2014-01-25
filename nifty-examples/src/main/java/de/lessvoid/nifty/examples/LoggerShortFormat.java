package de.lessvoid.nifty.examples;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import javax.annotation.Nonnull;

public class LoggerShortFormat extends java.util.logging.Formatter {
  @Override
  @Nonnull
  public String format(@Nonnull final LogRecord record) {
    return record.getMillis() + " " +
        record.getLevel() + " [" +
        record.getSourceClassName() + "] " +
        record.getMessage() +
        (record.getThrown() != null ? " Stack trace:\n" + getStackTrace(record.getThrown()) : "") + "\n";
  }

  public static void intialize() throws Exception {
    InputStream input = null;
    try {
      input = LoggerShortFormat.class.getClassLoader().getResourceAsStream("logging.properties");
      LogManager.getLogManager().readConfiguration(input);
    } finally {
      if (input != null) {
        input.close();
      }
    }
  }

  // Internal implementations

  private String getStackTrace(@Nonnull final Throwable throwable) {
    Writer stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    throwable.printStackTrace(printWriter);
    return stringWriter.toString();
  }
}
