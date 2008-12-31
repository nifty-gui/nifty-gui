package de.lessvoid.nifty.loader.xpp3.processor.helper;

import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.loader.xpp3.elements.ControlType;
import de.lessvoid.nifty.loader.xpp3.elements.ImageType;
import de.lessvoid.nifty.loader.xpp3.elements.LabelType;
import de.lessvoid.nifty.loader.xpp3.elements.LayerType;
import de.lessvoid.nifty.loader.xpp3.elements.PanelType;
import de.lessvoid.nifty.loader.xpp3.elements.PopupType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterControlDefinitionType;
import de.lessvoid.nifty.loader.xpp3.elements.RegisterEffectType;
import de.lessvoid.nifty.loader.xpp3.elements.TextType;
import de.lessvoid.nifty.loader.xpp3.elements.helper.StyleHandler;
import de.lessvoid.nifty.tools.TimeProvider;

public class TypeContext {
  public StyleHandler styleHandler;
  public Nifty nifty;
  public Map < String, RegisterEffectType > registeredEffects;
  public Map < String, RegisterControlDefinitionType > registeredControls;
  public TimeProvider time;

  /**
   * @param styleHandler
   * @param nifty
   * @param registeredEffects
   * @param time
   */
  public TypeContext(
      final StyleHandler styleHandler,
      final Nifty nifty,
      final Map < String, RegisterEffectType > registeredEffects,
      final Map < String, RegisterControlDefinitionType > registeredControls,
      final TimeProvider time) {
    this.styleHandler = styleHandler;
    this.nifty = nifty;
    this.registeredEffects = registeredEffects;
    this.registeredControls = registeredControls;
    this.time = time;
  }

  public LabelType createLabelType(final String textParam) {
    Attributes attributes = new Attributes();
    attributes.set("text", textParam);
    return createLabelType(attributes);
  }

  public LabelType createLabelType(final Attributes attributes) {
    if (!attributes.isSet("style")) {
      attributes.set("style", "nifty-label");
    }
    return new LabelType(this, attributes, attributes.get("text"));
  }

  public ControlType createControlType(final Attributes attributes) {
    ControlType controlType = new ControlType(this, attributes, attributes.get("name"));
    if (attributes.isSet("onChange")) {
      controlType.setOnChange(attributes.get("onChange"));
    }
    return controlType;
  }

  public ImageType createImageType(final Attributes attributes) {
    ImageType imageType = new ImageType(this, attributes, attributes.get("filename"));
    if (attributes.isSet("filter")) {
      imageType.setFilter(attributes.getAsBoolean("filer"));
    }
    return imageType;
  }

  public PanelType createPanelType(final Attributes attributes) {
    return new PanelType(this, attributes);
  }

  public LayerType createLayerType(final Attributes attributes) {
    return new LayerType(this, attributes);
  }

  public PopupType createPopupType(final Attributes attributes) {
    return new PopupType(this, attributes, attributes.get("controller"));
  }

  public TextType createTextType(final Attributes attributes) {
    return new TextType(this, attributes, attributes.get("text"));
  }
}
