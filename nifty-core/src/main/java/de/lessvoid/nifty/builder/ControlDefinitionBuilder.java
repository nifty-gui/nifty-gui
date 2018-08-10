package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.dynamic.ControlDefinitionCreator;
import de.lessvoid.nifty.loaderv2.types.ControlDefinitionType;

import javax.annotation.Nonnull;

public class ControlDefinitionBuilder extends ElementBuilder {
  @Nonnull
  private final ControlDefinitionCreator creator;

  private ControlDefinitionBuilder(@Nonnull final ControlDefinitionCreator creator) {
    super(creator);
    this.creator = creator;
  }

  public ControlDefinitionBuilder(@Nonnull String name) {
    this(new ControlDefinitionCreator(name));
  }

  @Override
  public ControlDefinitionBuilder controller(@Nonnull final Controller controller) {
    creator.setController(controller.getClass().getName());
    return this;
  }

  @Override
  public ControlDefinitionBuilder controller(@Nonnull final String controllerClass) {
    creator.setController(controllerClass);
    return this;
  }

  @Nonnull
  public String controlParameter(@Nonnull final String parameterName) {
    return "$" + parameterName;
  }

  public void registerControlDefintion(@Nonnull final Nifty nifty) {
    ControlDefinitionType controlDefinitionType = (ControlDefinitionType) buildElementType();
    if (controlDefinitionType != null) {
      controlDefinitionType.translateSpecialValues(nifty, null);
      controlDefinitionType.makeFlat();
      nifty.registerControlDefintion(controlDefinitionType);
    }
  }
}
