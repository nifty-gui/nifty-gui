package org.bushe.swing.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

/**
 * An Annotation for subscribing to EventService Events.
 * <p>
 * This annotation simplifies much of the repetitive boilerplate used for subscribing to EventService Events.
 * <p>
 * Instead of this:
 * <pre>
 * public class MyAppController implements EventSubscriber {
 *   public MyAppController {
 *      EventBus.subscribe(AppClosingEvent.class, this);
 *   }
 *   public void onEvent(EventServiceEvent event) {
 *      AppClosingEvent appClosingEvent = (AppClosingEvent)event;
 *      //do something
 *   }
 * }
 * </pre>
 * You can do this:
 * <pre>
 * public class MyAppController {  //no interface necessary
 *   public MyAppController {
 *       AnnotationProcessor.process(this);//if not using AOP
 *   }
 *   &#64;EventSubscriber
 *   public void onAppClosingEvent(AppClosingEvent appClosingEvent) {//Use your own method name with typesafety
 *      //do something
 *   }
 * }
 * </pre>
 * <p>
 * That's pretty good, but when the controller does more, annotations are even nicer.
 * <pre>
 * public class MyAppController implements EventSubscriber {
 *   public MyAppController {
 *      EventBus.subscribe(AppStartingEvent.class, this);
 *      EventBus.subscribe(AppClosingEvent.class, this);
 *   }
 *   public void onEvent(EventServiceEvent event) {
 *      //wicked bad pattern, but we have to this
 *      //...or create multiple subscriber classes and hold instances of them fields, which is even more verbose...
 *      if (event instanceof AppStartingEvent) {
 *         onAppStartingEvent((AppStartingEvent)event);
 *      } else (event instanceof AppClosingEvent) {
 *         onAppStartingEvent((AppClosingEvent)event);
 *      }
 *
 *   }
 *
 *   public void onAppStartingEvent(AppStartingEvent appStartingEvent) {
 *      //do something
 *   }
 *
 *   public void onAppClosingEvent(AppClosingEvent appClosingEvent) {
 *      //do something
 *   }
 * }
 * </pre>
 * You can do this:
 * <pre>
 * public class MyAppController {
 *   public MyAppController {
 *       AnnotationProcessor.process(this);//this line can be avoided with a compile-time tool or an Aspect
 *   }
 *   &#64;EventSubscriber(eventClass=AppStartingEvent.class)
 *   public void onAppStartingEvent(AppStartingEvent appStartingEvent) {
 *      //do something
 *   }
 *   &#64;EventSubscriber(eventClass=AppAppClosingEvent.class)
 *   public void onAppClosingEvent(AppClosingEvent appClosingEvent) {
 *      //do something
 *   }
 * }
 * </pre>
 * Brief, clear, and easy.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventSubscriber {
   /** The class to subscribe to, if not specified, a subscription is created for the type of the method parameter. */
   Class eventClass() default UseTheClassOfTheAnnotatedMethodsParameter.class;

   /** Determines the order in which this subscriber is called, default is FIFO.*/
   int priority() default 0;

   /** Whether or not to subscribe to the exact class or a class hierarchy, defaults to class hierarchy (false). */
   boolean exact() default false;

   /** Whether to subscribe weakly or strongly. */
   ReferenceStrength referenceStrength() default ReferenceStrength.WEAK;

   /** The event service to subscribe to, default to the EventServiceLocator.SERVICE_NAME_EVENT_BUS. */
   String eventServiceName() default EventServiceLocator.SERVICE_NAME_EVENT_BUS;

   /**
    * Whether or not to autocreate the event service if it doesn't exist on subscription, default is true. If the
    * service needs to be created, it must have a default constructor.
    */
   Class<? extends EventService> autoCreateEventServiceClass() default ThreadSafeEventService.class;
}
