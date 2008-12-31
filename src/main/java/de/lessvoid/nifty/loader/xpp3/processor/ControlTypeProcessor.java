package de.lessvoid.nifty.loader.xpp3.processor;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ControlType;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.processor.helper.ProcessorHelper;
import de.lessvoid.nifty.loader.xpp3.processor.helper.TypeContext;

public class ControlTypeProcessor implements XmlElementProcessor {
  private ElementType element;
  private TypeContext typeContext;

  public ControlTypeProcessor(final TypeContext typeContextParam, final ElementType elementParam) {
    this.element = elementParam;
    this.typeContext = typeContextParam;
  }

  public void process(final XmlParser xmlParser, final Attributes attributes) throws Exception {
    ControlType controlType = typeContext.createControlType(attributes);
    ProcessorHelper.processElement(xmlParser, controlType, typeContext);
    element.addElementType(controlType);
  }
}
