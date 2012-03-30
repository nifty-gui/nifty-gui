package de.lessvoid.nifty.examples.slick.console;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ConsoleMain extends StateBasedGame {
  private ConsoleState consoleState;

  public ConsoleMain(final String title) {
    super(title);
  }

  @Override
  public void initStatesList(final GameContainer container) throws SlickException {
    consoleState = new ConsoleState(container, this, "slick/console/console.xml");
    addState(consoleState);
  }

  public static void main(final String[] args) {
    try {
      AppGameContainer container = new AppGameContainer(new ConsoleMain("Nifty Console with Slick"));
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
