package de.lessvoid.nifty.examples;

import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class LoggerShortFormat extends java.util.logging.Formatter {
  public String format(final LogRecord record) {
     return
       record.getMillis() + " " +
       record.getLevel() + " [" +
       record.getSourceClassName() + "] " +
       record.getMessage() + "\n";
  }
  
  public static void intialize() {
    try {
      LogManager.getLogManager().readConfiguration(LoggerShortFormat.class.getClassLoader().getResourceAsStream("logging.properties"));
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
