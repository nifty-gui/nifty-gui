package de.lessvoid.nifty.loader.xpp3.elements;


import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

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
  public TextType(
      final TypeContext typeContext,
      final String textParam) {
    super(typeContext);
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
    TextRenderer textRenderer = new TextRenderer();
    Element panel = new Element(
        this,
        getAttributes().getId(),
        parent,
        screen,
        false,
        typeContext.time,
        new PanelRenderer(),
        textRenderer);
    super.addElementAttributes(
        panel,
        screen,
        screenController,
        inputControl);
    ValignType textValignType = getAttributes().getTextVAlign();
    if (textValignType != null) {
      textRenderer.setTextVAlign(VerticalAlign.valueOf(textValignType.getValue()));
    }
    AlignType textHalignType = getAttributes().getTextHAlign();
    if (textHalignType != null) {
      textRenderer.setTextHAlign(HorizontalAlign.valueOf(textHalignType.getValue()));
    }
    if (textSelectionColor != null) {
      textRenderer.setTextSelectionColor(new Color(textSelectionColor));
    }
    textRenderer.setText(text);
    if (panel.getConstraintHeight() == null) {
      panel.setConstraintHeight(new SizeValue(textRenderer.getTextHeight() + "px"));
    }
//    panel.setConstraintWidth(new SizeValue(textRenderer.getTextWidth() + "px"));

    parent.add(panel);
    return panel;
  }
}
