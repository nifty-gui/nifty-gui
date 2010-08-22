package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.TextCreator;
import de.lessvoid.nifty.elements.Element;

public class TextBuilder extends ElementBuilder {

  protected String text;

  @Override
  public Element build(Nifty nifty) {

    validate();

    Element text = new TextCreator(id, this.text) {
      {
        for (String name : elementAttributes.keySet())
          set(name, elementAttributes.get(name));

        setEffects(effectsAttributes);
        setInteract(interactAttributes);

      }
    }.create(nifty, screen, parent);

    addChildrenFor(nifty, text);

    return text;
  }

  public void text(String text) {
    this.text = text;
  }

}
