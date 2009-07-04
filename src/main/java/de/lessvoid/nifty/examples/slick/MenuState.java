package de.lessvoid.nifty.examples.slick;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.slick.NiftyGameState;

public class MenuState extends NiftyGameState implements ScreenController {

  public static int id = 1;
  private StateBasedGame game;
  private GameContainer container;
  private Screen screen;

  public MenuState(
      final GameContainer newContainer,
      final StateBasedGame newGame,
      final String filename)
      throws SlickException {
    super(id);
    game = newGame;
    container = newContainer;
    fromXml(filename, this);
    enableMouseImage(new Image("slick/crosshair.png"));
  }

  public void bind(final Nifty newNifty, final Screen newScreen) {
    screen = newScreen;
  }

  public void onStartScreen() {
    if (screen.getScreenId().equals("start")) {
      nifty.gotoScreen("mainMenu");
    } else if (screen.getScreenId().equals("newGame")) {
      screen.findElementByName("newGame").setFocus();
    }
  }

  public void onEndScreen() {
  }

  public void newGame() {
    System.out.println("mouseX: " + mouseX + ", mouseY: " + mouseY);
  }

  public void exit() {
    screen.endScreen(new EndNotify() {
      public void perform() {
        container.exit();
      }
    });
  }

  public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
    super.mouseMoved(oldx, oldy, newx, newy);
    System.out.println(oldx + ", " + oldy + ", " + newx + ", " + newy);
  }
}
