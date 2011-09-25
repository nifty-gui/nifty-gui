package de.lessvoid.nifty.html;

import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;

public class NiftyBuilderFactory {

  public TextBuilder createTextBuilder(final String text, final String defaultFontName, final String color) {
    TextBuilder textBuilder = createTextBuilder();
    textBuilder.text(removeNewLine(text));
    textBuilder.wrap(true);
    textBuilder.textHAlignLeft();
    textBuilder.textVAlignTop();
    textBuilder.font(defaultFontName);
    textBuilder.width("100%");
    if (color != null) {
      textBuilder.color(color);
    }
    return textBuilder;
  }

  public ImageBuilder createImageBuilder() {
    return new ImageBuilder();
  }

  public PanelBuilder createBodyPanelBuilder() {
    PanelBuilder builder = createPanelBuilder();
    builder.width("100%");
    builder.height("100%");
    builder.childLayoutVertical();
    return builder;
  }

  public PanelBuilder createParagraphPanelBuilder() {
    PanelBuilder builder = createPanelBuilder();
    builder.width("100%");
    builder.childLayoutVertical();
    return builder;
  }

  PanelBuilder createPanelBuilder() {
    return new PanelBuilder();
  }

  TextBuilder createTextBuilder() {
    return new TextBuilder();
  }

  private String removeNewLine(final String text) {
    return text.replaceAll("\n", " ");
  }
}
