package de.lessvoid.niftyinternal.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

/**
 * This is a special loader for services that ignores access restriction to service classes.
 * It also excludes some facilities that are not relevant for Nifty.
 *
 * Aside from that is is largely based on {@link java.util.ServiceLoader<S>}
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class NiftyServiceLoader<S> implements Iterable<S> {
  @Nonnull
  private static final String PREFIX = "META-INF/services/";
  @Nonnull
  private final Map<String, S> providers;

  private NiftyServiceLoader(@Nonnull Class<S> service, @Nullable ClassLoader cl) {
    providers= performLoading(service, ((cl == null) ? ClassLoader.getSystemClassLoader() : cl));
  }

  private static <S> Map<String, S> performLoading(@Nonnull final Class<S> service,
                                                   @Nonnull final ClassLoader loader) {
    String fullName = PREFIX + service.getName();
    Enumeration<URL> configs = null;
    try {
      configs = loader.getResources(fullName);
    } catch (IOException e) {
      fail(service, "Loading service files failed.");
    }

    Map<String, S> providers = new HashMap<>();
    while (configs.hasMoreElements()) {
      URL nextURL = configs.nextElement();

      for (String configLine : parse(service, nextURL)) {
        if (!providers.containsKey(configLine)) {
          S newService = createService(service, loader, configLine);
          providers.put(configLine, newService);
        }
      }
    }
    return providers;
  }

  private static int parseLine(@Nonnull final Class<?> service,
                               @Nonnull final URL u,
                               @Nonnull final BufferedReader r,
                               final int lc,
                               @Nonnull final List<String> names) throws IOException, ServiceConfigurationError {
    String ln = r.readLine();
    if (ln == null) {
      return -1;
    }
    int ci = ln.indexOf('#');
    if (ci >= 0) {
      ln = ln.substring(0, ci);
    }
    ln = ln.trim();
    int n = ln.length();
    if (n != 0) {
      if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0)) {
        fail(service, u, lc, "Illegal configuration-file syntax");
      }
      int cp = ln.codePointAt(0);
      if (!Character.isJavaIdentifierStart(cp)) {
        fail(service, u, lc, "Illegal provider-class name: " + ln);
      }
      for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
        cp = ln.codePointAt(i);
        if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) {
          fail(service, u, lc, "Illegal provider-class name: " + ln);
        }
      }
      if (!names.contains(ln)) {
        names.add(ln);
      }
    }
    return lc + 1;
  }

  @Nonnull
  private static Iterable<String> parse(@Nonnull final Class<?> service,
                                        @Nonnull final URL u) throws ServiceConfigurationError {
    InputStream in = null;
    BufferedReader r = null;
    ArrayList<String> names = new ArrayList<>();
    try {
      in = u.openStream();
      r = new BufferedReader(new InputStreamReader(in, "utf-8"));
      int lc = 1;
      //noinspection StatementWithEmptyBody
      while ((lc = parseLine(service, u, r, lc, names)) >= 0);
    } catch (IOException x) {
      fail(service, "Error reading configuration file", x);
    } finally {
      try {
        if (r != null) {
          r.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException y) {
        fail(service, "Error closing configuration file", y);
      }
    }
    return names;
  }

  private static <S> S createService(@Nonnull final Class<S> service,
                                     @Nonnull final ClassLoader loader,
                                     @Nonnull final String serviceName) {
    Class<?> c = null;
    try {
      c = Class.forName(serviceName, false, loader);
    } catch (ClassNotFoundException x) {
      fail(service, "Provider " + serviceName + " not found");
    }
    if (!service.isAssignableFrom(c)) {
      fail(service, "Provider " + serviceName  + " not a subtype");
    }
    try {
      Constructor<?> constructor = c.getDeclaredConstructor();
      if (!constructor.isAccessible()) {
        constructor.setAccessible(true);
      }
      return service.cast(constructor.newInstance());
    } catch (Throwable x) {
      fail(service, "Provider " + serviceName + " could not be instantiated", x);
    }
    throw new Error();          // This cannot happen
  }

  private static void fail(@Nonnull final Class<?> service,
                           @Nonnull final String msg,
                           @Nullable final Throwable cause) throws ServiceConfigurationError {
    throw new ServiceConfigurationError(service.getName() + ": " + msg, cause);
  }

  private static void fail(@Nonnull final Class<?> service,
                           @Nonnull final String msg) throws ServiceConfigurationError {
    throw new ServiceConfigurationError(service.getName() + ": " + msg);
  }

  private static void fail(@Nonnull final Class<?> service,
                           @Nonnull final URL u,
                           final int line,
                           @Nonnull final String msg) throws ServiceConfigurationError {
    fail(service, u + ":" + line + ": " + msg);
  }

  @Override
  public Iterator<S> iterator() {
    return providers.values().iterator();
  }

  public static <S> NiftyServiceLoader<S> load(@Nonnull final Class<S> service) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    return new NiftyServiceLoader<>(service, cl);
  }
}
