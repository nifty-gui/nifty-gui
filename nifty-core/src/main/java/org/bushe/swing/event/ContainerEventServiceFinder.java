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

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.RootPaneContainer;

/**
 * This class finds a component's container event service, and creates one if necessary and possible.
 * <p/>
 * A Container EventService is, unlike the EventBus, an EventService that is container specific, in other words, it is
 * shared only amongst components within a container.  For example, a Form component can supply an EventService used
 * only by components in the form.  The Form's components can publish value change events on their Container's Event
 * Service.  The Form's Model and Validator may listen to these events to collect data and show errors, respectively.
 * <p/>
 * Most importantly, Container EventService's cuts down event traffic, avoid naming and listener clashes, promotes
 * componentization, and splits events usage into logical subsets.
 * <p/>
 * The finder will walk up a component's hierarchy searching for a parent that implements ContainerEventServiceSupplier.
 * If it find one, it returns it.  If it doesn't find one, the top level JComponent (specifically, the highest parent in
 * the hierarchy, typically a JRootPane) has a client property added to it (if not already set) that has the value of a
 * new SwingEventService, which is then returned.  The EventBus is never returned.
 *
 * @author Michael Bushe michael@bushe.com
 */
public class ContainerEventServiceFinder {
   /** The client property used to put a new SwingEventService on top-level components. */
   public static final String CLIENT_PROPERTY_KEY_TOP_LEVEL_EVENT_SERVICE = "ContainerEventServiceFinder.createdService";

   /**
    * Walks the component's parents until it find an ContainerEventServiceSupplier and returns the supplier's
    * EventService.  If the component in the tree is a JPopupMenu, then the menu's invoker is walked.
    *
    * @param component any component
    *
    * @return the ContainerEventService of the nearest parent
    */
   public static EventService getEventService(Component component) {
      while (component != null) {
         if (component instanceof ContainerEventServiceSupplier) {
            return ((ContainerEventServiceSupplier) component).getContainerEventService();
         }
         if (component instanceof JPopupMenu) {
            component = ((JPopupMenu) component).getInvoker();
         } else {
            if (component.getParent() == null) {
               //There is no supplier.  Instead of returning null, make an event service
               //and stick it in the client properties of the top level container.
               if (component instanceof RootPaneContainer) {
                  component = ((RootPaneContainer) component).getRootPane();
               }
               if (!(component instanceof JComponent)) {
                  return null;
               }
               JComponent jComp = ((JComponent) component);
               SwingEventService eventService = (SwingEventService) jComp.getClientProperty(CLIENT_PROPERTY_KEY_TOP_LEVEL_EVENT_SERVICE);
               if (eventService == null) {
                  eventService = new SwingEventService();
                  jComp.putClientProperty(CLIENT_PROPERTY_KEY_TOP_LEVEL_EVENT_SERVICE, eventService);
               }
               return eventService;
            } else {
               component = component.getParent();
            }
         }
      }
      return null;
   }
}
