package de.lessvoid.nifty.input;

import de.lessvoid.nifty.NiftyEvent;

/**
 * A nifty input event. This was an enum before but has now been changed to an interface.
 * This allows users to implement this interface and create their own enums.
 *
 * This interface is meant to be implemented by an enum. This way you can simply call
 * equals() on any instance of NiftyInputEvent and compare it with your concrete enum. 
 *
 * @author void
 */
public interface NiftyInputEvent extends NiftyEvent {
}
