package org.bushe.swing.event.annotation;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.bushe.swing.event.EventService;

/**
 * A Proxy Subscriber for Annotations that use topic patterns
 */
public class ProxyTopicPatternSubscriber extends ProxyTopicSubscriber {
   private Pattern pattern;

   /**
    * Creates a proxy.  This does not subscribe it.
    *
    * @param proxiedSubscriber the subscriber that the proxy will call when an event is published
    * @param subscriptionMethod the method the proxy will call, must have an Object as it's first and only parameter
    * @param referenceStrength if the subscription is weak, the reference from the proxy to the real subscriber should
    * be too
    * @param es the EventService we will be subscribed to, since we may need to unsubscribe when weak refs no longer
    * exist
    * @param patternString the Regular Expression for topics to subscribe to, used for unsubscription only
    */
   public ProxyTopicPatternSubscriber(Object proxiedSubscriber, Method subscriptionMethod,
           ReferenceStrength referenceStrength, EventService es, String patternString,
           Pattern pattern, boolean veto) {
      this(proxiedSubscriber, subscriptionMethod, referenceStrength, 0, es, patternString, pattern, veto);
   }

   /**
    * Creates a proxy.  This does not subscribe it.
    *
    * @param proxiedSubscriber the subscriber that the proxy will call when an event is published
    * @param subscriptionMethod the method the proxy will call, must have an Object as it's first and only parameter
    * @param referenceStrength if the subscription is weak, the reference from the proxy to the real subscriber should
    * be too
    * @param es the EventService we will be subscribed to, since we may need to unsubscribe when weak refs no longer
    * exist
    * @param patternString the Regular Expression for topics to subscribe to, used for unsubscription only
    */
   public ProxyTopicPatternSubscriber(Object proxiedSubscriber, Method subscriptionMethod,
           ReferenceStrength referenceStrength, int priority,
           EventService es, String patternString, Pattern pattern, boolean veto) {
      super(proxiedSubscriber, subscriptionMethod, referenceStrength, priority, es, patternString, veto);
      this.pattern = pattern;
   }

   protected void unsubscribe(String topic) {
      if (veto) {
         getEventService().unsubscribeVetoListener(pattern, this);
      } else {
         getEventService().unsubscribe(pattern, this);
      }
      pattern = null;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      if (!super.equals(o)) {
         return false;
      }

      ProxyTopicPatternSubscriber that = (ProxyTopicPatternSubscriber) o;

      if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) {
         return false;
      }

      return true;
   }

   public String toString() {
      return "ProxyTopicPatternSubscriber{" +
              "pattern=" + pattern +
              "veto=" + veto +
              "realSubscriber=" + getProxiedSubscriber() +
              ", subscriptionMethod=" + getSubscriptionMethod() +
              ", referenceStrength=" + getReferenceStrength() +
              ", eventService=" + getEventService() +
              '}';
   }
}
