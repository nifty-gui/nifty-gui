package de.lessvoid.nifty.internal.common;

import java.util.List;

/**
 * A Helper function to iterate over a list and perform an action for each element.
 * @author void
 *
 * @param <C> The type that is contained in the list. This is the stuff we iterate over.
 * @param <T> The type of some parameter that will be forwarded to each method invocation.
 */
public class InternalChildIterate<C, T> {

  /**
   * Iterate over the given List<C> and call the given method for each entry contained in the list. The parameter will
   * be added to each invocation.
   *
   * @param children the list of type C to iterate over
   * @param action the action to execute for each element of C
   * @param parameter the parameter object to call each action with (additionally to the element)
   */
  public static <C, T> void iterate(final List<C> children, final Function<C, T> action, final T parameter) {
    for (int i=0; i<children.size(); i++) {
      action.perform(children.get(i), parameter);
    }
  }

  /**
   * Iterate over the given List<C> and call the given method for each entry contained in the list.
   *
   * @param children the list of type C to iterate over
   * @param action the action to execute for each element of C
   */
  public static <C, T> void iterate(final List<C> children, final Function<C, T> action) {
    for (int i=0; i<children.size(); i++) {
      action.perform(children.get(i), null);
    }
  }

  /**
   * The interface representing the action to perform for each child element.
   * @author void
   */
  public static interface Function<C, T> {
    void perform(C child, T parameter);
  }
}
