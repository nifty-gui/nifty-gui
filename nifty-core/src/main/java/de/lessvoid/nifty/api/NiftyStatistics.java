package de.lessvoid.nifty.api;

import java.util.List;

import de.lessvoid.nifty.internal.common.InternalNiftyStatistics;

/**
 * Several different statistical informations about Niftys internal processing.
 * @author void
 */
public class NiftyStatistics {
  private final InternalNiftyStatistics statistics;

  /**
   * Get the total number of render tree synchronisations.
   * @return the total number of render tree synchronisations
   */
  public int getRenderTreeSynchronisations() {
    return statistics.getRenderTreeSynchronisations();
  }

  /**
   * Get the collected frame time samples into the target list. The oldest sample is returned first.
   * @param target the list to collect the samples into
   */
  public void getFrameTime(final List<Integer> target) {
    statistics.getFrameTime(target);
  }

  /**
   * Get the collected update time samples into the target list. The oldest sample is returned first.
   * @param target the list to collect the samples into
   */
  public void getUpdateTime(final List<Integer> target) {
    statistics.getUpdateTime(target);
  }

  /**
   * Get all statistics as a String ready for output in a console.
   * @return a String with all appropriate statistics for loggin
   */
  public String getAll() {
    StringBuilder result = new StringBuilder("Nifty statistics\n");
    result.append(" renderTreeSynchronisations: ").append(statistics.getRenderTreeSynchronisations()).append("\n");
    return result.toString();
  }

  NiftyStatistics(final InternalNiftyStatistics statistics) {
    this.statistics = statistics;
  }

  InternalNiftyStatistics getImpl() {
    return statistics;
  }
}
