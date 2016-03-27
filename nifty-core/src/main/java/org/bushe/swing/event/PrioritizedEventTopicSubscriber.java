package org.bushe.swing.event;

/**
 * This is a convenience interface, particularly for inner classes, that implements
 * {@link org.bushe.swing.event.EventTopicSubscriber} and {@link org.bushe.swing.event.Prioritized}.
 */
public interface PrioritizedEventTopicSubscriber extends EventTopicSubscriber, Prioritized {
}