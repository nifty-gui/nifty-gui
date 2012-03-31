package de.lessvoid.nifty.examples;

import de.lessvoid.nifty.Nifty;

/**
 * This class defines a example for Nifty. If defines how the example is supposed to be load in a unified way.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface NiftyExample {
  /**
   * The start screen that is supposed to be load initial.
   *
   * @return the name of the start screen
   */
  String getStartScreen();

  /**
   * The resource path of the XML file that is supposed to be load for the example.
   *
   * @return the resource path to the main XML or {@code null} in case no XML file is supposed to be load
   */
  String getMainXML();

  /**
   * The title of this demonstration example.
   *
   * @return the title of this example
   */
  String getTitle();

  /**
   * This function is called right before the example itself is executed. In case building or changing the GUI before
   * its displayed is required: This is the place.
   *
   * @param nifty the used instance of the Nifty-GUI
   */
  void prepareStart(Nifty nifty);
}
