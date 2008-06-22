package de.lessvoid.nifty.loader.xpp3.processor.helper;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.SubstitutionGroup;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.AttributesType;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.processor.ControlTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.EffectsTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.HoverTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.ImageTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.InteractTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.LabelTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.MenuTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.PanelTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.TextTypeProcessor;



/**
 * ElementType.
 * @author void
 */
public final class ProcessorHelper {

  /**
   * prevent instances of this class.
   */
  private ProcessorHelper() {
  }

  /**
   * process element attributes.
   * @param xmlParser xmlParser
   * @param elementType elementType
   * @param attributes attributes
   * @param typeContext typeContext
   * @throws Exception exception
   */
  public static void processElement(
      final XmlParser xmlParser,
      final ElementType elementType,
      final Attributes attributes,
      final TypeContext typeContext) throws Exception {
    AttributesType attributesType = new AttributesType(attributes);
    elementType.setAttributes(attributesType);

    xmlParser.nextTag();
    xmlParser.optional("interact", new InteractTypeProcessor(elementType));
    xmlParser.optional("hover", new HoverTypeProcessor(elementType));
    xmlParser.optional("effect", new EffectsTypeProcessor(elementType));
    xmlParser.zeroOrMore(
          new SubstitutionGroup().
            add("panel", new PanelTypeProcessor(typeContext, elementType)).
            add("text", new TextTypeProcessor(typeContext, elementType)).
            add("label", new LabelTypeProcessor(typeContext, elementType)).
            add("image", new ImageTypeProcessor(typeContext, elementType)).
            add("menu", new MenuTypeProcessor(typeContext, elementType)).
            add("control", new ControlTypeProcessor(typeContext, elementType))
            );
  }
}
