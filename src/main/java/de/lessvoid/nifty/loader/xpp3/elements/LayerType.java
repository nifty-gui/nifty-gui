package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.elements.helper.NiftyCreator;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;


/**
 * LayerType.
 * @author void
 */
public class LayerType extends PanelType {

  /**
   * Create LayerType.
   * @param typeContext TypeContext
   */
  public LayerType(final TypeContext typeContext, final Attributes attributesParam) {
    super(typeContext, attributesParam);
  }

  /**
   * Create Layer.
   * @param root Root Element to attach to
   * @param nifty nifty
   * @param screen screen
   * @param registeredEffects effects
   * @param registeredControls registeredControls
   * @param styleHandler style handler
   * @param time time
   * @param inputControl input control
   * @param screenController screen controller
   * @return element
   */
  public Element createElement(
      final Element root,
      final Nifty nifty,
      final Screen screen,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final StyleHandler styleHandler,
      final TimeProvider time,
      final NiftyInputControl inputControl,
      final ScreenController screenController) {
    Element layer = NiftyCreator.createLayer(
        root,
        this,
        getAttributes().getId(),
        nifty,
        screen,
        getAttributes(),
        typeContext.time);
    super.addAllElementAttributes(
        layer,
        screen,
        screenController,
        inputControl);
    root.add(layer);
    return layer;
  }
}
