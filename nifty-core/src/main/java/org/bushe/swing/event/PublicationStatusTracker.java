package org.bushe.swing.event;

/**
 * An optional interface that can be implemented by Events objects or topic Payloads
 * to enable the events' status to be stamped on the event by an event service.
 * <p>
 * EventService implementations must call setEventStatus(status) on event objects and
 * payloads that implement this interface.
 */
public interface PublicationStatusTracker {

   /**
    * Implementations of this method must be made thread safe.
    * @return last value set by setPublicationStatus(), or
    * {@link PublicationStatus#Unpublished} if setPublicationStatus was never called.
    */
   public PublicationStatus getPublicationStatus();

   /**
    * Implementations of this method must be made thread safe.
    * @param status the status of the event during it's current publication
    */
   public void setPublicationStatus(PublicationStatus status);
}
