package de.lessvoid.nifty.api;

/**
 * The NiftyRuntimeException will be thrown when Nifty is desperate :)
 * @author void
 */
public class NiftyRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NiftyRuntimeException(final Exception e) {
    super(e);
  }
}
