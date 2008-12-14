package de.lessvoid.nifty;

import de.lessvoid.console.Console;
import de.lessvoid.nifty.effects.EffectManager;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.render.spi.RenderDevice;
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
   * output elements.
   */
  private boolean outputElements = true;

  /**
   * Create new console.
 * @param RenderDevice as specified by the implementation
   */
  public NiftyDebugConsole(RenderDevice device) {
    this.console = new Console(device, CONSOLE_MAX_LINES, true);
  }

  /**
   * Render all Information for the given screen in the console.
   * @param nifty TODOnifty
   * @param screen the Screen to output
   * @param renderDevice the RenderDevice to use
   */
  public void render(final Nifty nifty, final Screen screen, final NiftyRenderEngine renderDevice) {
    if (outputElements) {
      outputElements(nifty, screen, renderDevice);
    } else {
      outputEffects(nifty, screen, renderDevice);
    }
  }

  /**
   * Output layers.
   * @param nifty nifty
   * @param screen the Screen information should be output
   * @param theRenderDevice the RenderDevice
   */
  private void outputElements(final Nifty nifty, final Screen screen, final NiftyRenderEngine theRenderDevice) {
    console.clear();
    console.output("*[" + screen.getScreenId() + "]");

    // render all layers
    for (Element layer : screen.getLayerElements()) {
      layer.render(theRenderDevice);
      String layerType = " +";
      if (!layer.isVisible()) {
        layerType = " -";
      }
      String offsetString = layerType + getIdText(layer) + " => ";
      console.output(offsetString + layer.getElementStateString(offsetString));
      outputElement(layer, "    ");
    }

    screen.debug(console);
    console.output(nifty.getMouseInputEventQueue().toString());

    theRenderDevice.saveState(RenderStateType.allStates());
    console.update();
    theRenderDevice.restoreState();
  }

  /**
   * Output layers.
   * @param nifty nifty
   * @param screen the Screen information should be output
   * @param theRenderDevice the RenderDevice
   */
  private void outputEffects(final Nifty nifty, final Screen screen, final NiftyRenderEngine theRenderDevice) {
    console.clear();
    if (screen == null) {
      return;
    }

    String screenId = screen.getScreenId();
    console.output("*[" + screenId + "]");

    // render all layers
    for (Element layer : screen.getLayerElements()) {
      layer.render(theRenderDevice);
      console.output(" +" + getIdText(layer) + " => " + outputEffects(layer, "      "));
      outputEffect(layer, "    ");
    }

    screen.debug(console);
    console.output(nifty.getMouseInputEventQueue().toString());

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
      String offsetString = offset + getIdText(ww) + " -> ";
      console.output(offsetString + ww.getElementStateString(offsetString));
      outputElement(ww, offset + "  ");
    }
  }

  private void outputEffect(final Element w, final String offset) {
    for (Element ww : w.getElements()) {
      console.output(offset + getIdText(ww) + outputEffects(ww, offset));
      outputEffect(ww, offset + "  ");
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
   * @param offset offset
   * @return the String
   */
  private String outputEffects(final Element w, final String offset) {
    EffectManager m = w.getEffectManager();
    return m.getStateString(offset);
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

  /**
   * @param outputElements the outputElements to set
   */
  public void setOutputElements(final boolean outputElements) {
    this.outputElements = outputElements;
  }

}
