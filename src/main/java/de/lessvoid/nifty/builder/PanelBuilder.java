package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.elements.Element;

public class PanelBuilder extends ElementBuilder {

  @Override
  public Element build(Nifty nifty) {

    validate();

    Element panel = new PanelCreator(id) {
      {
        for (String name : elementAttributes.keySet())
          set(name, elementAttributes.get(name));

        setEffects(effectsAttributes);
        setInteract(interactAttributes);
      }
    }.create(nifty, screen, parent);

    addChildrenFor(nifty, panel);

    return panel;
  }

}
