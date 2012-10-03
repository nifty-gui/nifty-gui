package de.lessvoid.nifty.examples;

import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class LoggerShortFormat extends java.util.logging.Formatter {
  public String format(final LogRecord record) {
    return record.getMillis() + " " +
        record.getLevel() + " [" +
        record.getSourceClassName() + "] " +
        record.getMessage() + "\n";
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
}
