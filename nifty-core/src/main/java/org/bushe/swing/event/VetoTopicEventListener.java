package org.bushe.swing.event;

/**
 * Interface for classes that can veto publication on topic names from the {@link org.bushe.swing.event.EventService}.
 *
 * @author Michael Bushe michael@bushe.com
 */
public interface VetoTopicEventListener<T> {

   /**
    * Determine whether a topic publication should be vetoed or allowed.
    * <p/>
    * The EventService calls this method <b>before</b> publication of on a topic name.  If any of the
    * VetoTopicEventListeners return true, then none of the subscribers to that topic are called. <p>Prerequisite:
    * VetoTopicEventListener has to be subscribed with the EventService for the topic name.</p> <p>Guaranteed to be
    * called in the SwingEventThread when using the SwingEventService (EventBus). See {@link EventService}</p>
    *
    * @param topic The topic name the data object is published on.
    * @param data The data object being published on the topic.
    *
    * @return true if the publication on the topic should be vetoed and not published, false if the data should be
    *         published on the topic.
    */
   public boolean shouldVeto(String topic, T data);
}
