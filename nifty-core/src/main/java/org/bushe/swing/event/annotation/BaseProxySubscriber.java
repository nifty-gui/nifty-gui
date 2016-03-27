package org.bushe.swing.event.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.VetoEventListener;

/** A class is subscribed to an EventService on behalf of another object. */
public class BaseProxySubscriber extends AbstractProxySubscriber
        implements org.bushe.swing.event.EventSubscriber, VetoEventListener {
   private Class subscriptionClass;

   /**
    * Creates a proxy.  This does not subscribe it.
    *
    * @param proxiedSubscriber the subscriber that the proxy will call when an event is published
    * @param subscriptionMethod the method the proxy will call, must have an Object as it's first and only parameter
    * @param referenceStrength if the subscription is weak, the reference from the proxy to the real subscriber should
    * be too
    * @param es the EventService we will be subscribed to, since we may need to unsubscribe when weak refs no longer
    * exist
    * @param subscription the class to subscribe to, used for unsubscription only
    * @param veto whether this is a veto subscriber
    */
   public BaseProxySubscriber(Object proxiedSubscriber, Method subscriptionMethod, ReferenceStrength referenceStrength,
           EventService es, Class subscription, boolean veto) {
      this(proxiedSubscriber, subscriptionMethod, referenceStrength, 0, es, subscription, veto);
   }

   /**
    * Creates a proxy with a priority.  This does not subscribe it.
    *
    * @param proxiedSubscriber the subscriber that the proxy will call when an event is published
    * @param subscriptionMethod the method the proxy will call, must have an Object as it's first and only parameter
    * @param referenceStrength if the subscription is weak, the reference from the proxy to the real subscriber should
    * be too
    * @param es the EventService we will be subscribed to, since we may need to unsubscribe when weak refs no longer
    * exist
    * @param subscription the class to subscribe to, used for unsubscription only
    * @param veto whether this is a veto subscriber
    */
   public BaseProxySubscriber(Object proxiedSubscriber, Method subscriptionMethod, ReferenceStrength referenceStrength,
           int priority, EventService es, Class subscription, boolean veto) {
      super(proxiedSubscriber, subscriptionMethod, referenceStrength, priority, es, veto);
      this.subscriptionClass = subscription;
      Class[] params = subscriptionMethod.getParameterTypes();
      if (params == null || params.length != 1 || params[0].isPrimitive()) {
         throw new IllegalArgumentException("The subscriptionMethod must have a single non-primitive parameter.");
      }
   }

   /**
    * Handles the event publication by pushing it to the real subscriber's subscription Method.
    *
    * @param event The Object that is being published.
    */
   public void onEvent(Object event) {
      Object[] args = new Object[]{event};
      Method subscriptionMethod = null;
      Object obj = null;
      try {
         obj = getProxiedSubscriber();
         if (obj == null) {
            //has been garbage collected
            return;
         }
         subscriptionMethod = getSubscriptionMethod();
         subscriptionMethod.invoke(obj, args);
      } catch (IllegalAccessException e) {
         String message = "Exception when invoking annotated method from EventService publication.  Event class:" + event.getClass() + ", Event:" + event + ", subscriber:" + getProxiedSubscriber() + ", subscription Method=" + getSubscriptionMethod();
         retryReflectiveCallUsingAccessibleObject(args, subscriptionMethod, obj, e, message);
      } catch (InvocationTargetException e) {
         throw new RuntimeException("InvocationTargetException when invoking annotated method from EventService publication.  Event class:" + event.getClass() + ", Event:" + event + ", subscriber:" + getProxiedSubscriber() + ", subscription Method=" + getSubscriptionMethod(), e);
      }
   }


   public boolean shouldVeto(Object event) {
      Object[] args = new Object[]{event};
      Method subscriptionMethod = null;
      Object obj = null;
      try {
         obj = getProxiedSubscriber();
         if (obj == null) {
            //has been garbage collected
            return false;
         }
         subscriptionMethod = getSubscriptionMethod();
         return Boolean.valueOf(subscriptionMethod.invoke(obj, args)+"");
      } catch (IllegalAccessException e) {
         String message = "Exception when invoking annotated method from EventService publication.  Event class:" + event.getClass() + ", Event:" + event + ", subscriber:" + getProxiedSubscriber() + ", subscription Method=" + getSubscriptionMethod();
         return retryReflectiveCallUsingAccessibleObject(args, subscriptionMethod, obj, e, message);
      } catch (InvocationTargetException e) {
         throw new RuntimeException("InvocationTargetException when invoking annotated method from EventService publication.  Event class:" + event.getClass() + ", Event:" + event + ", subscriber:" + getProxiedSubscriber() + ", subscription Method=" + getSubscriptionMethod(), e);
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof BaseProxySubscriber) {
         if (!super.equals(obj)) {
            return false;
         }
         BaseProxySubscriber bps = (BaseProxySubscriber) obj;
         if (subscriptionClass != bps.subscriptionClass) {
            if (subscriptionClass == null) {
               return false;
            } else {
               if (!subscriptionClass.equals(bps.subscriptionClass)) {
                  return false;
               }
            }
         }
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      return "BaseProxySubscriber{" +
              "subscription=" + subscriptionClass +
              "veto=" + veto +
              "realSubscriber=" + getProxiedSubscriber() +
              ", subscriptionMethod=" + getSubscriptionMethod() +
              ", referenceStrength=" + getReferenceStrength() +
              ", eventService=" + getEventService() +
              '}';
   }

}
