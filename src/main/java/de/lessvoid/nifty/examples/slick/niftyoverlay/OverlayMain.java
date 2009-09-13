package de.lessvoid.nifty.examples.slick.niftyoverlay;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * This demonstrates a standard slick GameState that has been "overlayed" with
 * a simple Nifty GUI. Note that no NiftyGameState is used! Instead this uses
 * the plain Nifty methods one would use in a none Slick environment.
 * @author void
 *
 */
public class OverlayMain extends StateBasedGame {
  public OverlayMain(final String title) {
    super(title);
  }

  @Override
  public void initStatesList(final GameContainer container) throws SlickException {
    addState(new TestState1());  
  }

  public static void main(final String[] args) {
    try {
      AppGameContainer container = new AppGameContainer(new OverlayMain("Nifty Overlay over Slick"));
      container.setDisplayMode(1024, 768, false);
      container.setTargetFrameRate(1000);
      container.setMinimumLogicUpdateInterval(1);
      container.setMinimumLogicUpdateInterval(2);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }
}
