package de.lessvoid.nifty.controls.dynamic.attributes;

import de.lessvoid.nifty.loaderv2.types.EffectType;
import de.lessvoid.xml.xpp3.Attributes;

public class ControlEffectAttributes {
  protected Attributes attributes = new Attributes();

  public void setAttribute(final String name, final String value) {
    attributes.set(name, value);
  }

  public void setInherit(final String inherit) {
    attributes.set("inherit", inherit);
  }

  public void setPost(final String post) {
    attributes.set("post", post);
  }

  public void setOverlay(final String overlay) {
    attributes.set("overlay", overlay);
  }

  public void setAlternateEnable(final String alternateEnable) {
    attributes.set("alternateEnable", alternateEnable);
  }

  public void setName(final String name) {
    attributes.set("name", name);
  }

  public EffectType create() {
    EffectType effectType = new EffectType();
    effectType.initFromAttributes(attributes);
    return effectType;
  }

  public void refreshEffectType(final EffectType effectsType) {
    
  }
}
