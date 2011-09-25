package de.lessvoid.nifty.html;

import java.util.logging.Logger;

import de.lessvoid.nifty.builder.ElementBuilder.Align;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;

public class NiftyBuilderFactory {
  private Logger log = Logger.getLogger(NiftyBuilderFactory.class.getName());

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

  public ImageBuilder createImageBuilder(final String src, final String align, final String width, final String height, final String bgcolor, final String vspace) {
    ImageBuilder imageBuilder = createImageBuilder();
    imageBuilder.filename(src);
    if (align != null) {
      imageBuilder.align(translateAlign(align));
    }
    if (width != null) {
      imageBuilder.width(width);
    }
    if (height != null) {
      imageBuilder.height(height);
    }
    if (bgcolor != null) {
      imageBuilder.backgroundColor(bgcolor);
    }
    if (vspace != null) {
      imageBuilder.padding(vspace);
    }
    return imageBuilder;
  }

  public PanelBuilder createBreakPanelBuilder(final String height) {
    PanelBuilder result = createPanelBuilder();
    result.height(height);
    return result;
  }

  PanelBuilder createPanelBuilder() {
    return new PanelBuilder();
  }

  TextBuilder createTextBuilder() {
    return new TextBuilder();
  }

  ImageBuilder createImageBuilder() {
    return new ImageBuilder();
  }

  private String removeNewLine(final String text) {
    return text.replaceAll("\n", " ");
  }

  private Align translateAlign(final String align) {
    if ("left".equalsIgnoreCase(align)) {
      return Align.Left;
    } else if ("right".equalsIgnoreCase(align)) {
        return Align.Right;
    } else if ("middle".equalsIgnoreCase(align)) {
      return Align.Center;
    // TODO: "center" is not really supported in HTML http://de.selfhtml.org/html/referenz/attribute.htm#img
    } else if ("center".equalsIgnoreCase(align)) {
      return Align.Center;
    } else {
      // default to left
      log.warning("Unknown align type [" + align + "] detected. Will default to Align.LEFT");
      return Align.Left;
    }
  }
}
