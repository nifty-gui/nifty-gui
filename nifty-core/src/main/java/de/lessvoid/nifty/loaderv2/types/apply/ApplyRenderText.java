package de.lessvoid.nifty.loaderv2.types.apply;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.xml.xpp3.Attributes;

public class ApplyRenderText implements ApplyRenderer {
  private final Convert convert;

  public ApplyRenderText(final Convert convertParam) {
    convert = convertParam;
  }

  public void apply(
      final Element element,
      final Attributes attributes,
      final NiftyRenderEngine renderEngine) {
    TextRenderer textRenderer = element.getRenderer(TextRenderer.class);
    if (textRenderer == null) {
      return;
    }
    textRenderer.setFont(convert.font(renderEngine, attributes.get("font")));
    textRenderer.setTextHAlign(convert.textHorizontalAlign(attributes.get("textHAlign")));
    textRenderer.setTextVAlign(convert.textVerticalAlign(attributes.get("textVAlign")));
    textRenderer.setColor(convert.color(attributes.get("color")));
    textRenderer.setTextSelectionColor(convert.color(attributes.get("selectionColor")));
    textRenderer.setText(attributes.get("text"));
    textRenderer.setTextLineHeight(convert.sizeValue(attributes.get("textLineHeight")));
    textRenderer.setTextMinHeight(convert.sizeValue(attributes.get("textMinHeight")));
    boolean wrap = attributes.getAsBoolean("wrap", false);
    textRenderer.setLineWrapping(wrap);

    if (!wrap) {
      if (element.getConstraintWidth() == null) {
        element.setConstraintWidth(convert.sizeValue(textRenderer.getTextWidth() + "px"));
      }
      if (element.getConstraintHeight() == null) {
        element.setConstraintHeight(convert.sizeValue(textRenderer.getTextHeight() + "px"));
      }
    }
  }
}
