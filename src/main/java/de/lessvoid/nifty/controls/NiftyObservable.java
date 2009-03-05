package de.lessvoid.nifty.controls;

import java.util.Observable;

/**
 * We don't feel like extending everywhere from Observable (extends is evil). So we
 * kind of add this helper class that allows us to change the changed flag from the
 * outside and we simply use the Observable functionality to help with managing the
 * Observers.
 * @author void256
 */
public class NiftyObservable extends Observable {
  public synchronized void clearChanged() {
    super.clearChanged();
  }

  public synchronized void setChanged() {
    super.setChanged();
  }
}
