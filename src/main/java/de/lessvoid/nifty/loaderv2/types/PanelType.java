package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.NiftyFactory;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class PanelType extends ElementType {
  public PanelType() {
    super();
  }

  public PanelType(final Attributes attributes) {
    super(attributes);
  }

  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<panel> " + super.output(offset);
  }

  public ElementRenderer[] createElementRenderer(final Nifty nifty) {
    return NiftyFactory.getPanelRenderer();
  }
}
