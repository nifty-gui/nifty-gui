package de.lessvoid.nifty.examples.controls;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerShortFormat extends java.util.logging.Formatter {
  // inefficient implementation
  public String format(final LogRecord record) {
     return
       record.getMillis() + " " +
       record.getLevel() + " [" +
       record.getSourceClassName() + "] " +
       record.getMessage() + "\n";
  }
  
  public static void intialize() {
    Logger root = Logger.getLogger("");
    Handler[] handlers = root.getHandlers();  // returns 1

    for (int i = 0; i < handlers.length; i++) {
      if (handlers[i] instanceof ConsoleHandler) {
        ((ConsoleHandler) handlers[i]).setFormatter(new LoggerShortFormat());
        ((ConsoleHandler) handlers[i]).setLevel(Level.ALL);
      }
    }

    Logger.getLogger("de.lessvoid").setLevel(Level.WARNING);
    Logger.getLogger("de.lessvoid.nifty.screen.Screen").setLevel(Level.WARNING);
//    Logger.getLogger("org.bushe.swing.event.EventService").setLevel(Level.ALL);
//    Logger.getLogger("de.lessvoid.nifty.examples.listbox.NiftyAnnotationProcessor").setLevel(Level.ALL);
    
//    Logger.getLogger("de.lessvoid.nifty.Nifty").setLevel(Level.FINE);
  }
}
