package org.bushe.swing.event;

/**
 * This is a convenience interface, particularly for inner classes, that implements
 * {@link EventSubscriber} and {@link Prioritized}.
 */
public interface PrioritizedEventSubscriber extends EventSubscriber, Prioritized {
}
