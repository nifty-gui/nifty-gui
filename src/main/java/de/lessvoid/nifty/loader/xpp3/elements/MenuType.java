package de.lessvoid.nifty.loader.xpp3.elements;


import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ImageType.
 * @author void
 */
public class MenuType extends ElementType {

  /**
   * create it.
   */
  public MenuType(
      final TypeContext typeContext) {
    super(typeContext);
  }

  /**
   * create element.
   * @param parent parent
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
    PanelRenderer renderer = new PanelRenderer();
    Element panel = new Element(
        this,
        getAttributes().getId(),
        parent,
        screen,
        false,
        renderer);
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
