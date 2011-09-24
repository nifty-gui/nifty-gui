package de.lessvoid.nifty.html;

import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;

public class NiftyBuilderFactory {
  public PanelBuilder createPanelBuilder() {
    return new PanelBuilder();
  }

  public TextBuilder createTextBuilder() {
    return new TextBuilder();
  }

  public ImageBuilder createImageBuilder() {
    return new ImageBuilder();
  }
}
