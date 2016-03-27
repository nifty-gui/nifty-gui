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
 * Callback interface for class-based subscribers of an {@link EventService}.
 *
 * @author Michael Bushe michael@bushe.com
 */
public interface EventSubscriber<T> {

   /**
    * Handle a published event. <p>The EventService calls this method on each publication of an object that matches the
    * class or interface passed to one of the EventService's class-based subscribe methods, specifically, {@link
    * EventService#subscribe(Class,EventSubscriber)} {@link EventService#subscribeExactly(Class,EventSubscriber)}
    * {@link EventService#subscribeStrongly(Class,EventSubscriber)} and {@link EventService#subscribeExactlyStrongly(Class,
    *EventSubscriber)}.
    *
    * @param event The Object that is being published.
    */
   public void onEvent(T event);
}
