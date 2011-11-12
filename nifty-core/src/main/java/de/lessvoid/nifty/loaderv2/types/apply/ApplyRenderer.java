package de.lessvoid.nifty.loaderv2.types.apply;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.xml.xpp3.Attributes;

public interface ApplyRenderer {
  void apply(Element element, Attributes attributes, NiftyRenderEngine renderEngine);
}
