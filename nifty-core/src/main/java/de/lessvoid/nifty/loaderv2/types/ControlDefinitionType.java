package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.loaderv2.types.helper.NullElementRendererCreator;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ControlDefinitionType extends ElementType {
  public ControlDefinitionType() {
    super();
  }

  public ControlDefinitionType(@Nonnull final Attributes attributes) {
    super(attributes);
  }

  public ControlDefinitionType(@Nonnull final ControlDefinitionType src) {
    super(src);
  }

  @Override
  @Nonnull
  public ElementType copy() {
    return new ControlDefinitionType(this);
  }

  @Override
  public void makeFlat() {
    super.makeFlat();
    setTagName("<controlDefinition>");
    setElementRendererCreator(new NullElementRendererCreator());
  }

  @Nullable
  public String getName() {
    return getAttributes().get("name");
  }

  @Nullable
  public ElementType getControlElementType() {
    if (!hasElements()) {
      return null;
    }

    return getFirstElement();
  }
}
