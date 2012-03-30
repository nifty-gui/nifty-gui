package de.lessvoid.nifty.examples.slick;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {
  private MenuState menuState;

  public Main(final String title) {
    super(title);
  }

  @Override
  public void initStatesList(final GameContainer container) throws SlickException {
    try {
      menuState = new MenuState(container, this, "slick/mainmenu.xml");
    } catch (Exception e) {
      throw new SlickException("failed", e);
    }
    addState(menuState);
  }

  public static void main(final String[] args) {
    try {
      AppGameContainer container = new AppGameContainer(new Main("Nifty Slick Nifty"));
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
