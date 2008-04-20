package de.lessvoid.nifty.loader.xpp3.elements;


import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlElementProcessor;
import de.lessvoid.nifty.loader.xpp3.XmlParser;

public class LayerGroupType implements XmlElementProcessor {

  public void process(final XmlParser xmlParser, Attributes attributes) throws Exception {
    String id = attributes.get("id");

    xmlParser.nextTag();
    xmlParser.zeroOrMore("layer", new ElementType());
  }
}
