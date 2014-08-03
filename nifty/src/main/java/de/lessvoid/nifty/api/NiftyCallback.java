package de.lessvoid.nifty.api;

/**
 * This is used as a parameter wherever Nifty needs to call user code. You provide an implementation of this interface
 * so that Nifty can call you back a the appropriate times.
 *
 * @param <T>
 */
public interface NiftyCallback<T> {
  void execute(T t);
}
