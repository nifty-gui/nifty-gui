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

import java.util.List;
import java.util.regex.Pattern;
import java.lang.reflect.Type;

/**
 * The EventBus provides event publication and subscription services.  It is a simple static wrapper around a
 * global instance of an {@link EventService}, specifically a {@link SwingEventService} by default.
 * <p/>
 * For Swing Applications the EventBus is nearly all you need, besides some of your own Event classes (if so desired).
 * <p/>
 * The EventBus is really just a convenience class that provides a static wrapper around a global {@link
 * EventService} instance.  This class exists solely for simplicity.  Calling
 * <code>EventBus.subscribeXXX/publishXXX</code> is equivalent to
 * <code>EventServiceLocator.getEventBusService().subscribeXXX/publishXXX</code>,
 * it is just shorter to type.  See {@link org.bushe.swing.event.EventServiceLocator} for details on how to customize
 * the global EventService in place of the default SwingEventService. 
 *
 * @author Michael Bushe michael@bushe.com
 * @see EventService
 * @see SwingEventService
 * @see ThreadSafeEventService See package JavaDoc for more information
 */
public class EventBus {

  /**
    * The EventBus uses a global static EventService.  This method is not necessary in usual usage, use the other static
    * methods instead.  It is used to expose any other functionality and for framework classes (EventBusAction)
    *
    * @return the global static EventService
    */
   public static EventService getGlobalEventService() {
      return EventServiceLocator.getEventBusService();
   }

   /** @see EventService#publish(Object) */
   public static void publish(Object event) {
      if (event == null) {
         throw new IllegalArgumentException("Can't publish null.");
      }
      EventServiceLocator.getEventBusService().publish(event);
   }

   /** @see EventService#publish(String,Object) */
   public static void publish(String topic, Object o) {
      if (topic == null) {
         throw new IllegalArgumentException("Can't publish to null topic.");
      }
      EventServiceLocator.getEventBusService().publish(topic, o);
   }

   /** @see EventService#publish(java.lang.reflect.Type, Object)  */
   public static void publish(Type genericType, Object o) {
      if (genericType == null) {
         throw new IllegalArgumentException("Can't publish to null type.");
      }
      EventServiceLocator.getEventBusService().publish(genericType, o);
   }


   /** @see EventService#subscribe(Class,EventSubscriber) */
   public static boolean subscribe(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribe(eventClass, subscriber);
   }

   /** @see EventService#subscribe(java.lang.reflect.Type, EventSubscriber)  */
   public static boolean subscribe(Type genericType, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribe(genericType, subscriber);
   }

   /** @see EventService#subscribeExactly(Class,EventSubscriber) */
   public static boolean subscribeExactly(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeExactly(eventClass, subscriber);
   }

   /** @see EventService#subscribe(String,EventTopicSubscriber) */
   public static boolean subscribe(String topic, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribe(topic, subscriber);
   }

   /** @see EventService#subscribe(Pattern,EventTopicSubscriber) */
   public static boolean subscribe(Pattern topicPattern, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribe(topicPattern, subscriber);
   }

   /** @see EventService#subscribeStrongly(Class,EventSubscriber) */
   public static boolean subscribeStrongly(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeStrongly(eventClass, subscriber);
   }

   /** @see EventService#subscribeExactlyStrongly(Class,EventSubscriber) */
   public static boolean subscribeExactlyStrongly(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeExactlyStrongly(eventClass, subscriber);
   }

   /** @see EventService#subscribeStrongly(String,EventTopicSubscriber) */
   public static boolean subscribeStrongly(String topic, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeStrongly(topic, subscriber);
   }

   /** @see EventService#subscribeStrongly(Pattern,EventTopicSubscriber) */
   public static boolean subscribeStrongly(Pattern topicPattern, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().subscribeStrongly(topicPattern, subscriber);
   }

   /** @see EventService#unsubscribe(Class,EventSubscriber) */
   public static boolean unsubscribe(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(eventClass, subscriber);
   }

   /** @see EventService#unsubscribeExactly(Class,EventSubscriber) */
   public static boolean unsubscribeExactly(Class eventClass, EventSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribeExactly(eventClass, subscriber);
   }

   /** @see EventService#unsubscribe(String,EventTopicSubscriber) */
   public static boolean unsubscribe(String topic, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(topic, subscriber);
   }

   /** @see EventService#unsubscribe(Pattern,EventTopicSubscriber) */
   public static boolean unsubscribe(Pattern topicPattern, EventTopicSubscriber subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(topicPattern, subscriber);
   }

   /**
    * For usage with annotations.
    *
    * @see EventService#unsubscribe(Class,Object)
    */
   public static boolean unsubscribe(Class eventClass, Object object) {
      return EventServiceLocator.getEventBusService().unsubscribe(eventClass, object);
   }

   /**
    * For usage with annotations.
    *
    * @see EventService#unsubscribeExactly(Class,Object)
    */
   public static boolean unsubscribeExactly(Class eventClass, Object subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribeExactly(eventClass, subscriber);
   }

   /**
    * For usage with annotations.
    *
    * @see EventService#unsubscribe(String,Object)
    */
   public static boolean unsubscribe(String topic, Object subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(topic, subscriber);
   }

   /**
    * For usage with annotations.
    *
    * @see EventService#unsubscribe(Pattern,Object)
    */
   public static boolean unsubscribe(Pattern topicPattern, Object subscriber) {
      return EventServiceLocator.getEventBusService().unsubscribe(topicPattern, subscriber);
   }

   /** @see EventService#subscribeVetoListener(Class,VetoEventListener) */
   public static boolean subscribeVetoListener(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListener(eventClass, vetoListener);
   }

   /** @see EventService#subscribeVetoListener(Class,VetoEventListener) */
   public static boolean subscribeVetoListenerExactly(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerExactly(eventClass, vetoListener);
   }


   /** @see EventService#subscribeVetoListener(String,VetoTopicEventListener) */
   public static boolean subscribeVetoListener(String topic, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListener(topic, vetoListener);
   }

   /** @see EventService#subscribeVetoListener(Pattern,VetoTopicEventListener) */
   public static boolean subscribeVetoListener(Pattern topicPattern, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListener(topicPattern, vetoListener);
   }

   /** @see EventService#subscribeVetoListenerStrongly(Class,VetoEventListener) */
   public static boolean subscribeVetoListenerStrongly(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerStrongly(eventClass, vetoListener);
   }

   /** @see EventService#subscribeVetoListenerExactlyStrongly(Class,VetoEventListener) */
   public static boolean subscribeVetoListenerExactlyStrongly(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerExactlyStrongly(eventClass, vetoListener);
   }

   /** @see EventService#subscribeVetoListenerStrongly(String,VetoTopicEventListener) */
   public static boolean subscribeVetoListenerStrongly(String topic, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerStrongly(topic, vetoListener);
   }

   /** @see EventService#subscribeVetoListener(String,VetoTopicEventListener) */
   public static boolean subscribeVetoListenerStrongly(Pattern topicPattern, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().subscribeVetoListenerStrongly(topicPattern, vetoListener);
   }

   /** @see EventService#unsubscribeVetoListener(Class,VetoEventListener) */
   public static boolean unsubscribeVetoListener(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoListener(eventClass, vetoListener);
   }

   /** @see EventService#unsubscribeVetoListenerExactly(Class,VetoEventListener) */
   public static boolean unsubscribeVetoListenerExactly(Class eventClass, VetoEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoListenerExactly(eventClass, vetoListener);
   }

   /** @see EventService#unsubscribeVetoListener(String,VetoTopicEventListener) */
   public static boolean unsubscribeVetoListener(String topic, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoListener(topic, vetoListener);
   }

   /** @see EventService#unsubscribeVetoListener(Pattern,VetoTopicEventListener) */
   public static boolean unsubscribeVetoListener(Pattern topicPattern, VetoTopicEventListener vetoListener) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoListener(topicPattern, vetoListener);
   }

   /** @see EventService#getSubscribers(Class) */
   public static <T> List<T> getSubscribers(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getSubscribers(eventClass);
   }   

   /** @see EventService#getSubscribersToClass(Class) */
   public static <T> List<T> getSubscribersToClass(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getSubscribersToClass(eventClass);
   }

   /** @see EventService#getSubscribersToExactClass(Class) */
   public static <T> List<T> getSubscribersToExactClass(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getSubscribersToExactClass(eventClass);
   }

   /** @see EventService#getSubscribers(Type) */
   public static <T> List<T> getSubscribers(Type type) {
      return EventServiceLocator.getEventBusService().getSubscribers(type);
   }

   /** @see EventService#getSubscribers(String) */
   public static <T> List<T> getSubscribers(String topic) {
      return EventServiceLocator.getEventBusService().getSubscribers(topic);
   }

   /** @see EventService#getSubscribersToTopic(String) */
   public static <T> List<T> getSubscribersToTopic(String topic) {
      return EventServiceLocator.getEventBusService().getSubscribersToTopic(topic);
   }

   /** @see EventService#getSubscribers(Pattern) */
   public static <T> List<T> getSubscribers(Pattern pattern) {
      return EventServiceLocator.getEventBusService().getSubscribers(pattern);
   }

   /** @see EventService#getSubscribersByPattern(String) */
   public static <T> List<T> getSubscribersByPattern(String topic) {
      return EventServiceLocator.getEventBusService().getSubscribersByPattern(topic);
   }

   /** @see EventService#getSubscribers(Class) */
   public static <T> List<T> getVetoSubscribers(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getVetoSubscribers(eventClass);
   }

   /** @see EventService#getVetoSubscribersToClass(Class) */
   public static <T> List<T> getVetoSubscribersToClass(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getVetoSubscribersToClass(eventClass);
   }

   /** @see EventService#getVetoSubscribersToExactClass(Class) */
   public static <T> List<T> getVetoSubscribersToExactClass(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getVetoSubscribersToExactClass(eventClass);
   }

   /** @see EventService#getVetoSubscribers(Class)
    *  @deprecated use getVetoSubscribersToTopic instead for direct replacement,
    *             or use getVetoEventListeners to get topic and pattern matchers.
    *             In EventBus 2.0 this name will replace getVetoEventListeners()
    *             and have it's union functionality
    */
   public static <T> List<T> getVetoSubscribers(String topic) {
      return EventServiceLocator.getEventBusService().getVetoSubscribers(topic);
   }

   /** @see EventService#getVetoEventListeners(String) */
   public static <T> List<T> getVetoEventListeners(String topic) {
      return EventServiceLocator.getEventBusService().getVetoEventListeners(topic);
   }

   /** @see EventService#getVetoSubscribers(Pattern) */
   public static <T> List<T> getVetoSubscribers(Pattern pattern) {
      return EventServiceLocator.getEventBusService().getVetoSubscribers(pattern);
   }

   /** @see EventService#getVetoSubscribersToTopic(String) */
   public static <T> List<T> getVetoSubscribersToTopic(String topic) {
      return EventServiceLocator.getEventBusService().getVetoSubscribersToTopic(topic);
   }

   /** @see EventService#getVetoSubscribersByPattern(String) */
   public static <T> List<T> getVetoSubscribersByPattern(String topic) {
      return EventServiceLocator.getEventBusService().getVetoSubscribersByPattern(topic);
   }

   /** @see EventService#unsubscribeVeto(Class, Object) */
   public static boolean unsubscribeVeto(Class eventClass, Object subscribedByProxy) {
      return EventServiceLocator.getEventBusService().unsubscribeVeto(eventClass, subscribedByProxy);
   }

   /** @see EventService#unsubscribeVetoExactly(Class, Object) */
   public static boolean unsubscribeVetoExactly(Class eventClass, Object subscribedByProxy) {
      return EventServiceLocator.getEventBusService().unsubscribeVetoExactly(eventClass, subscribedByProxy);
   }

   /** @see EventService#unsubscribeVeto(String, Object) */
   public static boolean unsubscribeVeto(String topic, Object subscribedByProxy) {
      return EventServiceLocator.getEventBusService().unsubscribeVeto(topic, subscribedByProxy);
   }

   /** @see EventService#unsubscribeVeto(Pattern, Object) */
   public static boolean unsubscribeVeto(Pattern pattern, Object subscribedByProxy) {
      return EventServiceLocator.getEventBusService().unsubscribeVeto(pattern, subscribedByProxy);
   }

   /** @see EventService#clearAllSubscribers() */
   public static void clearAllSubscribers() {
      EventServiceLocator.getEventBusService().clearAllSubscribers();
   }

   /** @see EventService#setDefaultCacheSizePerClassOrTopic(int) */
   public static void setDefaultCacheSizePerClassOrTopic(int defaultCacheSizePerClassOrTopic) {
      EventServiceLocator.getEventBusService().setDefaultCacheSizePerClassOrTopic(defaultCacheSizePerClassOrTopic);
   }

   /** @see org.bushe.swing.event.EventService#getDefaultCacheSizePerClassOrTopic() */
   public static int getDefaultCacheSizePerClassOrTopic() {
      return EventServiceLocator.getEventBusService().getDefaultCacheSizePerClassOrTopic();
   }

   /** @see EventService#setCacheSizeForEventClass(Class,int) */
   public static void setCacheSizeForEventClass(Class eventClass, int cacheSize) {
      EventServiceLocator.getEventBusService().setCacheSizeForEventClass(eventClass, cacheSize);
   }

   /** @see EventService#getCacheSizeForEventClass(Class) */
   public static int getCacheSizeForEventClass(Class eventClass) {
      return EventServiceLocator.getEventBusService().getCacheSizeForEventClass(eventClass);
   }

   /** @see EventService#setCacheSizeForTopic(String,int) */
   public static void setCacheSizeForTopic(String topicName, int cacheSize) {
      EventServiceLocator.getEventBusService().setCacheSizeForTopic(topicName, cacheSize);
   }

   /** @see EventService#setCacheSizeForTopic(java.util.regex.Pattern,int) */
   public static void setCacheSizeForTopic(Pattern pattern, int cacheSize) {
      EventServiceLocator.getEventBusService().setCacheSizeForTopic(pattern, cacheSize);
   }

   /** @see EventService#getCacheSizeForTopic(String) */
   public static int getCacheSizeForTopic(String topic) {
      return EventServiceLocator.getEventBusService().getCacheSizeForTopic(topic);
   }

   /** @see EventService#getLastEvent(Class) */
   public static <T> T getLastEvent(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getLastEvent(eventClass);
   }

   /** @see EventService#getCachedEvents(Class) */
   public static <T> List<T> getCachedEvents(Class<T> eventClass) {
      return EventServiceLocator.getEventBusService().getCachedEvents(eventClass);
   }

   /** @see EventService#getLastTopicData(String) */
   public static Object getLastTopicData(String topic) {
      return EventServiceLocator.getEventBusService().getLastTopicData(topic);
   }

   /** @see EventService#getCachedTopicData(String) */
   public static List getCachedTopicData(String topic) {
      return EventServiceLocator.getEventBusService().getCachedTopicData(topic);
   }

   /** @see EventService#clearCache(Class) */
   public static void clearCache(Class eventClass) {
      EventServiceLocator.getEventBusService().clearCache(eventClass);
   }

   /** @see EventService#clearCache(String) */
   public static void clearCache(String topic) {
      EventServiceLocator.getEventBusService().clearCache(topic);
   }

   /** @see EventService#clearCache(java.util.regex.Pattern) */
   public static void clearCache(Pattern pattern) {
      EventServiceLocator.getEventBusService().clearCache(pattern);
   }

   /** @see org.bushe.swing.event.EventService#clearCache() */
   public static void clearCache() {
      EventServiceLocator.getEventBusService().clearCache();
   }
}
