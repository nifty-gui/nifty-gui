package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.loaderv2.types.helper.NullElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlDefinitionType extends ElementType {
  public ControlDefinitionType() {
    super();
  }

  public ControlDefinitionType(final ControlDefinitionType src) {
    super(src);
  }

  public ElementType copy() {
    return new ControlDefinitionType(this);
  }

  public ControlDefinitionType(final Attributes attributes) {
    super(attributes);
  }

  public void makeFlat() {
    super.makeFlat();
    setTagName("<controlDefinition>");
    setElementRendererCreator(new NullElementRendererCreator());
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
