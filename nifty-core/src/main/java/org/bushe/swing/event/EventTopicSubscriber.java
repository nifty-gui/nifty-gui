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
 * Callback interface for topic-based subscribers of an {@link EventService}.
 *
 * @author Michael Bushe michael@bushe.com
 */
public interface EventTopicSubscriber<T> {

   /**
    * Handle an event published on a topic.
    * <p/>
    * The EventService calls this method on each publication on a matching topic name passed to one of the
    * EventService's topic-based subscribe methods, specifically, {@link EventService#subscribe(String,
    *EventTopicSubscriber)} {@link EventService#subscribe(java.util.regex.Pattern,EventTopicSubscriber)} {@link
    * EventService#subscribeStrongly(String,EventTopicSubscriber)} and {@link EventService#subscribeStrongly(java.util.regex.Pattern,
    *EventTopicSubscriber)}.
    *
    * @param topic the name of the topic published on
    * @param data the data object published on the topic
    */
   public void onEvent(String topic, T data);
}
