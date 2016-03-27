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
 * The core interface.  An EventService provides publish/subscribe services to a single JVM using Class-based and
 * String-based (i.e. "topic") publications and subscriptions.
 * <p/>
 * In class-based pub/sub, {@link EventSubscriber}s subscribe to a type on an {@link EventService}, such
 * as the {@link org.bushe.swing.event.EventBus}, by providing a class, interface or generic type.  The EventService
 * notifies subscribers when objects are published on the EventService with a matching type.  Full class semantics are
 * respected.  That is, if a subscriber subscribes to a class, the subscriber is notified if an object of
 * that class is publish or if an object of a subclass of that class is published. Likewise if a subscriber subscribes
 * to an interface, it will be notified if any object that implements that interface is published.  Subscribers can
 * subscribe "exactly" using  {@link #subscribeExactly(Class, EventSubscriber)} so that they are notified only if an
 * object of the exact class is published (and will not be notified if subclasses are published, since this would not
 * be "exact")
 * <p/>
 * In topic-based pub/sub, an object "payload" is published on a topic name (String).  {@link EventTopicSubscriber}s subscribe
 * to either the exact name of the topic or they may subscribe using a Regular Expression that is used to match topic
 * names.
 * <p/>
 * See the <a href="../../../../overview-summary.html#overview_description">overview</a> for an general introduction
 * and <a href="package-summary.html">package documentation</a> for usage details and examples.
 * <p/>
 * A single subscriber cannot subscribe more than once to an event or topic name.  EventService implementations should
 * handle double-subscription requests by returning false on subscribe().  A single EventSubscriber can subscribe to more
 * than one event class, and a single EventTopicSubscriber can subscribe to more than one topic name or pattern. A
 * single object may implement both EventSubscriber and EventTopicSubscriber interfaces.  Subscribers are guaranteed to
 * only be called for the classes and/or topic names they subscribe to.  If a subscriber subscribes to a topic and to a
 * regular expression that matches the topic name, this is considered two different subscriptions and the subscriber
 * will be called twice for the publication on the topic.  Similarly, if a subscriber subscribes to a class and its
 * subclasses using subscribe() and again to a class of the same type using subscribeExactly(), this is considered two
 * different subscriptions and the subscriber will be called twice for the publication for a single event of the exact
 * type.
 * <p/>
 * By default the EventService only holds WeakReferences to subscribers.  If a subscriber has no references to it, then
 * it can be garbage collected.  This avoids memory leaks in exchange for the risk of accidentally adding a listener and
 * have it disappear unexpectedly.  If you want to subscribe a subscriber that will have no other reference to it, then
 * use one of the subscribeStrongly() methods, which will prevent garbage collection.
 * <p/>
 * Unless garbage collected, EventSubscribers will remain subscribed until they are passed to one of the unsubscribe()
 * methods with the event class or topic name to which there are subscribed.
 * <p/>
 * Subscribers are called in the order in which they are subscribed by default (FIFO), unless subscribers implement
 * {@link Prioritized}.  Those subscribers that implement Prioritized and return a negative priority are moved to the
 * front of the list (the more negative, the more to the front).  Those subscribers that implement Prioritized and return
 * a positive priority are moved to the end of the list (the more positive, the more to the back).  The FIFO guarantee
 * is only valid for the same subscribe() call.  That is, the order of two subscribers, one to List.class and the other
 * to ArrayList.class is not guaranteed to be in the order of subscription when an ArrayList is published.  The same is
 * true for topic subscribers when using RegEx expressions - when "Foo" is published, the order of subscribers that are
 * subscribed to "Foo", "Fo*" and "F*" are not guaranteed, though the second "Fo*" subscriber will never be called
 * before the first "Fo*" subscriber (ditto List and ArrayList). Prioritized subscribers are always guaranteed to be in
 * the order of priority, no matter the call or the resulting mix of subscribers.  All ordering rules apply to all
 * types subscribers: class, topic, pattern, veto, etc.  For Swing users, note that FIFO is
 * the opposite of Swing, where event listeners are called in the reverse order of when they were subscribed (FILO).    
 * <p/>
 * Publication on a class or topic name can be vetoed by a {@link VetoEventListener}. All VetoEventListeners are checked
 * before any EventSubscribers or EventTopicSubscribers are called. This is unlike the JavaBean's
 * VetoPropertyEventListener which can leave side effects and half-propogated events. VetoEventListeners are subscribed
 * in the same manner as EventSubscribers and EventTopicSubscribers.
 * <p/>
 * The state of a published event can be tracked if an event or a topic's payload object implements the
 * {@link org.bushe.swing.event.PublicationStatus} interface.  EventServices are required to set such objects'
 * {@link org.bushe.swing.event.PublicationStatus} at the appropriate times during publication.
 * <p/>
 * This simple example prints "Hello World"
 * <pre>
 * EventService eventService = new ThreadSafeEventService();
 * //Create a subscriber
 * EventTopicSubscriber subscriber = new EventTopicSubscriber() {
 *    public void onEvent(String topic, Object event) {
 *        System.out.println(topic+" "+event);
 *    }
 * });
 * eventService.subscribe("Hello", subscriber);
 * eventService.publish("Hello", "World");
 * System.out.println(subscriber + " Since the reference is used after it is subscribed, it doesn't get garbage collected, this is not necessary if you use subscribeStrongly()");
 * </pre>
 * <p/>
 * Events and/or topic data can be cached, but are not by default.  To cache events or topic data, call
 * {@link #setDefaultCacheSizePerClassOrTopic(int)}, {@link #setCacheSizeForEventClass(Class, int)}, or
 * {@link #setCacheSizeForTopic(String, int)}, {@link #setCacheSizeForTopic(Pattern, int)}.  Retrieve cached values
 * with {@link #getLastEvent(Class)}, {@link #getLastTopicData(String)}, {@link #getCachedEvents(Class)}, or
 * {@link #getCachedTopicData(String)}.  Using caching while subscribing is most likely to make sense only if you
 * subscribe and publish on the same thread (so caching is very useful for Swing applications since both happen on
 * the EDT in a single-threaded manner). In multithreaded applications, you never know if your subscriber has handled
 * an event while it was being subscribed (before the subscribe() method returned) that is newer or older than the
 * retrieved cached value (taken before or after subscribe() respectively).
 * <p/>
 * There is nothing special about the term "Event," this could just as easily be called a "Message" Service, this term
 * is already taken by the JMS, which is similar, but is used across processes and networks.
 *
 * @author Michael Bushe michael@bushe.com
 * @see {@link ThreadSafeEventService} for the default implementation
 * @see {@link SwingEventService} for the Swing-safe implementation
 * @see {@link EventBus} for simple access to the Swing-safe implementation
 * @see {@link org.bushe.swing.event.annotation.EventSubscriber} for subscription annotations
 * @see {@link org.bushe.swing.event.annotation.EventTopicSubscriber} for subscription annotations
 */
public interface EventService {

   /**
    * Publishes an object so that subscribers will be notified if they subscribed to the object's class, one of its
    * subclasses, or to one of the interfaces it implements.
    *
    * @param event the object to publish
    */
   public void publish(Object event);

   /**
    * Use this method to publish generified objects to subscribers of Types, i.e. subscribers that use
    * {@link #subscribe(Type, EventSubscriber)}, and to publish to subscribers of the non-generic type.
    * <p>
    * Due to generic type erasure, the type must be supplied by the caller.  You can get a declared object's
    * type by using the {@link org.bushe.swing.event.generics.TypeReference} class.  For Example:
    * <pre>
    * TypeReference&#60;List&#60;Trade&#62;&#62; subscribingTypeReference = new TypeReference&#60;List&#60;Trade&#62;&#62;(){};
    * EventBus.subscribe(subscribingTypeReference.getType(), mySubscriber);
    * EventBus.subscribe(List.class, thisSubscriberWillGetCalledToo);
    * ...
    * //Likely in some other class
    * TypeReference&#60;List&#60;Trade&#62;&#62; publishingTypeReference = new TypeReference&#60;List&#60;Trade&#62;&#62;(){};
    * List&#60;Trade&#62; trades = new ArrayList&#60;Trade&#62;();
    * EventBus.publish(publishingTypeReference.getType(), trades);
    * trades.add(trade);
    * EventBus.publish(publishingTypeReference.getType(), trades);
    * </pre>
    * <p>
    * @param genericType the generified type of the published object.  
    * @param event The event that occurred
    */
   public void publish(Type genericType, Object event);

   /**
    * Publishes an object on a topic name so that all subscribers to that name or a Regular Expression that matches
    * the topic name will be notified.
    *
    * @param topic The name of the topic subscribed to
    * @param o the object to publish
    */
   public void publish(String topic, Object o);

   /**
    * Subscribes an EventSubscriber to the publication of objects matching a type.  Only a <b>WeakReference</b> to
    * the subscriber is held by the EventService.
    * <p/>
    * Subscribing to a class means the subscriber will be called when objects of that class are published, when
    * objects of subclasses of the class are published, when objects implementing any of the interfaces of the
    * class are published, or when generic types are published with the class' raw type.
    * <p/>
    * Subscription is weak by default to avoid having to call unsubscribe(), and to avoid the memory leaks that would
    * occur if unsubscribe was not called.  The service will respect the WeakReference semantics.  In other words, if
    * the subscriber has not been garbage collected, then onEvent(Object) will be called normally.  If the hard
    * reference has been garbage collected, the service will unsubscribe it's WeakReference.
    * <p/>
    * It's allowable to call unsubscribe() with the same EventSubscriber hard reference to stop a subscription
    * immediately.
    * <p/>
    * The service will create the WeakReference on behalf of the caller.
    *
    * @param eventClass the class of published objects to subscriber listen to
    * @param subscriber The subscriber that will accept the events of the event class when published.
    *
    * @return true if the subscriber was subscribed successfully, false otherwise
    */
   public boolean subscribe(Class eventClass, EventSubscriber subscriber);

  /** 
   * Subscribe an EventSubscriber to publication of generic Types.
   * Subscribers will only be notified for publications using  {@link #publish(java.lang.reflect.Type, Object)}.
   * <p>
   * Due to generic type erasure, the type must be supplied by the publisher.  You can get a declared object's
   * type by using the {@link org.bushe.swing.event.generics.TypeReference} class.  For Example:
   * <pre>
   * TypeReference&#60;List&#60;Trade&#62;&#62; subscribingTypeReference = new TypeReference&#60;List&#60;Trade&#62;&#62;(){};
   * EventBus.subscribe(subscribingTypeReference.getType(), mySubscriber);
   * EventBus.subscribe(List.class, thisSubscriberWillGetCalledToo);
   * ...
   * //Likely in some other class
   * TypeReference&#60;List&#60;Trade&#62;&#62; publishingTypeReference = new TypeReference&#60;List&#60;Trade&#62;&#62;(){};
   * List&#60;Trade&#62; trades = new ArrayList&#60;Trade&#62;();
   * EventBus.publish(publishingTypeReference.getType(), trades);
   * trades.add(trade);
   * EventBus.publish(publishingTypeReference.getType(), trades);
   * </pre>
   * <p>
   * @param type the generic type to subscribe to
   * @param subscriber the subscriber to the type
   * @return true if a new subscription is made, false if it already existed
   */
   public boolean subscribe(Type type, EventSubscriber subscriber);

   /**
    * Subscribes an EventSubscriber to the publication of objects exactly matching a type.  Only a <b>WeakReference</b>
    * to the subscriber is held by the EventService.
    * <p/>
    * Subscription is weak by default to avoid having to call unsubscribe(), and to avoid the memory leaks that would
    * occur if unsubscribe was not called.  The service will respect the WeakReference semantics.  In other words, if
    * the subscriber has not been garbage collected, then the onEvent will be called normally.  If the hard reference
    * has been garbage collected, the service will unsubscribe it's WeakReference.
    * <p/>
    * It's allowable to call unsubscribe() with the same EventSubscriber hard reference to stop a subscription
    * immediately.
    * <p/>
    * The service will create the WeakReference on behalf of the caller.
    *
    * @param eventClass the class of published objects to listen to
    * @param subscriber The subscriber that will accept the events when published.
    *
    * @return true if the subscriber was subscribed successfully, false otherwise
    */
   public boolean subscribeExactly(Class eventClass, EventSubscriber subscriber);

   /**
    * Subscribes an EventTopicSubscriber to the publication of a topic name.  Only a <b>WeakReference</b>
    * to the subscriber is held by the EventService.
    * <p/>
    * Subscription is weak by default to avoid having to call unsubscribe(), and to avoid the memory leaks that would
    * occur if unsubscribe was not called.  The service will respect the WeakReference semantics.  In other words, if
    * the subscriber has not been garbage collected, then the onEvent will be called normally.  If the hard reference
    * has been garbage collected, the service will unsubscribe it's WeakReference.
    * <p/>
    * It's allowable to call unsubscribe() with the same EventSubscriber hard reference to stop a subscription
    * immediately.
    * <p/>
    *
    * @param topic the name of the topic listened to
    * @param subscriber The topic subscriber that will accept the events when published.
    *
    * @return true if the subscriber was subscribed successfully, false otherwise
    */
   public boolean subscribe(String topic, EventTopicSubscriber subscriber);

   /**
    * Subscribes an EventSubscriber to the publication of all the topic names that match a RegEx Pattern.  Only a
    * <b>WeakReference</b> to the subscriber is held by the EventService.
    * <p/>
    * Subscription is weak by default to avoid having to call unsubscribe(), and to avoid the memory leaks that would
    * occur if unsubscribe was not called.  The service will respect the WeakReference semantics.  In other words, if
    * the subscriber has not been garbage collected, then the onEvent will be called normally.  If the hard reference
    * has been garbage collected, the service will unsubscribe it's WeakReference.
    * <p/>
    * It's allowable to call unsubscribe() with the same EventSubscriber hard reference to stop a subscription
    * immediately.
    * <p/>
    *
    * @param topicPattern pattern that matches to the name of the topic published to
    * @param subscriber The topic subscriber that will accept the events when published.
    *
    * @return true if the subscriber was subscribed successfully, false otherwise
    */
   public boolean subscribe(Pattern topicPattern, EventTopicSubscriber subscriber);

   /**
    * Subscribes an EventSubscriber to the publication of objects matching a type.
    * <p/>
    * The semantics are the same as {@link #subscribe(Class, EventSubscriber)}, except that the EventService holds
    * a regularly reference, not a WeakReference.
    * <p/>
    * The subscriber will remain subscribed until {@link #unsubscribe(Class,EventSubscriber)}  is called.
    *
    * @param eventClass the class of published objects to listen to
    * @param subscriber The subscriber that will accept the events when published.
    *
    * @return true if the subscriber was subscribed successfully, false otherwise
    */
   public boolean subscribeStrongly(Class eventClass, EventSubscriber subscriber);

   /**
    * Subscribes an EventSubscriber to the publication of objects matching a type exactly.
    * <p/>
    * The semantics are the same as {@link #subscribeExactly(Class, EventSubscriber)}, except that the EventService
    * holds a regularly reference, not a WeakReference.
    * <p/>
    * The subscriber will remain subscribed until {@link #unsubscribe(Class,EventSubscriber)}  is called.
    *
    * @param eventClass the class of published objects to listen to
    * @param subscriber The subscriber that will accept the events when published.
    *
    * @return true if the subscriber was subscribed successfully, false otherwise
    */
   public boolean subscribeExactlyStrongly(Class eventClass, EventSubscriber subscriber);

   /**
    * Subscribes a subscriber to an event topic name.
    * <p/>
    * The semantics are the same as {@link #subscribe(String, EventTopicSubscriber)}, except that the EventService
    * holds a regularly reference, not a WeakReference.
    * <p/>
    * The subscriber will remain subscribed until {@link #unsubscribe(String,EventTopicSubscriber)}  is called.
    *
    * @param topic the name of the topic listened to
    * @param subscriber The topic subscriber that will accept the events when published.
    *
    * @return true if the subscriber was subscribed successfully, false otherwise
    */
   public boolean subscribeStrongly(String topic, EventTopicSubscriber subscriber);

   /**
    * Subscribes a subscriber to all the event topic names that match a RegEx expression.
    * <p/>
    * The semantics are the same as {@link #subscribe(java.util.regex.Pattern, EventTopicSubscriber)}, except that the
    * EventService holds a regularly reference, not a WeakReference.
    * <p/>
    * The subscriber will remain subscribed until {@link #unsubscribe(String,EventTopicSubscriber)}  is called.
    *
    * @param topicPattern the name of the topic listened to
    * @param subscriber The topic subscriber that will accept the events when published.
    *
    * @return true if the subscriber was subscribed successfully, false otherwise
    */
   public boolean subscribeStrongly(Pattern topicPattern, EventTopicSubscriber subscriber);

   /**
    * Stop the subscription for a subscriber that is subscribed to a class.
    *
    * @param eventClass the class of published objects to listen to
    * @param subscriber The subscriber that is subscribed to the event.  The same reference as the one subscribed.
    *
    * @return true if the subscriber was subscribed to the event, false if it wasn't
    */
   public boolean unsubscribe(Class eventClass, EventSubscriber subscriber);

   /**
    * Stop the subscription for a subscriber that is subscribed to an exact class.
    *
    * @param eventClass the class of published objects to listen to
    * @param subscriber The subscriber that is subscribed to the event.  The same reference as the one subscribed.
    *
    * @return true if the subscriber was subscribed to the event, false if it wasn't
    */
   public boolean unsubscribeExactly(Class eventClass, EventSubscriber subscriber);

   /**
    * Stop the subscription for a subscriber that is subscribed to an event topic.
    *
    * @param topic the topic listened to
    * @param subscriber The subscriber that is subscribed to the topic. The same reference as the one subscribed.
    *
    * @return true if the subscriber was subscribed to the event, false if it wasn't
    */
   public boolean unsubscribe(String topic, EventTopicSubscriber subscriber);

   /**
    * Stop the subscription for a subscriber that is subscribed to event topics via a Pattern.
    *
    * @param topicPattern the regex expression matching topics listened to
    * @param subscriber The subscriber that is subscribed to the topic. The same reference as the one subscribed.
    *
    * @return true if the subscriber was subscribed to the event, false if it wasn't
    */
   public boolean unsubscribe(Pattern topicPattern, EventTopicSubscriber subscriber);

   /**
    * Subscribes a VetoEventListener to publication of event matching a class.  Only a <b>WeakReference</b> to the 
    * VetoEventListener is held by the EventService.
    * <p/>
    * Use this method to avoid having to call unsubscribe(), though with care since garbage collection semantics is
    * indeterminate.  The service will respect the WeakReference semantics.  In other words, if the vetoListener has not
    * been garbage collected, then the onEvent will be called normally.  If the hard reference has been garbage
    * collected, the service will unsubscribe it's WeakReference.
    * <p/>
    * It's allowable to call unsubscribe() with the same VetoEventListener hard reference to stop a subscription
    * immediately.
    * <p/>
    * The service will create the WeakReference on behalf of the caller.
    *
    * @param eventClass the class of published objects that can be vetoed
    * @param vetoListener The VetoEventListener that can determine whether an event is published.
    *
    * @return true if the VetoEventListener was subscribed successfully, false otherwise
    */
   public boolean subscribeVetoListener(Class eventClass, VetoEventListener vetoListener);

   /**
    * Subscribes a VetoEventListener to publication of an exact event class.  Only a <b>WeakReference</b> to the 
    * VetoEventListener is held by the EventService.
    * <p/>
    * Use this method to avoid having to call unsubscribe(), though with care since garbage collection semantics is
    * indeterminate.  The service will respect the WeakReference semantics.  In other words, if the vetoListener has not
    * been garbage collected, then the onEvent will be called normally.  If the hard reference has been garbage
    * collected, the service will unsubscribe it's WeakReference.
    * <p/>
    * It's allowable to call unsubscribe() with the same VetoEventListener hard reference to stop a subscription
    * immediately.
    * <p/>
    * The service will create the WeakReference on behalf of the caller.
    *
    * @param eventClass the class of published objects that can be vetoed
    * @param vetoListener The vetoListener that can determine whether an event is published.
    *
    * @return true if the vetoListener was subscribed successfully, false otherwise
    */
   public boolean subscribeVetoListenerExactly(Class eventClass, VetoEventListener vetoListener);

   /**
    * Subscribes a VetoTopicEventListener to a topic name.  Only a <b>WeakReference</b> to the
    * VetoEventListener is held by the EventService.
    *
    * @param topic the name of the topic listened to
    * @param vetoListener The vetoListener that can determine whether an event is published.
    *
    * @return true if the vetoListener was subscribed successfully, false otherwise
    */
   public boolean subscribeVetoListener(String topic, VetoTopicEventListener vetoListener);

   /**
    * Subscribes an VetoTopicEventListener to all the topic names that match the RegEx Pattern.  Only a
    * <b>WeakReference</b> to the VetoEventListener is held by the EventService.  
    *
    * @param topicPattern the RegEx pattern to match topics with
    * @param vetoListener The vetoListener that can determine whether an event is published.
    *
    * @return true if the vetoListener was subscribed successfully, false otherwise
    */
   public boolean subscribeVetoListener(Pattern topicPattern, VetoTopicEventListener vetoListener);

   /**
    * Subscribes a VetoEventListener for an event class and its subclasses.  Only a <b>WeakReference</b> to the
    * VetoEventListener is held by the EventService.
    * <p/>
    * The VetoEventListener will remain subscribed until {@link #unsubscribeVetoListener(Class,VetoEventListener)} is
    * called.
    *
    * @param eventClass the class of published objects to listen to
    * @param vetoListener The vetoListener that will accept the events when published.
    *
    * @return true if the vetoListener was subscribed successfully, false otherwise
    */
   public boolean subscribeVetoListenerStrongly(Class eventClass, VetoEventListener vetoListener);

   /**
    * Subscribes a VetoEventListener for an event class (but not its subclasses).
    * <p/>
    * The VetoEventListener will remain subscribed until {@link #unsubscribeVetoListener(Class,VetoEventListener)} is
    * called.
    *
    * @param eventClass the class of published objects to listen to
    * @param vetoListener The vetoListener that will accept the events when published.
    *
    * @return true if the vetoListener was subscribed successfully, false otherwise
    */
   public boolean subscribeVetoListenerExactlyStrongly(Class eventClass, VetoEventListener vetoListener);

   /**
    * Subscribes a VetoEventListener to a topic name.
    * <p/>
    * The VetoEventListener will remain subscribed until {@link #unsubscribeVetoListener(String,VetoTopicEventListener)} is
    * called.
    *
    * @param topic the name of the topic listened to
    * @param vetoListener The topic vetoListener that will accept or reject publication.
    *
    * @return true if the vetoListener was subscribed successfully, false otherwise
    *
    * @see #subscribeVetoListenerStrongly(Class,VetoEventListener)
    */
   public boolean subscribeVetoListenerStrongly(String topic, VetoTopicEventListener vetoListener);

   /**
    * Subscribes a VetoTopicEventListener to a set of topics that match a RegEx expression.
    * <p/>
    * The VetoEventListener will remain subscribed until {@link #unsubscribeVetoListener(Pattern,VetoTopicEventListener)} is
    * called.
    *
    * @param topicPattern the RegEx pattern that matches the name of the topics listened to
    * @param vetoListener The topic vetoListener that will accept or reject publication.
    *
    * @return true if the vetoListener was subscribed successfully, false otherwise
    *
    * @see #subscribeVetoListenerStrongly(Pattern,VetoTopicEventListener)
    */
   public boolean subscribeVetoListenerStrongly(Pattern topicPattern, VetoTopicEventListener vetoListener);

   /**
    * Stop the subscription for a vetoListener that is subscribed to an event class and its subclasses.
    *
    * @param eventClass the class of published objects that can be vetoed
    * @param vetoListener The vetoListener that will accept or reject publication of an event.
    *
    * @return true if the vetoListener was subscribed to the event, false if it wasn't
    */
   public boolean unsubscribeVetoListener(Class eventClass, VetoEventListener vetoListener);

   /**
    * Stop the subscription for a vetoListener that is subscribed to an event class (but not its subclasses).
    *
    * @param eventClass the class of published objects that can be vetoed
    * @param vetoListener The vetoListener that will accept or reject publication of an event.
    *
    * @return true if the vetoListener was subscribed to the event, false if it wasn't
    */
   public boolean unsubscribeVetoListenerExactly(Class eventClass, VetoEventListener vetoListener);

   /**
    * Stop the subscription for a VetoTopicEventListener that is subscribed to an event topic name.
    *
    * @param topic the name of the topic that is listened to
    * @param vetoListener The vetoListener that can determine whether an event is published on that topic
    *
    * @return true if the vetoListener was subscribed to the topic, false if it wasn't
    */
   public boolean unsubscribeVetoListener(String topic, VetoTopicEventListener vetoListener);

   /**
    * Stop the subscription for a VetoTopicEventListener that is subscribed to an event topic RegEx pattern.
    *
    * @param topicPattern the RegEx pattern matching the name of the topics listened to
    * @param vetoListener The vetoListener that can determine whether an event is published on that topic
    *
    * @return true if the vetoListener was subscribed to the topicPattern, false if it wasn't
    */
   public boolean unsubscribeVetoListener(Pattern topicPattern, VetoTopicEventListener vetoListener);

   /**
    * Union of getSubscribersToClass(Class) and getSubscribersToExactClass(Class)
    *
    * @param eventClass the eventClass of interest
    *
    * @return the subscribers that will be called when an event of eventClass is published, this includes those
    *         subscribed that match by exact class and those that match to a class and its subtypes
    */
   public <T> List<T> getSubscribers(Class<T> eventClass);

   /**
    * Gets subscribers that subscribed with the given a class, but not those subscribed exactly to the class.
    * @param eventClass the eventClass of interest
    *
    * @return the subscribers that are subscribed to match to a class and its subtypes, but not those subscribed by
    *         exact class
    */
   public <T> List<T> getSubscribersToClass(Class<T> eventClass);

   /**
    * Gets subscribers that are subscribed exactly to a class, but not those subscribed non-exactly to a class.
    * @param eventClass the eventClass of interest
    *
    * @return the subscribers that are subscribed by exact class but not those subscribed to match to a class and its
    *         subtypes
    */
   public <T> List<T> getSubscribersToExactClass(Class<T> eventClass);

   /**
    * Gets the subscribers that subscribed to a generic type.
    *
    * @param type the type of interest
    *
    * @return the subscribers that will be called when an event of eventClass is published, this includes those
    *         subscribed that match by exact class and those that match to a class and its subtypes
    */
   public <T> List<T> getSubscribers(Type type);

   /**
    * Union of getSubscribersByPattern(String) and geSubscribersToTopic(String)
    *
    * @param topic the topic of interest
    *
    * @return the subscribers that will be called when an event is published on the topic.  This includes subscribers
    *         subscribed to match the exact topic name and those subscribed by a RegEx Pattern that matches the topic
    *         name.
    */
   public <T> List<T> getSubscribers(String topic);

   /**
    * Get the subscribers that subscribed to a topic.
    * @param topic the topic of interest
    *
    * @return the subscribers that subscribed to the exact topic name.
    */
   public <T> List<T>  getSubscribersToTopic(String topic);

   /**
    * Gets the subscribers that subscribed to a regular expression.
    * @param pattern the RegEx pattern that was subscribed to
    *
    * @return the subscribers that were subscribed to this pattern.
    */
   public <T> List<T> getSubscribers(Pattern pattern);

   /**
    * Gets the subscribers that subscribed with a Pattern that matches the given topic.
    * @param topic a topic to match Patterns against
    *
    * @return the subscribers that subscribed by a RegEx Pattern that matches the topic name.
    */
   public <T> List<T> getSubscribersByPattern(String topic);

   /**
    * Gets veto subscribers that subscribed to a given class.
    * @param eventClass the eventClass of interest
    *
    * @return the veto subscribers that will be called when an event of eventClass or its subclasses is published.
    */
   public <T> List<T>  getVetoSubscribers(Class<T> eventClass);

   /**
    * Get veto subscribers that subscribed to a given class exactly.
    * @param eventClass the eventClass of interest
    *
    * @return the veto subscribers that will be called when an event of eventClass (but not its subclasses) is
    *         published.
    */
   public <T> List<T> getVetoSubscribersToExactClass(Class<T> eventClass);

   /**
    * Gets the veto subscribers that subscribed to a class.
    * @param eventClass the eventClass of interest
    *
    * @return the veto subscribers that are subscribed to the eventClass and its subclasses
    */
   public <T> List<T> getVetoSubscribersToClass(Class<T> eventClass);

   /**
    * Union of {@link #getVetoSubscribersToTopic(String)} and {@link #getVetoSubscribersByPattern(String)}
    * Misnamed method, should be called {@link #getVetoSubscribers(String)}. Will be deprecated in 1.5.
    *
    * @param topicOrPattern the topic or pattern of interest
    *
    * @return the veto subscribers that will be called when an event is published on the topic.
    */
   public <T> List<T> getVetoEventListeners(String topicOrPattern);

   /**
    * Gets the veto subscribers that subscribed to a topic.
    * @param topic the topic of interest
    *
    * @return the veto subscribers that will be called when an event is published on the topic.
    */
   public <T> List<T> getVetoSubscribersToTopic(String topic);

   /**
    * Gets the veto subscribers that subscribed to a regular expression.
    * @param pattern the RegEx pattern for the topic of interest
    *
    * @return the veto subscribers that were subscribed to this pattern.
    */
   public <T> List<T> getVetoSubscribers(Pattern pattern);

   /**
    * Gets the veto subscribers that are subscribed by pattern that match the topic.
    * @param topic the topic to match the pattern string subscribed to
    *
    * @return the veto subscribers that subscribed by pattern that will be called when an event is published on the topic.
    */
   public <T> List<T> getVetoSubscribersByPattern(String topic);

   /**
    * Misnamed method for backwards compatibility.
    * Duplicate of {@link #getVetoSubscribersToTopic(String)}.
    * Out of sync with {@link #getSubscribers(String)}.
    * @param topic the topic exactly subscribed to
    *
    * @return the veto subscribers that are subscribed to the topic.
    * @deprecated use getVetoSubscribersToTopic instead for direct replacement,
    *             or use getVetoEventListeners to get topic and pattern matchers.
    *             In EventBus 2.0 this name will replace getVetoEventListeners()
    *             and have it's union functionality
    */
   public <T> List<T> getVetoSubscribers(String topic);
      
   /** Clears all current subscribers and veto subscribers */
   public void clearAllSubscribers();

   /**
    * Sets the default cache size for each kind of event, default is 0 (no caching).
    * <p/>
    * If this value is set to a positive number, then when an event is published, the EventService caches the event or
    * topic payload data for later retrieval.  This allows subscribers to find out what has most recently happened
    * before they subscribed.  The cached event(s) are returned from #getLastEvent(Class), #getLastTopicData(String),
    * #getCachedEvents(Class), or #getCachedTopicData(String)
    * <p/>
    * The default can be overridden on a by-event-class or by-topic basis.
    *
    * @param defaultCacheSizePerClassOrTopic the cache size per event
    */
   public void setDefaultCacheSizePerClassOrTopic(int defaultCacheSizePerClassOrTopic);

   /**
    * The default number of events or payloads kept per event class or topic
    * @return the default number of event payloads kept per event class or topic
    */
   public int getDefaultCacheSizePerClassOrTopic();

   /**
    * Set the number of events cached for a particular class of event.  By default, no events are cached.
    * <p/>
    * This overrides any setting for the DefaultCacheSizePerClassOrTopic.
    * <p/>
    * Class hierarchy semantics are respected.  That is, if there are three events, A, X and Y, and X and Y are both
    * derived from A, then setting the cache size for A applies the cache size for all three.  Setting the cache size
    * for X applies to X and leaves the settings for A and Y in tact.  Interfaces can be passed to this method, but they
    * only take effect if the cache size of a class or it's superclasses has been set. Just like Class.getInterfaces(),
    * if multiple cache sizes are set, the interface names declared earliest in the implements clause of the eventClass
    * takes effect.
    * <p/>
    * The cache for an event is not adjusted until the next event of that class is published.
    *
    * @param eventClass the class of event
    * @param cacheSize the number of published events to cache for this event
    */
   public void setCacheSizeForEventClass(Class eventClass, int cacheSize);

   /**
    * Returns the number of events cached for a particular class of event.  By default, no events are cached.
    * <p/>
    * This result is computed for a particular class from the values passed to #setCacheSizeForEventClass(Class, int),
    * and respects the class hierarchy.
    *
    * @param eventClass the class of event
    *
    * @return the maximum size of the event cache for the given event class
    *
    * @see #setCacheSizeForEventClass(Class,int)
    */
   public int getCacheSizeForEventClass(Class eventClass);

   /**
    * Set the number of published data objects cached for a particular event topic.  By default, no data are cached.
    * <p/>
    * This overrides any setting for the DefaultCacheSizePerClassOrTopic.
    * <p/>
    * Exact topic names take precedence over pattern matching.
    * <p/>
    * The cache for a topic is not adjusted until the next publication on that topic.
    *
    * @param topicName the topic name
    * @param cacheSize the number of published data Objects to cache for this topic
    */
   public void setCacheSizeForTopic(String topicName, int cacheSize);

   /**
    * Set the number of published data objects cached for a topics matching a pattern.  By default, no data are cached.
    * <p/>
    * This overrides any setting for the DefaultCacheSizePerClassOrTopic.
    * <p/>
    * Exact topic names take precedence over pattern matching.
    * <p/>
    * The cache for a topic is not adjusted until the next publication on that topic.
    *
    * @param pattern the pattern matching topic names
    * @param cacheSize the number of data Objects to cache for this topic
    */
   public void setCacheSizeForTopic(Pattern pattern, int cacheSize);

   /**
    * Returns the number of cached data objects published on a particular topic.
    * <p/>
    * This result is computed for a particular class from the values passed to #setCacheSizeForEventClass(Class, int),
    * and respects the class hierarchy.
    *
    * @param topic the topic name
    *
    * @return the maximum size of the data Object cache for the given topic
    *
    * @see #setCacheSizeForTopic(String,int)
    * @see #setCacheSizeForTopic(java.util.regex.Pattern,int)
    */
   public int getCacheSizeForTopic(String topic);

   /**
    * When caching, returns the last event publish for the type supplied.
    * @param eventClass an index into the cache
    *
    * @return the last event published for this event class, or null if caching is turned off (the default)
    */
   public <T> T getLastEvent(Class<T> eventClass);

   /**
    * When caching, returns the last set of event published for the type supplied.
    * @param eventClass an index into the cache
    *
    * @return the last events published for this event class, or null if caching is turned off (the default)
    */
   public <T> List<T> getCachedEvents(Class<T> eventClass);

   /**
    * When caching, returns the last payload published on the topic name supplied.
    * @param topic an index into the cache
    *
    * @return the last data Object published on this topic, or null if caching is turned off (the default)
    */
   public Object getLastTopicData(String topic);

   /**
    * When caching, returns the last set of payload objects published on the topic name supplied.
    * @param topic an index into the cache
    *
    * @return the last data Objects published on this topic, or null if caching is turned off (the default)
    */
   public List getCachedTopicData(String topic);

   /**
    * Clears the event cache for a specific event class or interface and it's any of it's subclasses or implementing
    * classes.
    *
    * @param eventClass the event class to clear the cache for
    */
   public void clearCache(Class eventClass);

   /**
    * Clears the topic data cache for a specific topic name.
    *
    * @param topic the topic name to clear the cache for
    */
   public void clearCache(String topic);

   /**
    * Clears the topic data cache for all topics that match a particular pattern.
    *
    * @param pattern the pattern to match topic caches to
    */
   public void clearCache(Pattern pattern);

   /** Clear all event caches for all topics and event. */
   public void clearCache();

   /**
    * Stop a subscription for an object that is subscribed with a ProxySubscriber.
    * <p/>
    * If an object is subscribed by proxy and it implements EventSubscriber, then the normal unsubscribe methods will
    * still unsubscribe the object.
    *
    * @param eventClass class this object is subscribed to by proxy
    * @param subscribedByProxy object subscribed by proxy
    * @return true if the subscription was cancelled, false if it never existed
    */
   boolean unsubscribe(Class eventClass, Object subscribedByProxy);

   /**
    * Stop a subscription for an object that is subscribed exactly with a ProxySubscriber.
    * <p/>
    * If an object is subscribed by proxy and it implements EventSubscriber, then the normal unsubscribe methods will
    * still unsubscribe the object.
    *
    * @param eventClass class this object is subscribed to by proxy
    * @param subscribedByProxy object subscribed by proxy
    * @return true if the subscription was cancelled, false if it never existed
    */
   boolean unsubscribeExactly(Class eventClass, Object subscribedByProxy);

   /**
    * Stop a subscription for an object that is subscribed to a topic with a ProxySubscriber.
    * <p/>
    * If an object is subscribed by proxy and it implements EventSubscriber, then the normal unsubscribe methods will
    * still unsubscribe the object.
    *
    * @param topic the topic this object is subscribed to by proxy
    * @param subscribedByProxy object subscribed by proxy
    * @return true if the subscription was cancelled, false if it never existed
    */
   boolean unsubscribe(String topic, Object subscribedByProxy);

   /**
    * When using annotations, an object may be subscribed by proxy.  This unsubscribe method will unsubscribe an object
    * that is subscribed with a ProxySubscriber.
    * <p/>
    * If an object is subscribed by proxy and it implements EventSubscriber, then the normal unsubscribe methods will
    * still unsubscribe the object.
    *
    * @param pattern the RegEx expression this object is subscribed to by proxy
    * @param subscribedByProxy object subscribed by proxy
    * @return true if the subscription was cancelled, false if it never existed
    */
   boolean unsubscribe(Pattern pattern, Object subscribedByProxy);

   /**
    * Stop a veto subscription for an object that is subscribed with a ProxySubscriber.
    * <p/>
    * If an object is subscribed by proxy and it implements VetoSubscriber, then the normal unsubscribe methods will
    * still unsubscribe the object.
    *
    * @param eventClass class this object is subscribed to by proxy
    * @param subscribedByProxy object subscribed by proxy
    * @return true if the subscription was cancelled, false if it never existed
    */
   boolean unsubscribeVeto(Class eventClass, Object subscribedByProxy);

   /**
    * Stop a veto subscription for an object that is subscribed exactly with a ProxySubscriber.
    * <p/>
    * If an object is subscribed by proxy and it implements VetoSubscriber, then the normal unsubscribe methods will
    * still unsubscribe the object.
    *
    * @param eventClass class this object is subscribed to by proxy
    * @param subscribedByProxy object subscribed by proxy
    * @return true if the subscription was cancelled, false if it never existed
    */
   boolean unsubscribeVetoExactly(Class eventClass, Object subscribedByProxy);

   /**
    * Stop a veto subscription for an object that is subscribed to a topic with a ProxySubscriber.
    * <p/>
    * If an object is subscribed by proxy and it implements EventSubscriber, then the normal unsubscribe methods will
    * still unsubscribe the object.
    *
    * @param topic the topic this object is subscribed to by proxy
    * @param subscribedByProxy object subscribed by proxy
    * @return true if the subscription was cancelled, false if it never existed
    */
   boolean unsubscribeVeto(String topic, Object subscribedByProxy);

   /**
    * When using annotations, an object may be subscribed by proxy.  This unsubscribe method will unsubscribe an object
    * that is subscribed with a ProxySubscriber.
    * <p/>
    * If an object is subscribed by proxy and it implements EventSubscriber, then the normal unsubscribe methods will
    * still unsubscribe the object.
    *
    * @param pattern the RegEx expression this object is subscribed to by proxy
    * @param subscribedByProxy object subscribed by proxy
    * @return true if the subscription was cancelled, false if it never existed
    */
   boolean unsubscribeVeto(Pattern pattern, Object subscribedByProxy);
}
