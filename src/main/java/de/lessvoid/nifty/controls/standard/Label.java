package de.lessvoid.nifty.controls.standard;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.StandardAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class Label {
  private StandardAttributes attributes = new StandardAttributes();

  public Label(final String text) {
    attributes.setText(text);
  }

  public StandardAttributes getStandardAttributes() {
    return attributes;
  }

  public Element createElement(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    return attributes.createLabel(nifty, screen, parent);
  }
}
