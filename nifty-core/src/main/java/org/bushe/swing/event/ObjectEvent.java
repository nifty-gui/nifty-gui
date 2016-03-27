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
 * A simple event that delivers an untyped object with a source object.
 * <p/>
 * Usage: EventBus.publish(new ObjectEvent(this, objectOfInterest);
 *
 * @author Michael Bushe michael@bushe.com
 */
public class ObjectEvent extends AbstractEventServiceEvent {
   private Object eventObject;

   /**
    * Constructor
    *
    * @param sourceObject the source of the event
    * @param payload the payload or eventObject of the event
    */
   public ObjectEvent(Object sourceObject, Object payload) {
      super(sourceObject);
      this.eventObject = payload;
   }

   public Object getEventObject() {
      return eventObject;
   }
}
