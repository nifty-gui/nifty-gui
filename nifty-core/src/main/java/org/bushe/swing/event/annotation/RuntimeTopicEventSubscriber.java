package org.bushe.swing.event.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

/**
 * A subscriber to a topic that is determined at runtime.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RuntimeTopicEventSubscriber {
    /**
     * @return name of a method (that must return a String) and whose return value will become the subscription topic.
     */
    String methodName() default "getTopicName";

    /** @return Whether to subscribe weakly or strongly. */
    ReferenceStrength referenceStrength() default ReferenceStrength.WEAK;

    /**
     * @return event service to subscribe to, default to the EventServiceLocator.SERVICE_NAME_EVENT_BUS.
     */
    String eventServiceName() default EventServiceLocator.SERVICE_NAME_EVENT_BUS;

    /** @return Determines the order in which this subscriber is called, default is FIFO.*/
    int priority() default 0;

    /**
     * @return Whether or not to autocreate the event service if it doesn't exist on subscription, default is true. If the service needs to be created, it must have a default constructor.
     */
    Class<? extends EventService> autoCreateEventServiceClass() default ThreadSafeEventService.class;
}