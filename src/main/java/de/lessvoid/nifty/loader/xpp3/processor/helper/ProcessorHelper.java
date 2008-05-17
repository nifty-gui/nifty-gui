package de.lessvoid.nifty.loader.xpp3.processor.helper;

import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.SubstitutionGroup;
import de.lessvoid.nifty.loader.xpp3.XmlParser;
import de.lessvoid.nifty.loader.xpp3.elements.ColorType;
import de.lessvoid.nifty.loader.xpp3.elements.ElementType;
import de.lessvoid.nifty.loader.xpp3.processor.ControlTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.EffectsTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.HoverTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.ImageTypeProcessor;
import de.lessvoid.nifty.loader.xpp3.processor.InteractTypeProcessor;
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

    // id
    if (attributes.isSet("id")) {
      elementType.setId(attributes.get("id"));
    }

    // visible
    if (attributes.isSet("visible")) {
      elementType.setVisible(attributes.getAsBoolean("visible"));
    }

    // height
    if (attributes.isSet("height")) {
      elementType.setHeight(attributes.get("height"));
    }

    // width
    if (attributes.isSet("width")) {
      elementType.setWidth(attributes.get("width"));
    }

    // x
    if (attributes.isSet("x")) {
      elementType.setX(attributes.get("x"));
    }

    // y
    if (attributes.isSet("y")) {
      elementType.setY(attributes.get("y"));
    }

    // horizontal align
    if (attributes.isSet("align")) {
      elementType.setAlign(attributes.getAsAlignType("align"));
    }

    // vertical align
    if (attributes.isSet("valign")) {
      elementType.setValign(attributes.getAsVAlignType("valign"));
    }

    // child clip
    if (attributes.isSet("childClip")) {
      elementType.setChildClip(attributes.getAsBoolean("childClip"));
    }

    // visibleToMouse
    if (attributes.isSet("visibleToMouse")) {
      elementType.setVisibleToMouse(attributes.getAsBoolean("visibleToMouse"));
    }

    // childLayout
    if (attributes.isSet("childLayout")) {
      elementType.setChildLayoutType(attributes.getAsLayoutType("childLayout"));
    }

    // backgroundImage
    if (attributes.isSet("backgroundImage")) {
      elementType.setBackgroundImage(attributes.get("backgroundImage"));
    }

    // backgroundColor
    if (attributes.isSet("backgroundColor")) {
      elementType.setBackgroundColor(new ColorType(attributes.get("backgroundColor")));
    }

    xmlParser.nextTag();
    xmlParser.optional("interact", new InteractTypeProcessor(elementType));
    xmlParser.optional("hover", new HoverTypeProcessor(elementType));
    xmlParser.optional("effect", new EffectsTypeProcessor(elementType));
    xmlParser.zeroOrMore(
          new SubstitutionGroup().
            add("panel", new PanelTypeProcessor(elementType)).
            add("text", new TextTypeProcessor(elementType)).
            add("image", new ImageTypeProcessor(elementType)).
            add("menu", new MenuTypeProcessor(elementType)).
            add("control", new ControlTypeProcessor(elementType))
            );
  }
}
