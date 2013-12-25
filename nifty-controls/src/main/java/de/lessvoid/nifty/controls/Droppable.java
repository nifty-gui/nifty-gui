package de.lessvoid.nifty.controls;

import javax.annotation.Nonnull;

/**
 * The Droppable NiftyControl interface.
 *
 * @author void
 */
public interface Droppable extends NiftyControl {

  /**
   * Add the given filter to this Droppable. A DroppableDropFilter will be asked if
   * a certain Draggable is able to be dropped on this Droppable.
   *
   * @param droppableDropFilter the filter
   */
  void addFilter(@Nonnull DroppableDropFilter droppableDropFilter);

  /**
   * Remove the given filter.
   *
   * @param filter the filter to be removed
   */
  void removeFilter(@Nonnull DroppableDropFilter filter);

  /**
   * Remove all Filters.
   */
  void removeAllFilters();
}
