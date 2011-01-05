package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.ControlDefinitionCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlDefinitionType;
import de.lessvoid.nifty.screen.Screen;

public class ControlDefinitionBuilder extends ElementBuilder {
  private ControlDefinitionCreator creator;
  
  public ControlDefinitionBuilder(String name) {
    creator = new ControlDefinitionCreator(name);
    initialize(creator);
  }

  public void registerControlDefintion(final Nifty nifty) {
    ControlDefinitionType controlDefinitionType = (ControlDefinitionType) buildElementType();
    controlDefinitionType.translateSpecialValues(nifty, null);
    controlDefinitionType.makeFlat();
    nifty.registerControlDefintion(controlDefinitionType);
  }

  @Override
  protected Element buildInternal(Nifty nifty, Screen screen, Element parent) {
    return null;
  }
}
