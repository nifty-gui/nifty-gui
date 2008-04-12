package de.lessvoid.nifty;

import de.lessvoid.console.Console;
import de.lessvoid.nifty.effects.EffectManager;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.render.RenderState;
import de.lessvoid.nifty.screen.Screen;

/**
 * NiftyDebugConsole can be used to output information for a screen on a console.
 * @author void
 */
public class NiftyDebugConsole {

  /**
   * max number of lines in the debug console.
   */
  private static final int CONSOLE_MAX_LINES = 50;

  /**
   * The actual console is implemented there.
   */
  private Console console;

  /**
   * Create new console.
   */
  public NiftyDebugConsole() {
    this.console = new Console(CONSOLE_MAX_LINES, true);
  }

  /**
   * Render all Information for the given screen in the console.
   * @param screen the Screen to output
   * @param renderDevide the RenderDevice to use
   */
  public void render(final Screen screen, final RenderDevice renderDevide) {
    outputLayers(screen, renderDevide);
  }

  /**
   * Output layers.
   * @param screen the Screen information should be output
   * @param theRenderDevice the RenderDevice
   */
  private void outputLayers(final Screen screen, final RenderDevice theRenderDevice) {
    console.clear();
    console.output("Screen: " + screen.getScreenId());

    // render all layers
    for (Element layer : screen.getLayerElements()) {
      layer.render(theRenderDevice);
      console.output(" Layer: " + getIdText(layer) + ", " + layer.getElementStateString() + outputEffects(layer));

      for (Element w : layer.getElements()) {
        console.output("  " + getIdText(w) + ", " + w.getElementStateString());
        outputElement(w, "    ");
      }
    }

    theRenderDevice.saveState(RenderState.allStates());
    console.update();
    theRenderDevice.restoreState();
  }

  /**
   * helper to output information.
   * @param w the element
   * @param offset some string offset for shizzle
   */
  private void outputElement(final Element w, final String offset) {
    for (Element ww : w.getElements()) {
      console.output(offset + getIdText(ww) + ", " + ww.getElementStateString() + outputEffects(w));
      outputElement(ww, offset + "  ");
    }
  }

  /**
   * output effect helper.
   * @param w the Element
   * @return the String
   */
  private String outputEffects(final Element w) {
    EffectManager m = w.getEffectManager();
    return m.getStateString();
  }

  /**
   * Output Id Text.
   * @param ww the Element
   * @return the String
   */
  private String getIdText(final Element ww) {
    String id = ww.getId();
    if (id == null) {
      return "unknown";
    } else {
      return id;
    }
  }

}
