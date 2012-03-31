package de.lessvoid.nifty.examples.slick2d;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.slick2d.NiftyBasicGame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * This is the general example loader implementation that is able to load the different examples in a unified way. This
 * class will be used for the common examples.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class SlickExampleLoader extends NiftyBasicGame {
  /**
   * The nifty example that is executed by this class.
   */
  private final NiftyExample example;

  /**
   * This helper method creates the required Slick-instance to display the examples.
   *
   * @param loader the example loader that is supposed to be displayed in the game container
   */
  public static void createGame(final SlickExampleLoader loader) {
    try {
      AppGameContainer container = new AppGameContainer(loader, 1024, 768, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

  /**
   * Construct a new example loader that is going to execute the specified example.
   *
   * @param niftyExample the example to execute
   */
  public SlickExampleLoader(final NiftyExample niftyExample) {
    super(niftyExample.getTitle(), niftyExample.getStartScreen());
    example = niftyExample;
  }

  @Override
  protected void prepareNifty(final Nifty nifty) {
    example.prepareStart(nifty);
    if (example.getMainXML() != null) {
      nifty.fromXml(example.getMainXML(), example.getStartScreen());
    }
  }
}
