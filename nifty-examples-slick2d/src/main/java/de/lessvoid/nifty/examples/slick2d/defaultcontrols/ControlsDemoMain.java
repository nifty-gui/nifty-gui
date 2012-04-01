package de.lessvoid.nifty.examples.slick2d.defaultcontrols;

import org.lwjgl.opengl.DisplayMode;

import de.lessvoid.nifty.examples.defaultcontrols.ControlsDemo;
import de.lessvoid.nifty.examples.slick2d.SlickExampleLoader;
import de.lessvoid.nifty.examples.slick2d.resolution.ResolutionControlSlick;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;

/**
 * Demo class to execute the default controls demonstration.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ControlsDemoMain {
  /**
   * Execute the demonstration.
   *
   * @param args call arguments - have no effect
   */
  public static void main(final String[] args) {
    final ResolutionControlSlick resControl = new ResolutionControlSlick();
    SlickExampleLoader.createGame(new SlickExampleLoader(new ControlsDemo<DisplayMode>(resControl)) {
      @Override
      protected void initGameAndGUI(final GameContainer container) {
        resControl.setContainer((AppGameContainer) container);
        super.initGameAndGUI(container);
      }
    });
  }
}
