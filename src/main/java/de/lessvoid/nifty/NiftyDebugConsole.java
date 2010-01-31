package de.lessvoid.nifty;

import java.io.UnsupportedEncodingException;

import de.lessvoid.console.Console;
import de.lessvoid.nifty.effects.EffectManager;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.render.RenderStateType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.tools.StringHelper;

/**
 * NiftyDebugConsole can be used to output information for a screen on a console.
 * @author void
 */
public class NiftyDebugConsole {

  /**
   * max number of lines in the debug console.
   */
  private static final int CONSOLE_MAX_LINES = 60;

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
  public NiftyDebugConsole(final RenderDevice device) {
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
    console.output(colorString(0xff, 0xff, 0xff));
    console.output("*[" + screen.getScreenId() + "]");

    for (Element layer : screen.getLayerElements()) {
      String layerType = " +";
      if (!layer.isVisible()) {
        layerType = " -";
      }
      String offsetString = layerType + getIdText(layer);
      console.output(offsetString + "\n" + StringHelper.whitespace(layerType.length()) + layer.getElementStateString(StringHelper.whitespace(layerType.length())));
      outputElement(layer, "   ");
    }

    console.output(colorString(0xff, 0x00, 0x00));
    screen.debug(console);
    console.output(nifty.getMouseInputEventQueue().toString());

    theRenderDevice.saveState(RenderStateType.allStates());
    console.update();
    theRenderDevice.restoreState();
  }

  private String colorString(final int r, final int g, final int b) {
    String color = "";
    try {
      color = new String(new byte[] {0x01, (byte) r, (byte) g, (byte) b}, "ISO-8859-1");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return color;
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
    console.output(colorString(0xff, 0xff, 0xff));
    console.output("*[" + screenId + "]");

    // render all layers
    for (Element layer : screen.getLayerElements()) {
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
      String offsetString = offset + colorString(255, 255, 0) + getIdText(ww) + " " + ww.getElementType().getClass().getSimpleName() 
      + "\n" + offset + "childLayout [" + ww.getElementType().getAttributes().get("childLayout") + "]" + colorString(255, 255, 255);  
      console.output(offsetString + "\n" + StringHelper.whitespace(offset.length()) + ww.getElementStateString(StringHelper.whitespace(offset.length())));
      outputElement(ww, offset + " ");
    }
  }

  private void outputEffect(final Element w, final String offset) {
    for (Element ww : w.getElements()) {
      console.output(offset + getIdText(ww) + outputEffects(ww, offset));
      outputEffect(ww, offset + "  ");
    }
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
      return "[---]";
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
