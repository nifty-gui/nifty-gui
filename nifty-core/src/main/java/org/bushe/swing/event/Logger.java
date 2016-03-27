package org.bushe.swing.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

/**
 * Central Logging class.  Shields code from Logging implementation.
 * <p/>
 * The EventBus allows operation in two modes - using java.util.logging so that
 * the EventBus can be deployed in its own jar or using any logging system supported
 * by apache commons logging, which of course requires other jars.
 * <p/>
 * The EventBus logging uses the names of its classes as the log, primarily
 * "org.bushe.swing.event.EventService".  This aids in debugging which subscription and publication issues.
 * <p/>
 * Implementation note: There are no imports in this class to make things
 * explicit.  There is also no explicit use of classes outside java.util,
 * anything else is used by reflection to avoid NoClassDefFound errors on class load.
 */
public class Logger {
   private java.util.logging.Logger utilLogger;
   private /*Untyped to avoid java.lang.NoClassDefFoundError
   org.apache.commons.logging.Log*/ Object commonsLogger;
   private Map<String, Method> METHOD_CACHE_NO_PARAMS;
   private Map<String, Method> METHOD_CACHE_ONE_PARAM;
   private Map<String, Method> METHOD_CACHE_TWO_PARAMS;
   private static Class<?> logFactoryClass;
   private static Class<?> logClass;
   private static Method getLogMethod;
   private static final Object[] EMPTY_ARGS = new Object[0];
   private static final Class[] CLASS_ARGS_EMPTY = new Class[0];
   private static final Class[] CLASS_ARGS_ONE = new Class[]{Object.class};
   private static final Class[] CLASS_ARGS_TWO = new Class[]{Object.class, Throwable.class};

   /** Allows switching between Java and Commons logging.*/
   public static enum LoggerType {
      /*java.util.logging*/
      JAVA,
      /*org.apache.commons.logging*/
      COMMONS
   }

   /** Standardized logging levels. */
   public static enum Level {
      FATAL,
      ERROR,
      WARN,
      INFO,
      DEBUG,
      TRACE
   }

   public static LoggerType LOGGER_TYPE= null;

   public static Logger getLogger(String name) {
      if (LOGGER_TYPE == null) {
         LOGGER_TYPE = getLoggerType();
      }
      if (LOGGER_TYPE == LoggerType.COMMONS) {
            try {
               Object logger = getLogMethod.invoke(null, name);
               return new Logger(logger);
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            } catch (InvocationTargetException e) {
               e.printStackTrace();
            }
      }
      return new Logger(java.util.logging.Logger.getLogger(name));
   }

   /**
    * This method should only be called once in a JVM run.
    * @return
    */
   private static LoggerType getLoggerType() {
      LoggerType result = null;
      //See if apache commons is available
      try {
         logFactoryClass = Class.forName("org.apache.commons.logging.LogFactory");
         getLogMethod = logFactoryClass.getMethod("getLog", new Class[]{String.class});
         logClass = Class.forName("org.apache.commons.logging.Log");
         return LoggerType.COMMONS;
      } catch (Throwable e) {
      }
      return LoggerType.JAVA;
   }

   public Logger(java.util.logging.Logger utilLogger) {
      this.utilLogger = utilLogger;
   }

   public Logger(Object commonsLogger) {
      this.commonsLogger = commonsLogger;
   }

   /**
    * Returns whether this level is loggable.  If there is
    * a misconfiguration, this will always return false.
    * @param level the EventBus Logger level
    * @return whether this level is loggable.
    */
   public boolean isLoggable(Level level) {
      if (utilLogger != null) {
         java.util.logging.Level javaLevel = getJavaLevelFor(level);
         return javaLevel != null && utilLogger.isLoggable(javaLevel);
      } else if (commonsLogger != null) {
         switch (level) {
            case ERROR: return (Boolean)callCommonsLogger("isErrorEnabled");
            case FATAL: return (Boolean)callCommonsLogger("isFatalEnabled");
            case WARN: return (Boolean)callCommonsLogger("isWarnEnabled");
            case INFO: return (Boolean)callCommonsLogger("isInfoEnabled");
            case DEBUG: return (Boolean)callCommonsLogger("isDebugEnabled");
            case TRACE: return (Boolean)callCommonsLogger("isTraceEnabled");
         }
      }
      return false;
   }

   private java.util.logging.Level getJavaLevelFor(Level level) {
      switch (level) {
         case FATAL: return java.util.logging.Level.SEVERE;
         case ERROR: return java.util.logging.Level.SEVERE;
         case WARN: return java.util.logging.Level.WARNING;
         case INFO: return java.util.logging.Level.INFO;
         case DEBUG: return java.util.logging.Level.FINE;
         case TRACE: return java.util.logging.Level.FINEST;
      }
      return null;
   }

   public void debug(String message) {
      log(Level.DEBUG, message);
   }

   public void log(Level level, String message) {
      log(level, message, null);
   }

   public void log(Level level, String message, Throwable throwable) {
      if (!isLoggable(level)) {
         return;
      }
      if (utilLogger != null) {
         java.util.logging.Level javaLevel = getJavaLevelFor(level);
         if (throwable == null) {
            utilLogger.log(javaLevel, message);
         } else {
            utilLogger.log(javaLevel, message, throwable);
         }
      } else if (commonsLogger != null) {
         if (throwable == null) {
            switch (level) {
               case ERROR: callCommonsLogger("error", message); break;
               case FATAL: callCommonsLogger("fatal", message); break;
               case WARN: callCommonsLogger("warn", message); break;
               case INFO: callCommonsLogger("info", message); break;
               case DEBUG: callCommonsLogger("debug", message); break;
               case TRACE: callCommonsLogger("trace", message); break;
            }
         } else {
            switch (level) {
               case ERROR: callCommonsLogger("error", message, throwable); break;
               case FATAL: callCommonsLogger("fatal", message, throwable); break;
               case WARN: callCommonsLogger("warn", message, throwable); break;
               case INFO: callCommonsLogger("info", message, throwable); break;
               case DEBUG: callCommonsLogger("debug", message, throwable); break;
               case TRACE: callCommonsLogger("trace", message, throwable); break;
            }
         }
      }
   }

   private Object callCommonsLogger(String methodName) {
      if (METHOD_CACHE_NO_PARAMS == null) {
         METHOD_CACHE_NO_PARAMS = new HashMap<String, Method>();
      }
      return callCommonsLogger(METHOD_CACHE_NO_PARAMS, methodName, CLASS_ARGS_EMPTY, EMPTY_ARGS);
   }

   private Object callCommonsLogger(String methodName, String message) {
      if (METHOD_CACHE_ONE_PARAM == null) {
         METHOD_CACHE_ONE_PARAM = new HashMap<String, Method>();
      }
      return callCommonsLogger(METHOD_CACHE_ONE_PARAM, methodName, CLASS_ARGS_ONE, new Object[]{message});
   }

   private Object callCommonsLogger(String methodName, String message, Throwable throwable) {
      if (METHOD_CACHE_TWO_PARAMS == null) {
         METHOD_CACHE_TWO_PARAMS = new HashMap<String, Method>();
      }
      return callCommonsLogger(METHOD_CACHE_TWO_PARAMS, methodName, CLASS_ARGS_TWO, new Object[]{message, throwable});
   }

   private Object callCommonsLogger(Map<String, Method> cache, String methodName, Class[] classOfArgs, Object[] args) {
      Method method = cache.get(methodName);
      if (method == null) {
         try {
            method = logClass.getMethod(methodName, classOfArgs);
            cache.put(methodName, method);
         } catch (NoSuchMethodException e) {
            e.printStackTrace();  
         }
      }
      if (method == null) {
         return null;
      }
      try {
         return method.invoke(commonsLogger, args);
      } catch (IllegalAccessException e) {
         return null;
      } catch (InvocationTargetException e) {
         return null;
      }
   }
}
