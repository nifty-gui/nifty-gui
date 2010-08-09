package de.lessvoid.nifty.tools.event;

/**
 * Event.
 * @param <H>
 */
public interface Event<H extends EventHandler> {

  /**
   * The Type of this Event.
   * @param <H>
   */
  public static class Type<H> {
    private static int nextHashCode;
    private final int index;

    public Type() {
      index = ++nextHashCode;
    }

    @Override
    public final int hashCode() {
      return index;
    }

    @Override
    public String toString() {
      return "Event type";
    }
  }

  /**
   * Get the Type of the Event.
   * @return
   */
  Type<H> getAssociatedType();

  /**
   * Dispatch to the handler.
   * @param handler
   */
  void dispatch(H handler);

}
