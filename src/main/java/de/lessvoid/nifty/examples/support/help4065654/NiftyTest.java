package de.lessvoid.nifty.examples.support.help4065654;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.lwjglslick.input.LwjglInputSystem;
import de.lessvoid.nifty.lwjglslick.render.RenderDeviceLwjgl;
import de.lessvoid.nifty.lwjglslick.sound.SlickSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

public class NiftyTest extends BasicGame {
  private Nifty nifty = null;
  private float time = 1500;

  public NiftyTest() {
    super("Nifty test");
  }

  @Override
  public void init(GameContainer gc) throws SlickException {
    nifty = new Nifty(new RenderDeviceLwjgl(), new SlickSoundDevice(), new LwjglInputSystem(), new TimeProvider());
    nifty.fromXml("de/lessvoid/nifty/examples/support/help4065654/nifty.xml", "screen"); // Show panels.
    nifty.getCurrentScreen().findElementByName("left").show();
    nifty.getCurrentScreen().findElementByName("right").show();
    nifty.getCurrentScreen().findElementByName("top").show();
    nifty.getCurrentScreen().findElementByName("bottom").show();
  }

  @Override
  public void update(GameContainer gc, int delta) throws SlickException {
    if (time < 0) {
      time += 1 * delta;
      if (time >= 0) {
        time = 1500;
        // Show panels.
        nifty.getCurrentScreen().findElementByName("left").show();
        nifty.getCurrentScreen().findElementByName("right").show();
        nifty.getCurrentScreen().findElementByName("top").show();
        nifty.getCurrentScreen().findElementByName("bottom").show();
      }
    } else {
      time -= 1 * delta;
      if (time <= 0) {
        time = -1500;
        // Hide panels.
        nifty.getCurrentScreen().findElementByName("left").hide();
        nifty.getCurrentScreen().findElementByName("right").hide();
        nifty.getCurrentScreen().findElementByName("top").hide();
        nifty.getCurrentScreen().findElementByName("bottom").hide();
      }
    }
  }

  @Override
  public void render(GameContainer gc, Graphics g) throws SlickException {
    SlickCallable.enterSafeBlock();
    nifty.render(true);
    SlickCallable.leaveSafeBlock();
  }

  /** * @param args */
  public static void main(String[] args) {
    AppGameContainer app;
    try {
      app = new AppGameContainer(new NiftyTest());
      app.setDisplayMode(800, 600, false);
      app.start();
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }
}
