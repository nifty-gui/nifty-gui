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

import java.util.HashMap;
import java.util.Map;

/**
 * A central registry of EventServices.  Used by the {@link EventBus}.
 * <p/>
 * By default will lazily hold a SwingEventService, which is mapped to {@link #SERVICE_NAME_SWING_EVENT_SERVICE} and
 * returned by {@link #getSwingEventService()}.  Also by default this same instance is returned by {@link #getEventBusService()},
 * is mapped to {@link #SERVICE_NAME_EVENT_BUS} and wrapped by the EventBus.
 * <p/>
 * Since the default EventService implementation is thread safe, and since it's not good to have lots of events on the
 * EventDispatchThread you may want multiple EventServices running on multiple threads, perhaps pulling events from a
 * server and coalescing them into one or more events that are pushed onto the EDT.
 * <p/>
 * To change the default implementation class for the EventBus' EventService, use the API:
 * <pre>
 * EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_EVENT_BUS, new SomeEventServiceImpl());
 * </pre>
 * Or use system properties by:
 * <pre>
 * System.setProperty(EventServiceLocator.SERVICE_NAME_EVENT_BUS, YourEventServiceImpl.class.getName());
 * </pre>
 * Likewise, you can set this on the command line via -Dorg.bushe.swing.event.swingEventServiceClass=foo.YourEventServiceImpl
 * <p/>
 * To change the default implementation class for the SwingEventService, use the API:
 * <pre>
 * EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_SWING_EVENT_SERVICE, new SomeSwingEventServiceImpl());
 * </pre>
 * Or use system properties by:
 * <pre>
 * System.setProperty(EventServiceLocator.SWING_EVENT_SERVICE_CLASS, YourEventServiceImpl.class.getName());
 * </pre>
 * Likewise, you can set this on the command line via -Dorg.bushe.swing.event.swingEventServiceClass=foo.YourEventServiceImpl
 *
 * @author Michael Bushe michael@bushe.com
 */
public class EventServiceLocator {
   /** The name "EventBus" is reserved for the service that the EventBus wraps and is returned by {@link #getEventBusService}.*/
   public static final String SERVICE_NAME_EVENT_BUS = "EventBus";
   /** The name "SwingEventService" is reserved for the service that is returned by {@link #getSwingEventService}. */
   public static final String SERVICE_NAME_SWING_EVENT_SERVICE = "SwingEventService";
   
   /**
    * Set this Java property to a Class that implements EventService to use an instance of that class instead of
    * the instance returned by {@link #getSwingEventService}.  Must be set before {@link #getEventBusService()} is called.
    */
   public static final String EVENT_BUS_CLASS = "org.bushe.swing.event.eventBusClass";
   /**
    * Set this Java property to a Class that implements EventService to use an instance of that class instead of
    * {@link SwingEventService} as service returned by {@link #getSwingEventService}.  Must be set on startup or
    * before the method {@link #getSwingEventService}is called.
    */
   public static final String SWING_EVENT_SERVICE_CLASS = "org.bushe.swing.event.swingEventServiceClass";

   private static EventService EVENT_BUS_SERVICE;
   private static EventService SWING_EVENT_SERVICE;

   private static final Map EVENT_SERVICES = new HashMap();

   /** @return the singleton instance of the EventService used by the EventBus */
   public static synchronized EventService getEventBusService() {
      if (EVENT_BUS_SERVICE == null) {
         EVENT_BUS_SERVICE = getEventService(EVENT_BUS_CLASS, getSwingEventService());
         EVENT_SERVICES.put(SERVICE_NAME_EVENT_BUS, EVENT_BUS_SERVICE);
      }
      return EVENT_BUS_SERVICE;
   }

   /** @return the singleton instance of a SwingEventService */
   public static synchronized EventService getSwingEventService() {
      if (SWING_EVENT_SERVICE == null) {
         SWING_EVENT_SERVICE = getEventService(SWING_EVENT_SERVICE_CLASS, new SwingEventService());
         EVENT_SERVICES.put(SERVICE_NAME_SWING_EVENT_SERVICE, SWING_EVENT_SERVICE);
      }
      return SWING_EVENT_SERVICE;
   }

   /**
    * @param serviceName the service name of the EventService, as registered by #setEventService(String, EventService),
    * or {@link #SERVICE_NAME_EVENT_BUS} or {@link #SERVICE_NAME_SWING_EVENT_SERVICE} .
    *
    * @return a named event service instance
    */
   public static synchronized EventService getEventService(String serviceName) {
      EventService es = (EventService) EVENT_SERVICES.get(serviceName);
      if (es == null) {
         if (SERVICE_NAME_EVENT_BUS.equals(serviceName)) {
            es = getEventBusService();
         } else if (SERVICE_NAME_SWING_EVENT_SERVICE.equals(serviceName)) {
            es = getSwingEventService();
         }
      }
      return es;
   }

   /**
    * Registers a named EventService to the locator.  Can be used to change the default EventBus implementation.
    * 
    * @param serviceName a named event service instance
    * @param es the EventService to attach to the service name
    *
    * @throws EventServiceExistsException if a service by this name already exists and the new service is non-null
    */
   public static synchronized void setEventService(String serviceName, EventService es) throws EventServiceExistsException {
      if (EVENT_SERVICES.get(serviceName) != null && es != null) {
         throw new EventServiceExistsException("An event service by the name " + serviceName + "already exists.  Perhaps multiple threads tried to create a service about the same time?");
      } else {
         EVENT_SERVICES.put(serviceName, es);
         if (SERVICE_NAME_EVENT_BUS.equals(serviceName)) {
            EVENT_BUS_SERVICE = es;
         } else if (SERVICE_NAME_SWING_EVENT_SERVICE.equals(serviceName)) {
            SWING_EVENT_SERVICE = es;
         }
      }
   }

   /**
    * Use this carefully.  Clears all the event services, including the SwingEventService (used by EventBus).
    * <p>
    * Callers may want to resubscribe existing subscribers.
    */
   static synchronized void clearAll() {
         EVENT_SERVICES.clear();
         EVENT_BUS_SERVICE = null;
         SWING_EVENT_SERVICE = null;
   }

   private static synchronized EventService getEventService(String eventServiceClassPropertyName, EventService defaultService) {
      EventService result;
      String eventServiceClassName = System.getProperty(eventServiceClassPropertyName);
      if (eventServiceClassName != null) {
         Class sesClass;
         try {
            sesClass = Class.forName(eventServiceClassName);
         } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find class specified in the property " + eventServiceClassPropertyName + ".  Class=" + eventServiceClassName, e);
         }
         Object service;
         try {
            service = sesClass.newInstance();
         } catch (InstantiationException e) {
            throw new RuntimeException("InstantiationException creating instance of class set from Java property" + eventServiceClassPropertyName + ".  Class=" + eventServiceClassName, e);
         } catch (IllegalAccessException e) {
            throw new RuntimeException("IllegalAccessException creating instance of class set from Java property" + eventServiceClassPropertyName + ".  Class=" + eventServiceClassName, e);
         }
         try {
            result = (EventService) service;
         } catch (ClassCastException ex) {
            throw new RuntimeException("ClassCastException casting to " + EventService.class + " from instance of class set from Java property" + eventServiceClassPropertyName + ".  Class=" + eventServiceClassName, ex);
         }
      } else {
         result = defaultService;
      }
      return result;
   }

}
