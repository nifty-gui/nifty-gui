package org.bushe.swing.event;

/**
 * The status of an event as it makes its way from publication through processing by subscribers.
 * <p>
 * EventServices are required to stamp any event object or payload that implements the EventStatusTracker
 * with the corresponding EventStatus as the event object is processed.  The EventService is not
 * required to set the Unpublished state.
 */
public enum PublicationStatus {
   /** Recommended default.*/
   Unpublished,
   /** Set directly after publication on an EventService.*/
   Initiated,
   /** End status for events that are vetoed and never sent to subscribers.*/
   Vetoed,
   /** State set after veto test is passed before the event is send to any subscribers.*/
   Queued,
   /** Set while the event is sent to it's subscribers.  EventService implementations
    * such as the ThreadSafeEventService and the SwingEventService will transition from Queued to
    * Publishing immediately.  Others implementations that call subscribers on threads different
    * from veto subscribers are free to leave an event in the Queued state and wait until
    * the event is passed to the thread(s) that subscribers are called on to set the
    * Publishing state */
   Publishing,
   /**
    * Called when all subscribers have finished handling the event publication.
    */
   Completed
}
