package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.LayerCreator;
import de.lessvoid.nifty.elements.Element;

public class LayerBuilder extends ElementBuilder {

  public Element build(Nifty nifty) {

    validate();

    Element layer = new LayerCreator(id) {
      {
        for (String name : elementAttributes.keySet())
          set(name, elementAttributes.get(name));

        setEffects(effectsAttributes);
        setInteract(interactAttributes);
      }
    }.create(nifty, screen, parent);

    addChildrenFor(nifty, layer);

    return layer;
  }
}
