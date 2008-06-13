package de.lessvoid.nifty.loader.xpp3.elements;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * ImageType.
 * @author void
 */
public class TextType extends ElementType {

  /**
   * filename.
   * @required
   */
  private String text;

  /**
   * text selection color.
   */
  private String textSelectionColor;

  /**
   * create it.
   * @param textParam filename
   */
  public TextType(final String textParam) {
    this.text = textParam;
  }

  /**
   * set text.
   * @param textParam text
   */
  public void setText(final String textParam) {
    this.text = textParam;
  }

  /**
   * set text selection color.
   * @param textSelectionColorParam new text selection color
   */
  public void setTextSelectionTextColor(final String textSelectionColorParam) {
    textSelectionColor = textSelectionColorParam;
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
   * @param inputControl controlController
   * @param screenController screenController
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
      final NiftyInputControl inputControl,
      final ScreenController screenController) {
    TextRenderer textRenderer = new TextRenderer();
    Element panel = new Element(
        getAttributes().getId(),
        parent,
        screen,
        false,
        new PanelRenderer(),
        textRenderer);
    super.addElementAttributes(
        panel,
        screen,
        nifty,
        registeredEffects,
        registeredControls,
        styleHandler,
        time,
        inputControl,
        screenController);
    textRenderer.setText(text);
    if (textSelectionColor != null) {
      textRenderer.setTextSelectionColor(new Color(textSelectionColor));
    }/*
    if (panel.getConstraintHeight() == null) {
      panel.setConstraintHeight(new SizeValue(textRenderer.getTextHeight() + "px"));
    }
    if (panel.getConstraintWidth() == null) {
      panel.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));
    }*/
    parent.add(panel);
    return panel;
  }
}
