package org.bushe.swing.event.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

/**
 * An Annotation for subscribing to EventService Topics.
 * <p>
 * This annotation simplifies much of the repetitive boilerplate used for subscribing to EventService Topics.
 * <p>
 * Instead of this:
 * <pre>
 * public class MyAppController implements EventTopicSubscriber {
 *   public MyAppController {
 *      EventBus.subscribe("AppClosing", this);
 *   }
 *   public void onEvent(String topic, Object data) {
 *      JComponent source = (JComponent)data;
 *      //do something
 *   }
 * }
 * </pre>
 * You can do this:
 * <pre>
 * public class MyAppController {  //no interface necessary
 *   public MyAppController {
 *       AnnotationProcessor.process(this);//this line can be avoided with a compile-time tool or an Aspect
 *   }
 *   &#64;EventTopicSubscriber{topic="AppClosingEvent"}
 *   public void onAppClosing(String topic, Object data) {
 *      //do something
 *   }
 * }
 * </pre>
 * <p>
 * That's pretty good, but when the controller does more, annotations are even nicer.
 * <pre>
 * public class MyAppController implements EventTopicSubscriber {
 *   public MyAppController {
 *      EventBus.subscribe("AppStartingEvent", this);
 *      EventBus.subscribe("AppClosingEvent", this);
 *   }
 *   public void onEvent(String topic, Object data) {
 *      //wicked bad pattern, but we have to this
 *      //...or create multiple subscriber classes and hold instances of them fields, which is even more verbose...
 *      if ("AppStartingEvent".equals(topic)) {
 *         onAppStartingEvent((JComponent)data);
 *      } else ("AppClosingEvent".equals(topic)) {
 *         onAppClosingEvent((JComponet)data);
 *      }
 *
 *   }
 *
 *   public void onAppStartingEvent(JComponent requestor) {
 *      //do something
 *   }
 *
 *   public void onAppClosingEvent(JComponent requestor) {
 *      //do something
 *   }
 * }
 * </pre>
 * Instead of all that, you can do this:
 * <pre>
 * public class MyAppController {
 *   public MyAppController {
 *       AnnotationProcessor.process(this);//this line can be avoided with a compile-time tool or an Aspect
 *   }
 *   &#64;EventTopicSubscriber{topic="AppStartingEvent"}
 *   public void onAppStartingEvent(Object data) {
 *      //do something
 *   }
 *   &#64;EventTopicSubscriber{topic="AppClosingEvent"}
 *   public void onAppClosingEvent(Foo data) {
 *      //do something
 *   }
 * }
 * </pre>
 * Brief, clear, and easy.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventTopicSubscriber {
   /** The topic to subscribe to */
   String topic();

   /** Whether to subscribe weakly or strongly. */
   ReferenceStrength referenceStrength() default ReferenceStrength.WEAK;

   /** The event service to subscribe to, default to the EventServiceLocator.SERVICE_NAME_EVENT_BUS. */
   String eventServiceName() default EventServiceLocator.SERVICE_NAME_EVENT_BUS;

   /** Determines the order in which this subscriber is called, default is FIFO.*/
   int priority() default 0;

   /**
    * Whether or not to autocreate the event service if it doesn't exist on subscription, default is true. If the
    * service needs to be created, it must have a default constructor.
    */
   Class<? extends EventService> autoCreateEventServiceClass() default ThreadSafeEventService.class;
}
