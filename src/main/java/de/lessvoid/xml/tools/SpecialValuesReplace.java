package de.lessvoid.xml.tools;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marc Pompl
 * @author void (added some additional features)
 * @created 12.06.2010
 */
public class SpecialValuesReplace {
  private static final String KEY_PROP = "PROP.";
  private static final String KEY_ENV = "ENV.";
  private static final String KEY_CALL = "CALL.";

  private static Logger log = Logger.getLogger(SpecialValuesReplace.class.getName());

  /**
   * Tries to replace values surrounded by "${...}". All pieces of the input that are not
   * part of a "${...}" expression are returned unmodified. Replacement is executed for
   * all "${...}" values contained in the input. Yes, this means there is support for
   * multiple "${...}" expressions in input! :)<br>
   * <br>
   * <b>Example inputs:</b>
   * <ul>
   * <li><code>${ENV.myEnv}</code> checks and returns if <code>myEnv</code> exists in
   * {@link System.getEnv()}.</li>
   *
   * <li><code>${PROP.myProp}</code> checks and returns if </ode>myProp</code> exists in
   * the given properties. If properties is <code>null</code>, {@link System.getProperties()}
   * is checked instead.</li
   * 
   * <li><code>${CALL.myMethod()}</code> calls the method <code>myMethod()</code> on the
   * given <code>object</code> if it's not <code>null</code></li>
   * </ul>
   *
   * <li><code>${resourceBundleId.key}</code> tries to find the ResourceBundle with the
   * given <code>id</code> on the given list of ResourceBundles. And then calls 
   * <code>resourceBundle.get(key)</code> to translate the value.</li>
   * </ul>
   * 
   * @param input
   *          (may be <code>null</code>)
   * @param resourceBundles
   *          Map of pre loaded ResourceBundles with a String id
   * @param methodCallTarget
   *          if the input contains ${CALL...} the target object to call the method with
   * @param properties
   *          if the input contains ${PROP...} the properties to use (may be <code>null</code> in this case System.getProperties() are used)
   * 
   * @return the parsed input
   */
  public static String replace(final String input, final Map<String, ResourceBundle> resourceBundles, final Object methodCallTarget, final Properties properties) {
    List<String> parts = Split.split(input);
    for (int idx=0; idx<parts.size(); idx++) {
      String part = parts.get(idx);
      String prev = getPrev(parts, idx);
      if (isSpecialTag(part, prev)) {
        String value = removeQuotes(part);
        if (value.startsWith(KEY_ENV)) {
          parts.set(idx, handleENV(part));
        } else if (value.startsWith(KEY_PROP)) {
          parts.set(idx, handleProperties(part, properties));
        } else if (value.startsWith(KEY_CALL)) {
          parts.set(idx, handleCall(part, methodCallTarget));
        } else {
          parts.set(idx, handleLocalize(part, resourceBundles));
        }
      } else {
        if (endsWithQuote(prev)) {
          parts.set(idx-1, prev.substring(0, prev.length()-1));
        }
      }
    }
    String result = Split.join(parts);
    if (log.isLoggable(Level.FINER)) {
      log.finer(MessageFormat.format("Parsed input \"{0}\" to \"{1}\"", input, result));
    }
    return result;
  }

  private static boolean endsWithQuote(String prev) {
    return prev != null && prev.endsWith("\\");
  }

  private static String getPrev(final List<String> parts, final int idx) {
    if (idx == 0) {
      return null;
    }
    return parts.get(idx - 1);
  }

  private static String removeQuotes(final String input) {
    return input.substring(2, input.length()-1);
  }

  private static boolean isSpecialTag(final String input, final String prev) {
    boolean isSpecialTag = input != null && input.startsWith("${") && input.endsWith("}");
    if (!isSpecialTag) {
      return false;
    }
    if (endsWithQuote(prev)) {
      return false;
    }
    return true;
  }
  

  private static String handleENV(final String value) {
    String name = removeQuotes(value).substring(KEY_ENV.length());
    if (System.getenv().containsKey(name)) {
      String env = System.getenv().get(name);
      if (env != null && env.length() > 0) {
        return env;
      }
    }
    return value;
  }

  private static String handleProperties(final String input, final Properties properties) {
    String name = removeQuotes(input).substring(KEY_PROP.length());
    String value = readFromProperties(name, properties);
    if (value == null) {
      value = readFromProperties(name, System.getProperties());
    }
    if (value != null) {
      return value;
    }
    return input;
  }

  private static String readFromProperties(final String name, final Properties properties) {
    if (properties != null) {
      if (properties.containsKey(name)) {
        String value = properties.getProperty(name);
        if (value != null && value.length() > 0) {
          return value;
        }
      }
    }
    return null;
  }

  private static String handleCall(final String value, final Object object) {
    if (object != null) {
      String methodName = removeQuotes(value).substring(KEY_CALL.length());
      MethodInvoker methodInvoker = new MethodInvoker(methodName, object);
      Object response = methodInvoker.invoke();
      if (response != null) {
        return response.toString();
      }
    }
    return value;
  }

  private static String handleLocalize(final String value, final Map<String, ResourceBundle> resourceBundles) {
    if (value.indexOf(".") != -1) {
      String removedQuotes = removeQuotes(value);
      String resourceSelector = removedQuotes.substring(0, removedQuotes.indexOf("."));
      String resourceKey = removedQuotes.substring(removedQuotes.indexOf(".") + 1);
      ResourceBundle res = resourceBundles.get(resourceSelector);
      if (res != null) {
          try {
              return res.getString(resourceKey);
          } catch(MissingResourceException e) {
              if (log.isLoggable(Level.WARNING)) {
                  log.warning("Missing resource: " + resourceSelector + "." + resourceKey);
                }
              return "<" + resourceKey + ">";
          }
      }
    }
    return value;
  }
}
