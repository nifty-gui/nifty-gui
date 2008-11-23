package de.lessvoid.nifty.loader.xpp3.elements;

import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loader.xpp3.elements.helper.NiftyCreator;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * PanelType.
 * @author void
 */
public class PanelType extends ElementType {

  public PanelType(final TypeContext typeContext) {
    super(typeContext);
  }

  /**
   * create element.
   * @param parent parent parent
   * @param screen screen
   * @param inputControl controlController
   * @param screenController screenController
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Screen screen,
      final NiftyInputControl inputControl,
      final ScreenController screenController) {
    Element panel = new Element(
        typeContext.nifty,
        this,
        getAttributes().getId(),
        parent,
        screen,
        false,
        typeContext.time, NiftyCreator.getPanelRenderer(typeContext.nifty, getAttributes()));

    Element element = panel;
    super.addElementAttributes(
        element,
        screen,
        screenController,
        inputControl);
    parent.add(element);
    return element;
  }
}
