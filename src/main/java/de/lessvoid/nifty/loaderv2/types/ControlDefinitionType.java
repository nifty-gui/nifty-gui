package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.tools.StringHelper;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlDefinitionType extends ElementType {
  public ControlDefinitionType() {
    super();
  }

  public ControlDefinitionType(final Attributes attributes) throws Exception {
    super(attributes);
  }

  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<controlDefinition> " + super.output(offset);
  }

  protected ElementRenderer[] createElementRenderer(final Nifty nifty) {
    return null;
  }

  public String getName() {
    return getAttributes().get("name");
  }

  public ElementType getControlElementType() {
    if (!hasElements()) {
      return null;
    }

    return getFirstElement();
  }
}
