package de.lessvoid.nifty;

import de.lessvoid.console.Console;
import de.lessvoid.nifty.effects.EffectManager;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
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
  public void render(final Screen screen, final NiftyRenderEngine renderDevide) {
    outputLayers(screen, renderDevide);
  }

  /**
   * Output layers.
   * @param screen the Screen information should be output
   * @param theRenderDevice the RenderDevice
   */
  private void outputLayers(final Screen screen, final NiftyRenderEngine theRenderDevice) {
    console.clear();
    console.output("*[" + screen.getScreenId() + "]");

    // render all layers
    for (Element layer : screen.getLayerElements()) {
      layer.render(theRenderDevice);
      console.output(" +" + getIdText(layer) + " => " + layer.getElementStateString());
      console.output("  " + whitespace(getIdText(layer).length()) + "    " + outputEffects(layer));
      outputElement(layer, "    ");
    }

    screen.debug(console);

    theRenderDevice.saveState(RenderStateType.allStates());
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
      console.output(offset + getIdText(ww) + " -> " + ww.getElementStateString());
      console.output(offset + whitespace(getIdText(ww).length()) + "    " + outputEffects(ww));
      outputElement(ww, offset + "  ");
    }
  }

  /**
   * output length whitespaces.
   * @param length number of whitespaces
   * @return string with whitespaces length times.
   */
  private String whitespace(final int length) {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < length; i++) {
      b.append(" ");
    }
    return b.toString();
  }

  /**
   * output effect helper.
   * @param w the Element
   * @return the String
   */
  private String outputEffects(final Element w) {
    EffectManager m = w.getEffectManager();
    return "effect [" + m.getStateString() + "]";
  }

  /**
   * Output Id Text.
   * @param ww the Element
   * @return the String
   */
  private String getIdText(final Element ww) {
    String id = ww.getId();
    if (id == null) {
      return "[unknown]";
    } else {
      return "[" + id + "]";
    }
  }

}
