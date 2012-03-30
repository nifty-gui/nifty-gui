package de.lessvoid.nifty.examples.slick.console;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.slick.NiftyGameState;

public class ConsoleState extends NiftyGameState implements ScreenController {

  public static int id = 1;

  public ConsoleState(
      final GameContainer newContainer,
      final StateBasedGame newGame,
      final String filename)
      throws SlickException {
    super(id);
    fromXml(filename, this);
  }

  public void bind(final Nifty newNifty, final Screen newScreen) {
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }
}
