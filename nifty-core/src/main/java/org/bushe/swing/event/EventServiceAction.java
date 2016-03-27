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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 * Abstract class that publishes a Swing ActionEvent (or another object) to an {@link EventService}.
 * <p/>
 * This abstract class ties the Swing Actions with the Event Bus.  When fired, an ActionEvent is published on an
 * EventService - either the global EventBus or a Container EventService.
 * <p/>
 * There are two derivatives of this class: The EventBusAction, which publishes the ActionEvent on the global EventBus,
 * and the ContainerEventServiceAction, which publishes the ActionEvent on the a local ContainerEventService.
 * <p/>
 * By default the ActionEvent is published on an EventService topic corresponding to this action's
 * Action.ACTION_COMMAND_KEY. Though this behavior is highly configurable.  See {@link #getTopicName(ActionEvent)} and
 * {@link #setTopicName(String)} for ways to customize the topic used.  Override {@link #getTopicValue(ActionEvent)} to
 * publish an object other than the ActionEvent.
 * <p/>
 * Instead of publishing on a topic, the ActionEvent can be published using class-based publication, use {@link
 * #setPublishesOnTopic(boolean)} to set this behavior.  When using class-based publication, the ActionEvent is published
 * by default.  Override {@link #getEventServiceEvent(ActionEvent)} to publish an object other than the ActionEvent.
 *
 * @author Michael Bushe michael@bushe.com
 */
public abstract class EventServiceAction extends AbstractAction {
   public static final String EVENT_SERVICE_TOPIC_NAME = "event-service-topic";

   private boolean throwsExceptionOnNullEventService = true;
   public static final String EVENT_BUS_EVENT_CLASS_NAME = "eventBus.eventClassName";

   private String topicName;
   private boolean publishesOnTopic = true;

   public EventServiceAction() {
   }

   public EventServiceAction(String actionName, ImageIcon icon) {
      super(actionName, icon);
   }


   /**
    * Override to return the EventService on which to publish.
    *
    * @param event the event passed to #execute(ActionEvent)
    *
    * @return the event service to publish on, if null and 
    * getThrowsExceptionOnNullEventService() is true (default) an exception is thrown
    *
    * @see EventBusAction
    * @see ContainerEventServiceAction
    */
   protected abstract EventService getEventService(ActionEvent event);

   /** @return true if this action publishes on a topic, false if it uses class-based publication. */
   public boolean isPublishesOnTopic() {
      return publishesOnTopic;
   }

   /**
    * Sets whether this action publishes on a topic or uses class-based publication.
    *
    * @param onTopic true if publishes on topic (the default), false if using class-based publication.
    */
   public void setPublishesOnTopic(boolean onTopic) {
      this.publishesOnTopic = onTopic;
   }

   /**
    * Explicitly sets the topic name this action publishes on.
    * <p/>
    * A topic name does not need to be explicitly set.  See {@link #getTopicName(ActionEvent)} to understand how the
    * topic name is determined implicitly.
    */
   public void setTopicName(String topicName) {
      this.topicName = topicName;
   }

   /**
    * The topic name is the first non-null value out of: <ol> <li>A topic name explicitly set via {@link
    * #setTopicName(String)} <li>the action's getValue("event-service-topic")  {@link #EVENT_SERVICE_TOPIC_NAME} <li>the
    * action's getValue("ID") (for compatibility with the SAM ActionManager's ID) <li>the action's {@link
    * javax.swing.Action#ACTION_COMMAND_KEY} <li>the action event's {@link javax.swing.Action#ACTION_COMMAND_KEY}
    * <li>the action's {@link javax.swing.Action#NAME} the value is used (if the value is not a String, the value's
    * toString() is used). 
    * <p/>
    * To use a different name, override this method.
    *
    * @param event the event passed to #execute(ActionEvent)
    *
    * @return the topic name to publish on, getId() by default
    */
   public String getTopicName(ActionEvent event) {
      if (topicName != null) {
         return topicName;
      }
      Object topic = getValue(EVENT_SERVICE_TOPIC_NAME);
      if (topic != null) {
         return topic + "";
      } else {
         topic = getValue("ID");
         if (topic != null) {
            return topic + "";
         } else {
            topic = getValue(Action.ACTION_COMMAND_KEY);
            if (topic != null) {
               return topic + "";
            } else {
               topic = event.getActionCommand();
               if (topic != null) {
                  return topic + "";
               } else {
                  return (String) getName();
               }
            }
         }
      }
   }

   /**
    * By default, the ActionEvent is the object published on the topic.  Override this method to publish another
    * object.
    *
    * @param event the event passed to #execute(ActionEvent)
    *
    * @return the topic value to publish, getId() by default
    */
   protected Object getTopicValue(ActionEvent event) {
      return event;
   }

   /** @return the name of the action (javax.swing.Action#NAME) */
   public Object getName() {
      return getValue(Action.NAME);
   }

   /**
    * If isPublishesOnTopic() returns false (i.e., when using class-based rather than topic-based publication), then
    * override this method to publish an on object other than the ActionEvent.
    *
    * @return the Object to publish, cannot be null
    */
   protected Object getEventServiceEvent(ActionEvent event) {
      return event;
   }

   /**
    * Publishes the event on the EventService returned by getEventService(event)
    * <p/>
    * Gets the EventService from {@link #getEventService(java.awt.event.ActionEvent)}. Checks isPublishesOnTopic().  If true,
    * gets the topic name from {@link #getTopicName(java.awt.event.ActionEvent)} and the topic value from {@link
    * #getTopicValue(ActionEvent)}, and publishes the value on the topic on the EventService.  If false, gets event from
    * {@link #getEventServiceEvent(java.awt.event.ActionEvent)}, and publishes the event on the EventService.
    * <p/>
    *
    * @param event the action event to publish.
    *
    * @throws RuntimeException if getThrowsExceptionOnNullEventService() &&  getEventService(event) == null
    */
   public void actionPerformed(ActionEvent event) {
      EventService eventService = getEventService(event);
      if (eventService == null) {
         if (getThrowsExceptionOnNullEventService()) {
            throw new RuntimeException("Null EventService supplied to EventServiceAction with name:" + getName());
         } else {
            return;
         }
      }
      if (isPublishesOnTopic()) {
         String topic = getTopicName(event);
         Object topicValue = getTopicValue(event);
         eventService.publish(topic, topicValue);
      } else {
         Object esEvent = getEventServiceEvent(event);
         eventService.publish(esEvent);
      }
   }

   /**
    * By default, exceptions are throw if getEventService() returns null.
    *
    * @return false to suppress this behavior
    */
   public boolean getThrowsExceptionOnNullEventService() {
      return throwsExceptionOnNullEventService;
   }

   /**
    * By default, exceptions are thrown if getEventService() returns null.
    *
    * @param throwsExceptionOnNullEventService true to suppress the exception when there is no event service
    */
   public void setThrowsExceptionOnNullEventService(boolean throwsExceptionOnNullEventService) {
      this.throwsExceptionOnNullEventService = throwsExceptionOnNullEventService;
   }
}
