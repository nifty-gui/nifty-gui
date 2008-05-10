package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.ControlController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.controls.MenuFocusHandler;
import de.lessvoid.nifty.elements.controls.MenuItemControl;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.loader.xpp3.elements.helper.NiftyCreator;
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
   * color.
   * @optional
   */
  private ColorType color = new ColorType();

  /**
   * font.
   */
  private String font;

  private MenuFocusHandler focusHandler;

  /**
   * create it.
   * @param fontParam font
   */
  public MenuItemType(final MenuFocusHandler focusHandlerParam, final String fontParam) {
    this.focusHandler = focusHandlerParam;
    this.font = fontParam;
  }

  /**
   * set text.
   * @param textParam text
   */
  public void setText(final String textParam) {
    this.text = textParam;
  }

  /**
   * set color.
   * @param colorParam color
   */
  public void setColor(final ColorType colorParam) {
    this.color = colorParam;
  }

  /**
   * create element.
   * @param parent parent
   * @param nifty nifty
   * @param screen screen
   * @param registeredEffects registeredEffects
   * @param registeredControls registeredControls
   * @param time time
   * @param controlController controlController
   * @param screenController screenController
   * @return element
   */
  public Element createElement(
      final Element parent,
      final Nifty nifty,
      final Screen screen,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final TimeProvider time,
      final ControlController controlController,
      final ScreenController screenController) {
    TextRenderer textRenderer = NiftyCreator.createTextRenderer(nifty, color, text, font);

    PanelRenderer renderer = NiftyCreator.createPanelRenderer(
        nifty.getRenderDevice(),
        getBackgroundColor().createColor(),
        getBackgroundImage());

    Element menuItem = new Element(
        getId(),
        parent,
        screen,
        true,
        renderer,
        textRenderer);

    menuItem.setConstraintHeight(new SizeValue(textRenderer.getTextHeight() + "px"));
    menuItem.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));

    focusHandler.addElement(menuItem);

    MenuItemControl control = new MenuItemControl(focusHandler);
    control.bind(screen, menuItem, null, null);

    super.addElementAttributes(
        menuItem,
        screen,
        nifty,
        registeredEffects,
        registeredControls,
        time,
        control,
        screenController);

    parent.add(menuItem);
    parent.setConstraintWidth(getMenuMaxWidth(parent.getElements()));
    parent.setConstraintHeight(getMenuMaxHeight(parent.getElements()));
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
