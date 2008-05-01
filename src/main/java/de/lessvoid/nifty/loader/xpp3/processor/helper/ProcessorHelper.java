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
   * @param xmlParser TODO
   * @param element element
   * @param attributes attributes
   * @throws Exception exception
   */
  public static void processElement(
      final XmlParser xmlParser,
      final ElementType element,
      final Attributes attributes) throws Exception {

    // id
    if (attributes.isSet("id")) {
      element.setId(attributes.get("id"));
    }

    // visible
    if (attributes.isSet("visible")) {
      element.setVisible(attributes.getAsBoolean("visible"));
    }

    // height
    if (attributes.isSet("height")) {
      element.setHeight(attributes.get("height"));
    }

    // width
    if (attributes.isSet("width")) {
      element.setWidth(attributes.get("width"));
    }

    // horizontal align
    if (attributes.isSet("align")) {
      element.setAlign(attributes.getAsAlignType("align"));
    }

    // vertical align
    if (attributes.isSet("valign")) {
      element.setValign(attributes.getAsVAlignType("valign"));
    }

    // child clip
    if (attributes.isSet("childClip")) {
      element.setChildClip(attributes.getAsBoolean("childClip"));
    }

    // visibleToMouse
    if (attributes.isSet("visibleToMouse")) {
      element.setVisibleToMouse(attributes.getAsBoolean("visibleToMouse"));
    }

    // childLayout
    if (attributes.isSet("childLayout")) {
      element.setChildLayoutType(attributes.getAsLayoutType("childLayout"));
    }

    // backgroundImage
    if (attributes.isSet("backgroundImage")) {
      element.setBackgroundImage(attributes.get("backgroundImage"));
    }

    // backgroundColor
    if (attributes.isSet("backgroundColor")) {
      element.setBackgroundColor(new ColorType(attributes.get("backgroundColor")));
    }

    xmlParser.nextTag();
    xmlParser.optional("interact", new InteractTypeProcessor(element));
    xmlParser.optional("hover", new HoverTypeProcessor(element));
    xmlParser.optional("effect", new EffectsTypeProcessor(element));
    xmlParser.zeroOrMore(
          new SubstitutionGroup().
            add("panel", new PanelTypeProcessor(element)).
            add("text", new TextTypeProcessor(element)).
            add("image", new ImageTypeProcessor(element)).
            add("menu", new MenuTypeProcessor(element)).
            add("control", new ControlTypeProcessor(element))
            );
  }
}
