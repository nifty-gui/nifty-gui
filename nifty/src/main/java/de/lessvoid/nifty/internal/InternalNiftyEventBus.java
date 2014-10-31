package de.lessvoid.nifty.internal;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import de.lessvoid.nifty.api.event.NiftyEvent;

public class InternalNiftyEventBus {
  private MBassador<NiftyEvent> eventBus;

  public InternalNiftyEventBus() {
    eventBus = new MBassador<NiftyEvent>(BusConfiguration.SyncAsync());
  }

  public void subscribe(final Object listener) {
    eventBus.subscribe(listener);
  }

  public void publish(final NiftyEvent message) {
    eventBus.publish(message);
  }
}
