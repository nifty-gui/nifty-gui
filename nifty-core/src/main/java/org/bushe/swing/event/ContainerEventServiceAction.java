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
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

/**
 * When fired, this action publishes an ActionEvent on a Container EventService.
 * See {@link EventServiceAction} for more information.
 * <p/>
 * By default, the Container EventService is found by asking the ContainerEventServiceFinder to find the EventService
 * for the source of the fired ActionEvent, which must be a java.awt.Component and contained in a hierarchy (the source
 * must have been added to another Swing container).  If the action was on a button, this means the container hierarchy
 * of the button is walked (up) until a ContainerEventServiceSupplier is found or until the top of the hierarchy is
 * reached, at which point a ContainerEventService is created automatically on the fly via the top container's
 * putClientProperty() method using the key {@link ContainerEventServiceFinder#CLIENT_PROPERTY_KEY_TOP_LEVEL_EVENT_SERVICE}.
 * If the event is from a JPopupMenu then the popup menu's invoker's hierarchy is walked.
 * <p/>
 * To exhibit other behavior, override the getSwingEventService() to return another EventService. For example, the
 * creator of a popup menu may pass itself to the ContainerEventServiceFinder to return a parent's EventService.
 * <p/>
 *
 * @author Michael Bushe michael@bushe.com
 * @see EventServiceAction for further documentation
 * @see ContainerEventServiceFinder on how the service is found
 */
public class ContainerEventServiceAction extends EventServiceAction {
   public ContainerEventServiceAction() {
   }

   public ContainerEventServiceAction(String actionName, ImageIcon icon) {
      super(actionName, icon);
   }

   protected EventService getEventService(ActionEvent event) {
      Component comp = null;
      try {
         if (event.getSource() instanceof Component) {
            comp = (Component) event.getSource();
         }
         if (comp == null) {
            if (getThrowsExceptionOnNullEventService()) {
               throw new RuntimeException("ActionEvent source was null, could not find event bus, must override getContainerEventService in action with id:" + getName());
            }
         } else {
            return ContainerEventServiceFinder.getEventService(comp);
         }
      } catch (ClassCastException ex) {
         if (getThrowsExceptionOnNullEventService()) {
            throw new RuntimeException("ActionEvent source was not a component (" + (comp == null ? "null" : comp.getClass() + "") + "), must override getContainerEventService in action with id:" + getName(), ex);
         }
      }
      return null;
   }
}

