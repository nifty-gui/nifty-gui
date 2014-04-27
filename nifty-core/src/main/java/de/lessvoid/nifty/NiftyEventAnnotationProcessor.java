package de.lessvoid.nifty;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.EventTopicSubscriber;
import org.bushe.swing.event.ProxySubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;

public class NiftyEventAnnotationProcessor {
  private static final Logger log = Logger.getLogger(NiftyEventAnnotationProcessor.class.getName());

  public static void process(final Object obj) {
    processOrUnprocess(obj, true);
  }

  public static void unprocess(final Object obj) {
    processOrUnprocess(obj, false);
  }

  private static void processOrUnprocess(@Nullable final Object obj, final boolean add) {
    if (obj == null) {
      return;
    }
    Class<?> cl = obj.getClass();
    Method[] methods = cl.getMethods();
    if (log.isLoggable(Level.FINE)) {
      log.fine("Looking for EventBus annotations for class " + cl + ", methods:" + Arrays.toString(methods));
    }
    for (Method method : methods) {
      NiftyEventSubscriber niftyEventSubscriber = method.getAnnotation(NiftyEventSubscriber.class);
      if (niftyEventSubscriber != null) {
        if (log.isLoggable(Level.FINE)) {
          log.fine("Found NiftyEventSubscriber:" + niftyEventSubscriber + " on method:" + method);
        }
        process(niftyEventSubscriber, obj, method, add);
      }
    }
  }

  private static void process(@Nonnull final NiftyEventSubscriber annotation, final Object obj, @Nonnull final Method method, final boolean add) {
    String id = annotation.id();
    String pattern = annotation.pattern();
    ensureNotNull(id, pattern);
    ensureMethodParamCount(method.getParameterTypes());
    EventService eventService = getEventService();
    Class<?> eventClass = method.getParameterTypes()[1];
    if (isSet(id)) {
      idProcess(obj, method, add, id, eventClass, eventService);
    } else {
      patternProcess(obj, method, add, pattern, eventClass, eventService);
    }
  }

  private static boolean isSet(@Nullable final String value) {
    return value != null && value.length() > 0;
  }

  private static void ensureNotNull(final String id, final String pattern) {
    if (!isSet(id) && !isSet(pattern)) {
      throw new IllegalArgumentException("id or pattern must have a value for NiftyEventSubscriber annotation");
    }
  }

  private static void ensureMethodParamCount(@Nullable final Class<?>[] params) {
    if (params == null || params.length != 2 || !String.class.equals(params[0]) || params[1].isPrimitive()) {
       throw new IllegalArgumentException("The subscriptionMethod must have the two parameters, the first one must be a String and the second a non-primitive (Object or derivative).");
    }
  }

  private static void patternProcess(final Object obj, final Method method, final boolean add, @Nonnull final String topicPattern, final Class<?> eventClass, @Nonnull final EventService eventService) {
    Pattern pattern = Pattern.compile(topicPattern);
    if (add) {
      Subscriber subscriber = new Subscriber(obj, method, eventClass);
      eventService.subscribeStrongly(pattern, subscriber);
      log.fine("-> subscribe [" + pattern + "] -> [" + subscriber + "]");
    } else {
      eventService.unsubscribe(pattern, obj);
      log.fine("<- unsubscribe [" + pattern + "] -> [" + obj + "]");
    }
  }

  private static void idProcess(final Object obj, final Method method, final boolean add, final String id, final Class<?> eventClass, @Nonnull final EventService eventService) {
    if (add) {
      Subscriber subscriber = new Subscriber(obj, method, eventClass);
      eventService.subscribeStrongly(id, subscriber);
      log.fine("-> subscribe [" + id + "] -> [" + subscriber + "]");
    } else {
      eventService.unsubscribe(id, obj);
      log.fine("<- unsubscribe [" + id + "] -> [" + obj + "]");
    }
  }

  private static EventService getEventService() {
    return EventServiceLocator.getEventService("NiftyEventBus");
  }

  private static class Subscriber implements EventTopicSubscriber<Object>, ProxySubscriber {
    private final Object obj;
    private final Method method;
    private final Class<?> eventClass;

    private Subscriber(final Object obj, final Method method, final Class<?> eventClass) {
      this.obj = obj;
      this.method = method;
      this.eventClass = eventClass;
    }

    @Override
    public void onEvent(final String topic, final Object data) {
      if (eventClass.isInstance(data)) {
        try {
          method.invoke(obj, topic, eventClass.cast(data));
        } catch (Throwable e) {
          log.log(Level.WARNING, "failed to invoke method [" + method + "] with Exception [" + e.getMessage() + "][" + e.getCause() + "]", e);
        }
      }
    }

    @Override
    public Object getProxiedSubscriber() {
      return obj;
    }

    @Override
    public void proxyUnsubscribed() {
    }

    @Nonnull
    @Override
    public ReferenceStrength getReferenceStrength() {
      return ReferenceStrength.WEAK;
    }
  }
}
