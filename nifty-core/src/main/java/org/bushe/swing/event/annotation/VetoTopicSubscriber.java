package org.bushe.swing.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

/**
 * An Annotation for adding VetoTopicListener subscriptions to EventService Events.
 * <p>
 * This annotation simplifies much of the repetitive boilerplate used for adding veto topic listeners
 * (which in EventBus 2.0 will be called VetoTopicSubscribers, thus this annotation name difference)
 * to EventService Events.  Example:
 * <p>
 * <pre>
 * public class MyAppController {
 *   public MyAppController {
 *       AnnotationProcessor.process(this);//this line can be avoided with a compile-time tool or an Aspect
 *   }
 *   &#64;EvenTopicSubscriber(topic="App.Close")
 *   public void onAppClosingEvent(String topic, Object payload) {
 *      //close connections, close windows
 *   }
 * }
 *
 * public class MyDocumentController {
 *   &#64;VetoTopicSubscriber(topic="App.Close")
 *   public boolean ensureDocumentIsSaved(String topic, Object payload) {
 *      if (docHasUnsavedChanges()) {
 *         boolean answer = MyModalDialog.show("Are you sure you want to close and lose your changes?");
 *         if (answer == StandardButtonValues.Cancel) {
 *            //stop processing this event
 *            return true;
 *         }
 *      }
 *      //It's OK to close
 *      return false;
 *   }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface VetoTopicSubscriber {
   /** The topic to subscribe to */
   String topic();

   /** Determines the order in which this veto subscriber is called, default is FIFO.*/
   public abstract int priority() default 0;

   /** Whether to subscribe weakly or strongly. */
   public abstract ReferenceStrength referenceStrength() default ReferenceStrength.WEAK;

   /** The event service to subscribe to, default to the EventServiceLocator.SERVICE_NAME_EVENT_BUS. */
   public abstract String eventServiceName() default EventServiceLocator.SERVICE_NAME_EVENT_BUS;

   /**
    * Whether or not to autocreate the event service if it doesn't exist on subscription, default is true. If the
    * service needs to be created, it must have a default constructor.
    */
   public abstract Class<? extends EventService> autoCreateEventServiceClass() default ThreadSafeEventService.class;
}
