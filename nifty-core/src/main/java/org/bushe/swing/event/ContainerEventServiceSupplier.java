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
 * A interface implemented by a Swing Container to supply an EventService local to it's child components.
 * <p/>
 * A Container EventService is an {@link EventService} which, unlike the {@link EventBus}, is specific to a container,
 * in other words, it is shared only among components within a Swing Container.  The only difference between a Container
 * EventService and any other EventService is that it's found and used by the children of a container.  The API and
 * available implementations all work the same as any other EventService.
 * <p/>
 * A good candidate for a ContainerEventServiceSupplier is a Form class.  The components that the Form contains can
 * publish objects when they they change state - for example when their values change or when they become invalid or
 * valid.  The Form may have a model that collects the user's entries by subscribing to events published on the Form's
 * EventService. A FormValidator may also listen to publications on the Form's EventService to subscribe to validation
 * errors.  The Form's components don't have to know about the form, or the model or the validator.  They just publish
 * events on their Container's EventService, which they can find by using a {@link ContainerEventServiceFinder}.
 * <p/>
 * This class does not ever have to be implemented or used directly.  The ContainerEventServiceFinder will create a
 * ContainerEventService on JRootPanes by default on demand.  Hence, each dialog and Frame will have their own
 * automatically as needed.  You only want to implement this interface when you want to limit events to subscribers
 * in containers smaller than a JRootPane, such as a Form's JPanel.
 *
 * @author Michael Bushe michael@bushe.com
 */
public interface ContainerEventServiceSupplier {
   public EventService getContainerEventService();
}
