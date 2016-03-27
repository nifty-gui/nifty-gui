/**
 * Copyright 2005 Bushe Enterprises, Inc., Hopkinton, MA, USA, www.bushe.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bushe.swing.event;

/**
 * Convenience base class for EventServiceEvents in the application. Provides the convenience of
 * holding the event source publication and event status.  It is not necessary to use this event class when 
 * using an EventService.
 *
 * @author Michael Bushe michael@bushe.com
 */
public abstract class AbstractEventServiceEvent implements EventServiceEvent, PublicationStatusTracker {

   private Object source = null;
   protected final Object stateLock = new Object();
   private PublicationStatus publicationStatus = PublicationStatus.Unpublished;

   /**
    * Default constructor
    *
    * @param source the source of the event
    */
   public AbstractEventServiceEvent(Object source) {
      this.source = source;
   }

   /** @return the source of this event */
   public Object getSource() {
      return source;
   }

   public PublicationStatus getPublicationStatus() {
      synchronized (stateLock) {
         return publicationStatus;
      }
   }

   public void setPublicationStatus(PublicationStatus status) {
      synchronized (stateLock) {
         publicationStatus = status;
      }
   }
}
