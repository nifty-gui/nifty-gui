/**
 * Copyright 2007 Bushe Enterprises, Inc., Hopkinton, MA, USA, www.bushe.com
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

import org.bushe.swing.event.annotation.ReferenceStrength;

/**
 * An interface that can be implemented when proxies are used for subscription, not needed in normal usage.  When an
 * unsubscribe method is called on an EventService, the EventService is required to check if any of subscribed objects
 * are ProxySubscribers and if the object to be unsubscribed is the ProxySubscriber's proxiedSubscriber. If so, the
 * EventService proxy is unsubscribed and the ProxySubscriber's proxyUnsubscribed() method is called to allow the proxy
 * to perform any cleanup if necessary.  ProxySubscribers should set their references to their proxied objects to null
 * for strong subscriptions to allow garbage collection.
 *
 * @author Michael Bushe
 */
public interface ProxySubscriber {

   /** @return the object this proxy is subscribed on behalf of */
   public Object getProxiedSubscriber();

   /**
    * Called by EventServices to inform the proxy that it is unsubscribed.  The ProxySubscriber should null the
    * reference to it's proxied subscriber
    */
   public void proxyUnsubscribed();

   /**
    * @return the reference strength from this proxy to the proxied subscriber
    */
   public ReferenceStrength getReferenceStrength();  
}
