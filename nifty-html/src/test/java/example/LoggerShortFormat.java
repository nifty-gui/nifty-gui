package example;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerShortFormat extends java.util.logging.Formatter {
  // inefficient implementation
  public String format(LogRecord record) {
    Throwable throwable = record.getThrown();
    if (throwable != null) {
      throwable.printStackTrace();
    }
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
        ((ConsoleHandler)handlers[i]).setFormatter(new LoggerShortFormat());
        ((ConsoleHandler)handlers[i]).setLevel(Level.OFF);
      }
    }

    Logger.getLogger("NiftyEventBusLog").setLevel(Level.ALL);
    Logger.getLogger("org.bushe.swing.event.Logger").setLevel(Level.WARNING);
    Logger.getLogger("de.lessvoid.nifty").setLevel(Level.WARNING);
    Logger.getLogger("de.lessvoid.nifty.effects.EffectProcessorImpl").setLevel(Level.WARNING);
    Logger.getLogger("de.lessvoid.xml.xpp3.XmlParser").setLevel(Level.WARNING);
    Logger.getLogger("de.lessvoid.nifty.NiftyMethodInvoker").setLevel(Level.WARNING);
    Logger.getLogger("de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice").setLevel(Level.WARNING);
    Logger.getLogger("de.lessvoid.nifty.Nifty").setLevel(Level.WARNING);
    Logger.getLogger("de.lessvoid.nifty.screen.Screen").setLevel(Level.WARNING);
  }
}
