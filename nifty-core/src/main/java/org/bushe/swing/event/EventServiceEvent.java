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
 * Convenience interface for events that get processed by the EventService, its usage is not required in any way.  Any
 * object can be published on an EventService or on the EventBus.
 * <p/>
 * It is a good practice to specify the source of the event when using pub/sub, especially in Swing applications.
 *
 * @author Michael Bushe michael@bushe.com
 * @see AbstractEventServiceEvent for a simple base class
 */
public interface EventServiceEvent {
   /** @return The issuer of the event. */
   Object getSource();
}
