package org.bushe.swing.event.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.Arrays;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.Logger;

/**
 * Enhances classes that use EventService Annotations.
 * <p/>
 * This class makes the EventService annotations "come alive."  This can be used in code like so:
 * <pre>
 * <code>
 * public class MyAppController {
 *   public MyAppController {
 *       AnnotationProcessor.process(this);//this line can be avoided with a compile-time tool or an Aspect
 *   }
 *   &#64;EventSubscriber
 *   public void onAppStartingEvent(AppStartingEvent appStartingEvent) {
 *      //do something
 *   }
 *   &#64;EventSubscriber
 *   public void onAppClosingEvent(AppClosingEvent appClosingEvent) {
 *      //do something
 *   }
 * }
 * ... some other place, needed in some cases when the like a window disposal before it's garbage collected ....
 * AnnotationProcessor.unprocess(this);
 * </code>
 * </pre>
 * <p/>
 * This class can be leveraged in outside of source code in other ways in which Annotations are used: <ul> <li>In an
 * Aspect-Oriented tool <li>In a Swing Framework classloader that wants to load and understand events. <li>In other
 * Inversion of Control containers, such as Spring or PicoContainer. <li>In the apt tool, though this does not generate
 * code. <li>In a Annotation Processing Tool plugin, when it becomes available. </ul> Support for these other methods
 * are not yet implemented.
 */
public class AnnotationProcessor {

   protected static final Logger LOG = Logger.getLogger(EventService.class.getName());

   /**
    * Add the appropriate subscribers to one or more EventServices for an instance of a class with
    * EventBus annotations.
    * @param obj the instance that may or may not have annotations
    */
   public static void process(Object obj) {
      processOrUnprocess(obj, true);
   }

   /**
    * Remove the appropriate subscribers from one or more EventServices for an instance of a class with
    * EventBus annotations.
    * @param obj the instance that may or may not have annotations
    */
   public static void unprocess(Object obj) {
      processOrUnprocess(obj, false);
   }

   private static void processOrUnprocess(Object obj, boolean add) {
      if (obj == null) {
         return;
      }
      Class cl = obj.getClass();
      Method[] methods = cl.getMethods();
      if (LOG.isLoggable(Logger.Level.DEBUG)) {
        LOG.debug("Looking for EventBus annotations for class " + cl + ", methods:" + Arrays.toString(methods));
      }
      for (Method method : methods) {
         
         EventSubscriber classAnnotation = method.getAnnotation(EventSubscriber.class);
         if (classAnnotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found EventSubscriber:"+classAnnotation +" on method:" + method);
            }
            process(classAnnotation, obj, method, add);
         }
         EventTopicSubscriber topicAnnotation = method.getAnnotation(EventTopicSubscriber.class);
         if (topicAnnotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found EventTopicSubscriber: "+topicAnnotation +"  on method:" + method);
            }
            process(topicAnnotation, obj, method, add);
         }
         EventTopicPatternSubscriber topicPatternAnnotation = method.getAnnotation(EventTopicPatternSubscriber.class);
         if (topicPatternAnnotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found EventTopicPatternSubscriber: "+topicPatternAnnotation+" on method:" + method);
            }
            process(topicPatternAnnotation, obj, method, add);
         }
         RuntimeTopicEventSubscriber runtimeTopicAnnotation = method.getAnnotation(RuntimeTopicEventSubscriber.class);
         if (runtimeTopicAnnotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found RuntimeTopicEventSubscriber: "+runtimeTopicAnnotation+" on method:" + method);
            }
            process(runtimeTopicAnnotation, obj, method, add);
         }
         RuntimeTopicPatternEventSubscriber annotation = method.getAnnotation(RuntimeTopicPatternEventSubscriber.class);
         if (annotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found RuntimeTopicPatternEventSubscriber:"+annotation+" on method:" + method);
            }
            process(annotation, obj, method, add);
         }


         VetoSubscriber vetoClassAnnotation = method.getAnnotation(VetoSubscriber.class);
         if (vetoClassAnnotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found VetoSubscriber:"+vetoClassAnnotation +" on method:" + method);
            }
            process(vetoClassAnnotation, obj, method, add);
         }
         VetoTopicSubscriber vetoTopicAnnotation = method.getAnnotation(VetoTopicSubscriber.class);
         if (vetoTopicAnnotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found VetoTopicSubscriber: "+vetoTopicAnnotation +"  on method:" + method);
            }
            process(vetoTopicAnnotation, obj, method, add);
         }
         VetoTopicPatternSubscriber vetoTopicPatternAnnotation = method.getAnnotation(VetoTopicPatternSubscriber.class);
         if (vetoTopicPatternAnnotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found VetoTopicPatternSubscriber: "+vetoTopicPatternAnnotation+" on method:" + method);
            }
            process(vetoTopicPatternAnnotation, obj, method, add);
         }
         VetoRuntimeTopicSubscriber vetoRuntimeTopicAnnotation = method.getAnnotation(VetoRuntimeTopicSubscriber.class);
         if (vetoRuntimeTopicAnnotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found VetoRuntimeTopicSubscriber: "+vetoRuntimeTopicAnnotation+" on method:" + method);
            }
            process(vetoRuntimeTopicAnnotation, obj, method, add);
         }
         VetoRuntimeTopicPatternSubscriber vetoAnnotation = method.getAnnotation(VetoRuntimeTopicPatternSubscriber.class);
         if (vetoAnnotation != null) {
            if (LOG.isLoggable(Logger.Level.DEBUG)) {
               LOG.debug("Found VetoRuntimeTopicPatternSubscriber:"+vetoAnnotation+" on method:" + method);
            }
            process(vetoAnnotation, obj, method, add);
         }
      }
   }


   private static void process(EventTopicPatternSubscriber topicPatternAnnotation, Object obj,
           Method method, boolean add) {
      //Check args
      String topicPattern = topicPatternAnnotation.topicPattern();
      if (topicPattern == null) {
         throw new IllegalArgumentException("Topic pattern cannot be null for EventTopicPatternSubscriber annotation");
      }

      //Get event service
      Class<? extends EventService> eventServiceClass = topicPatternAnnotation.autoCreateEventServiceClass();
      String eventServiceName = topicPatternAnnotation.eventServiceName();
      EventService eventService = getEventServiceFromAnnotation(eventServiceName, eventServiceClass);
      int priority = topicPatternAnnotation.priority();

      //Create proxy and subscribe
      Pattern pattern = Pattern.compile(topicPattern);
      //See Issue #18
      //Also note that this post is wrong: https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
      //Since two WeakReferences are not treated as one.  So this always has to be strong and we'll have to clean up occasionally.
      if (add) {
         ProxyTopicPatternSubscriber subscriber = new ProxyTopicPatternSubscriber(obj, method,
                 topicPatternAnnotation.referenceStrength(), priority, eventService,
                 topicPattern, pattern, false);

         eventService.subscribeStrongly(pattern, subscriber);
      } else {
         eventService.unsubscribe(pattern, obj);
      }
   }

   private static void process(EventTopicSubscriber topicAnnotation, Object obj, Method method, boolean add) {
      //Check args
      String topic = topicAnnotation.topic();
      if (topic == null) {
         throw new IllegalArgumentException("Topic cannot be null for EventTopicSubscriber annotation");
      }

      //Get event service
      Class<? extends EventService> eventServiceClass = topicAnnotation.autoCreateEventServiceClass();
      String eventServiceName = topicAnnotation.eventServiceName();
      EventService eventService = getEventServiceFromAnnotation(eventServiceName, eventServiceClass);

      int priority = topicAnnotation.priority();

      //See Issue #18
      //Also note that this post is wrong: https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
      //Since two WeakReferences are not treated as one.  So this always has to be strong and we'll have to clean up occasionally.
      if (add) {
         //Create proxy and subscribe
         ProxyTopicSubscriber subscriber = new ProxyTopicSubscriber(obj, method,
                 topicAnnotation.referenceStrength(), priority, eventService, topic, false);

         eventService.subscribeStrongly(topic, subscriber);
      } else {
         eventService.unsubscribe(topic, obj);
      }
   }

   private static void process(EventSubscriber annotation, Object obj, Method method, boolean add) {
      //Check args
      Class eventClass = annotation.eventClass();
      if (eventClass == null) {
         throw new IllegalArgumentException("Event class cannot be null for EventSubscriber annotation");
      } else if (UseTheClassOfTheAnnotatedMethodsParameter.class.equals(eventClass)) {
         Class[] params = method.getParameterTypes();
         if (params.length < 1) {
            throw new RuntimeException("Expected annotated method to have one parameter.");
         } else {
            eventClass = params[0];
         }
      }

      //Get event service
      Class<? extends EventService> eventServiceClass = annotation.autoCreateEventServiceClass();
      String eventServiceName = annotation.eventServiceName();
      EventService eventService = getEventServiceFromAnnotation(eventServiceName, eventServiceClass);

      if (add) {
         int priority = annotation.priority();

         //Create proxy and subscribe
         //See https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
         BaseProxySubscriber subscriber = new BaseProxySubscriber(obj, method, annotation.referenceStrength(),
                 priority, eventService, eventClass, false);
         if (annotation.exact()) {
            //See Issue #18
            //Also note that this post is wrong: https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
            //Since two WeakReferences are not treated as one.  So this always has to be strong and we'll have to clean up occasionally.
            eventService.subscribeExactlyStrongly(eventClass, subscriber);
         } else {
            //See Issue #18
            //Also note that this post is wrong: https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
            //Since two WeakReferences are not treated as one.  So this always has to be strong and we'll have to clean up occasionally.
            eventService.subscribeStrongly(eventClass, subscriber);
         }
      } else {
         if (annotation.exact()) {
            eventService.unsubscribeExactly(eventClass, obj);
         } else {
            eventService.unsubscribe(eventClass, obj);            
         }
      }
   }



   private static void process(final RuntimeTopicEventSubscriber annotation, final Object subscriber, final Method method, boolean add) {
       EventTopicSubscriber eventTopicSubscriber = new EventTopicSubscriber() {
           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public Class<? extends EventService> autoCreateEventServiceClass() {
               return annotation.autoCreateEventServiceClass();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public String eventServiceName() {
               return annotation.eventServiceName();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public ReferenceStrength referenceStrength() {
               return annotation.referenceStrength();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
			  public int priority() {
				return annotation.priority();
			  }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public String topic() {
               return getTopic(annotation.methodName(), subscriber, method);
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public Class<? extends Annotation> annotationType() {
               return annotation.annotationType();
           }
       };
       process(eventTopicSubscriber, subscriber, method, add);
   }

   private static void process(final RuntimeTopicPatternEventSubscriber annotation, final Object subscriber, final Method method, boolean add) {
       EventTopicPatternSubscriber eventTopicPatternSubscriber = new EventTopicPatternSubscriber() {
           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public Class<? extends EventService> autoCreateEventServiceClass() {
               return annotation.autoCreateEventServiceClass();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public String eventServiceName() {
               return annotation.eventServiceName();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public ReferenceStrength referenceStrength() {
               return annotation.referenceStrength();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
			  public int priority() {
				return annotation.priority();
			  }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public boolean exact() {
               return annotation.exact();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public String topicPattern() {
               return getTopic(annotation.methodName(), subscriber, method);
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public Class<? extends Annotation> annotationType() {
               return annotation.annotationType();
           }
       };
       process(eventTopicPatternSubscriber, subscriber, method, add);
   }

   /* This is a cut and paste from above practically, sure which Annotations could be extended.
    * TODO: When Java 7 comes out, or whatever JSR-308 is delivered, reduce this file by 70% */  
   private static void process(VetoTopicPatternSubscriber topicPatternAnnotation, Object obj, Method method, boolean add) {
      //Check args
      String topicPattern = topicPatternAnnotation.topicPattern();
      if (topicPattern == null) {
         throw new IllegalArgumentException("Topic pattern cannot be null for VetoTopicPatternSubscriber annotation");
      }

      //Get event service
      Class<? extends EventService> eventServiceClass = topicPatternAnnotation.autoCreateEventServiceClass();
      String eventServiceName = topicPatternAnnotation.eventServiceName();
      EventService eventService = getEventServiceFromAnnotation(eventServiceName, eventServiceClass);
      int priority = topicPatternAnnotation.priority();

      //Create proxy and subscribe
      Pattern pattern = Pattern.compile(topicPattern);
      ProxyTopicPatternSubscriber subscriber = new ProxyTopicPatternSubscriber(obj, method, topicPatternAnnotation.referenceStrength(),
              priority, eventService, topicPattern, pattern, true);

      //See Issue #18
      //Also note that this post is wrong: https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
      //Since two WeakReferences are not treated as one.  So this always has to be strong and we'll have to clean up occasionally.
      if (add) {
         eventService.subscribeVetoListenerStrongly(pattern, subscriber);
      } else {
         eventService.unsubscribeVeto(pattern, obj);
      }
   }

   private static void process(VetoTopicSubscriber topicAnnotation, Object obj, Method method, boolean add) {
      //Check args
      String topic = topicAnnotation.topic();
      if (topic == null) {
         throw new IllegalArgumentException("Topic cannot be null for VetoTopicSubscriber annotation");
      }

      //Get event service
      Class<? extends EventService> eventServiceClass = topicAnnotation.autoCreateEventServiceClass();
      String eventServiceName = topicAnnotation.eventServiceName();
      EventService eventService = getEventServiceFromAnnotation(eventServiceName, eventServiceClass);

      int priority = topicAnnotation.priority();

      //Create proxy and subscribe
      ProxyTopicSubscriber subscriber = new ProxyTopicSubscriber(obj, method,
              topicAnnotation.referenceStrength(), priority, eventService, topic, true);

      //See Issue #18
      //Also note that this post is wrong: https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
      //Since two WeakReferences are not treated as one.  So this always has to be strong and we'll have to clean up occasionally.
      if (add) {
         eventService.subscribeVetoListenerStrongly(topic, subscriber);
      } else {
         eventService.unsubscribeVeto(topic, obj);
      }
   }

   private static void process(VetoSubscriber annotation, Object obj, Method method, boolean add) {
      //Check args
      Class eventClass = annotation.eventClass();
      if (eventClass == null) {
         throw new IllegalArgumentException("Event class cannot be null for VetoSubscriber annotation");
      } else if (UseTheClassOfTheAnnotatedMethodsParameter.class.equals(eventClass)) {
         Class[] params = method.getParameterTypes();
         if (params.length < 1) {
            throw new RuntimeException("Expected annotated method to have one parameter.");
         } else {
            eventClass = params[0];
         }
      }

      //Get event service
      Class<? extends EventService> eventServiceClass = annotation.autoCreateEventServiceClass();
      String eventServiceName = annotation.eventServiceName();
      EventService eventService = getEventServiceFromAnnotation(eventServiceName, eventServiceClass);

      int priority = annotation.priority();

      //Create proxy and subscribe
      //See https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
      BaseProxySubscriber subscriber = new BaseProxySubscriber(obj, method, annotation.referenceStrength(),
              priority, eventService, eventClass, true);
      if (add) {
         if (annotation.exact()) {
            //See Issue #18
            //Also note that this post is wrong: https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
            //Since two WeakReferences are not treated as one.  So this always has to be strong and we'll have to clean up occasionally.
            eventService.subscribeVetoListenerExactlyStrongly(eventClass, subscriber);
         } else {
            //See Issue #18
            //Also note that this post is wrong: https://eventbus.dev.java.net/servlets/ProjectForumMessageView?messageID=19499&forumID=1834
            //Since two WeakReferences are not treated as one.  So this always has to be strong and we'll have to clean up occasionally.
            eventService.subscribeVetoListenerStrongly(eventClass, subscriber);
         }
      } else {
         if (annotation.exact()) {
            eventService.unsubscribeVetoExactly(eventClass, obj);
         } else {
            eventService.unsubscribeVeto(eventClass, obj);
         }
      }
   }


   private static void process(final VetoRuntimeTopicSubscriber annotation, final Object subscriber, final Method method, boolean add) {
       VetoTopicSubscriber eventTopicSubscriber = new VetoTopicSubscriber() {
           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public Class<? extends EventService> autoCreateEventServiceClass() {
               return annotation.autoCreateEventServiceClass();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public String eventServiceName() {
               return annotation.eventServiceName();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public ReferenceStrength referenceStrength() {
               return annotation.referenceStrength();
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
			  public int priority() {
			   	return annotation.priority();
			  }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public String topic() {
               return getTopic(annotation.methodName(), subscriber, method);
           }

           //TODO uncomment when language level is set to 1.6 (2.0) @Override
           public Class<? extends Annotation> annotationType() {
               return annotation.annotationType();
           }
       };
       process(eventTopicSubscriber, subscriber, method, add);
   }

   private static void process(final VetoRuntimeTopicPatternSubscriber annotation, final Object subscriber, final Method method, boolean add) {
      VetoTopicPatternSubscriber eventTopicPatternSubscriber = new VetoTopicPatternSubscriber() {
         //TODO uncomment when language level is set to 1.6 (2.0) @Override
         public Class<? extends EventService> autoCreateEventServiceClass() {
            return annotation.autoCreateEventServiceClass();
         }

         //TODO uncomment when language level is set to 1.6 (2.0) @Override
         public String eventServiceName() {
            return annotation.eventServiceName();
         }

         //TODO uncomment when language level is set to 1.6 (2.0) @Override
         public ReferenceStrength referenceStrength() {
            return annotation.referenceStrength();
         }

         //TODO uncomment when language level is set to 1.6 (2.0) @Override
         public int priority() {
            return annotation.priority();
         }

         //TODO uncomment when language level is set to 1.6 (2.0) @Override
         public boolean exact() {
            return annotation.exact();
         }

         //TODO uncomment when language level is set to 1.6 (2.0) @Override
         public String topicPattern() {
            return getTopic(annotation.methodName(), subscriber, method);
         }

         //TODO uncomment when language level is set to 1.6 (2.0) @Override
         public Class<? extends Annotation> annotationType() {
            return annotation.annotationType();
         }
      };
      process(eventTopicPatternSubscriber, subscriber, method, add);
   }


   private static String getTopic(String methodName, Object subscriber, Method method) {
       try {
           Method runtimeEvalMethod = subscriber.getClass().getMethod(methodName, new Class[0]);
           //necessary in case the method does not have public access or if the class it belongs
           //to isn't public
           runtimeEvalMethod.setAccessible(true);
           return runtimeEvalMethod.invoke(subscriber, new Object[0]).toString();
       } catch (SecurityException e) {
           throw new RuntimeException("Could not retrieve method for subscription. Method: " + methodName, e);
       } catch (NoSuchMethodException e) {
           throw new RuntimeException("Could not retrieve method for subscription. Method: " + methodName, e);
       } catch (InvocationTargetException e) {
           e.getTargetException().printStackTrace();
           throw new RuntimeException("Could not invoke method for subscription. Method: " + methodName, e);
       } catch (IllegalAccessException e) {
           throw new RuntimeException("Could not invoke method for subscription. Method: " + methodName, e);
       }
   }


   private static EventService getEventServiceFromAnnotation(String eventServiceName,
           Class<? extends EventService> eventServiceClass) {
      EventService eventService = EventServiceLocator.getEventService(eventServiceName);
      if (eventService == null) {
         if (EventServiceLocator.SERVICE_NAME_EVENT_BUS.equals(eventServiceName)) {
            //This may be the first time the EventBus is accessed.
            eventService = EventServiceLocator.getSwingEventService();
         } else {
            //The event service does not yet exist, create it
            try {
               eventService = eventServiceClass.newInstance();
            } catch (InstantiationException e) {
               throw new RuntimeException("Could not instance of create EventService class " + eventServiceClass, e);
            } catch (IllegalAccessException e) {
               throw new RuntimeException("Could not instance of create EventService class " + eventServiceClass, e);
            }
            try {
               EventServiceLocator.setEventService(eventServiceName, eventService);
            } catch (EventServiceExistsException e) {
               //ignore it, it's OK
               eventService = EventServiceLocator.getEventService(eventServiceName);
            }
         }
      }
      return eventService;
   }

}
