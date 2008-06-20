package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.MenuItemControl;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.mapping.Default;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * ImageType.
 * @author void
 */
public class MenuItemType extends ElementType {

  /**
   * text.
   * @optional
   */
  private String text;

  /**
   * focus handler.
   */
  private FocusHandler focusHandler;

  /**
   * create it.
   * @param focusHandlerParam focusHandler
   */
  public MenuItemType(final FocusHandler focusHandlerParam) {
    this.focusHandler = focusHandlerParam;
  }

  /**
   * set text.
   * @param textParam text
   */
  public void setText(final String textParam) {
    this.text = textParam;
  }

  /**
   * create element.
   * @param parent parent
   * @param nifty nifty
   * @param screen screen
   * @param registeredEffects registeredEffects
   * @param registeredControls registeredControls
   * @param styleHandler style handler
   * @param time time
   * @param screenController screenController
   * @param inputControlParam controlController
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final StyleHandler styleHandler,
      final TimeProvider time,
      final NiftyInputControl inputControlParam,
      final ScreenController screenController) {
    TextRenderer textRenderer = new TextRenderer();
    Element menuItem = new Element(
        this,
        getAttributes().getId(),
        parent,
        screen,
        true,
        new PanelRenderer(), textRenderer);

    focusHandler.addElement(menuItem);

    MenuItemControl control = new MenuItemControl(focusHandler);
    control.bind(nifty, screen, menuItem, null, null);

    final NiftyInputMapping inputMapping = new Default();
    NiftyInputControl inputControl = new NiftyInputControl(control, inputMapping);

    super.addElementAttributes(
        menuItem,
        screen,
        nifty,
        registeredEffects,
        registeredControls,
        styleHandler,
        time,
        inputControl,
        screenController);

    textRenderer.setText(text);
    menuItem.setConstraintHeight(new SizeValue(textRenderer.getTextHeight() + "px"));
    menuItem.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));

    parent.add(menuItem);
    parent.setConstraintWidth(getMenuMaxWidth(parent.getElements()));
    parent.setConstraintHeight(getMenuMaxHeight(parent.getElements()));

    ElementType.applyControlParameters(menuItem, getAttributes().getSrcAttributes(), nifty, screen);
    return menuItem;
  }

  /**
   * get max height of all elements.
   * @param elements the elements to check
   * @return max height
   */
  private static SizeValue getMenuMaxHeight(final List < Element > elements) {
    if (elements == null || elements.isEmpty()) {
      return null;
    }

    int sum = 0;
    for (Element e : elements) {
      SizeValue current = e.getConstraintHeight();
      if (current.isPercentOrPixel()) {
        sum += current.getValueAsInt(0);
      }
    }
    return new SizeValue(sum + "px");
  }

  /**
   * get max width of all elements.
   * @param elements the elements to check
   * @return max width
   */
  private static SizeValue getMenuMaxWidth(final List < Element > elements) {
    if (elements == null || elements.isEmpty()) {
      return null;
    }

    int max = -1;
    for (Element e : elements) {
      SizeValue current = e.getConstraintWidth();
      if (current.isPercentOrPixel()) {
        int value = current.getValueAsInt(0);
        if (value > max) {
          max = value;
        }
      }
    }
    return new SizeValue(max + "px");
  }
}
