package de.lessvoid.nifty.loader.xpp3.processor.helper;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.SubstitutionGroup;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.AttributesType;
import de.lessvoid.nifty.loader.xpp3.elements.ColorType;
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
   * @throws Exception exception
   */
  public static void processElement(
      final XmlParser xmlParser,
      final ElementType elementType,
      final Attributes attributes) throws Exception {
    AttributesType attributesType = new AttributesType();
    processAttributes(xmlParser, attributesType, attributes);
    elementType.setAttributes(attributesType);

    xmlParser.nextTag();
    xmlParser.optional("interact", new InteractTypeProcessor(elementType));
    xmlParser.optional("hover", new HoverTypeProcessor(elementType));
    xmlParser.optional("effect", new EffectsTypeProcessor(elementType));
    xmlParser.zeroOrMore(
          new SubstitutionGroup().
            add("panel", new PanelTypeProcessor(elementType)).
            add("text", new TextTypeProcessor(elementType)).
            add("label", new LabelTypeProcessor(elementType)).
            add("image", new ImageTypeProcessor(elementType)).
            add("menu", new MenuTypeProcessor(elementType)).
            add("control", new ControlTypeProcessor(elementType))
            );
  }

  /**
   * process element attributes.
   * @param xmlParser xmlParser
   * @param attributesType elementType
   * @param attributes attributes
   * @throws Exception exception
   */
  public static void processAttributes(
      final XmlParser xmlParser,
      final AttributesType attributesType,
      final Attributes attributes) throws Exception {

    // id
    if (attributes.isSet("id")) {
      attributesType.setId(attributes.get("id"));
    }
    // style
    if (attributes.isSet("style")) {
      attributesType.setStyle(attributes.get("style"));
    }
    // visible
    if (attributes.isSet("visible")) {
      attributesType.setVisible(attributes.getAsBoolean("visible"));
    }
    // height
    if (attributes.isSet("height")) {
      attributesType.setHeight(attributes.get("height"));
    }
    // width
    if (attributes.isSet("width")) {
      attributesType.setWidth(attributes.get("width"));
    }
    // x
    if (attributes.isSet("x")) {
      attributesType.setX(attributes.get("x"));
    }
    // y
    if (attributes.isSet("y")) {
      attributesType.setY(attributes.get("y"));
    }
    // horizontal align
    if (attributes.isSet("align")) {
      attributesType.setAlign(attributes.getAsAlignType("align"));
    }
    // vertical align
    if (attributes.isSet("valign")) {
      attributesType.setValign(attributes.getAsVAlignType("valign"));
    }
    // child clip
    if (attributes.isSet("childClip")) {
      attributesType.setChildClip(attributes.getAsBoolean("childClip"));
    }
    // visibleToMouse
    if (attributes.isSet("visibleToMouse")) {
      attributesType.setVisibleToMouse(attributes.getAsBoolean("visibleToMouse"));
    }
    // childLayout
    if (attributes.isSet("childLayout")) {
      attributesType.setChildLayoutType(attributes.getAsLayoutType("childLayout"));
    }
    // backgroundImage
    if (attributes.isSet("backgroundImage")) {
      attributesType.setBackgroundImage(attributes.get("backgroundImage"));
    }
    // backgroundColor
    if (attributes.isSet("backgroundColor")) {
      attributesType.setBackgroundColor(new ColorType(attributes.get("backgroundColor")));
    }
    // font
    if (attributes.isSet("font")) {
      attributesType.setFont(attributes.get("font"));
    }
    // color
    if (attributes.isSet("color")) {
      attributesType.setColor(new ColorType(attributes.get("color")));
    }
  }
}
