package de.lessvoid.nifty.effects.impl;


import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * Color - color overlay.
 * @author void
 */
public class Gradient implements EffectImpl {
  private List < Entry > entries = new ArrayList < Entry > ();
  private boolean horizontal = false;

  public void activate(final Nifty nifty, final Element element, final EffectProperties parameter) {
    entries.clear();
    for (Attributes entry : parameter.getEffectValues().getValues()) {
      SizeValue offset = new SizeValue(entry.get("offset"));
      Color color = entry.getAsColor("color");
      entries.add(new Entry(offset, color));
    }
    horizontal = "horizontal".equals(parameter.getProperty("direction", "vertical"));
  }

  public void execute(
      final Element element,
      final float normalizedTime,
      final Falloff falloff,
      final NiftyRenderEngine r) {
    if (normalizedTime > 0.0f) {
      if (horizontal) {
        for (int i=1; i<entries.size(); i++) {
          Entry entry1 = entries.get(i-1);
          Entry entry2 = entries.get(i);
          r.renderQuad(
              (int)Math.round(element.getX() + entry1.offset.getValue(element.getWidth())),
              element.getY(),
              (int)Math.round(entry2.offset.getValue(element.getWidth()) - entry1.offset.getValue(element.getWidth())),
              element.getHeight(),
              entry1.color,
              entry2.color,
              entry2.color,
              entry1.color);
        }
      } else {
        for (int i=1; i<entries.size(); i++) {
          Entry entry1 = entries.get(i-1);
          Entry entry2 = entries.get(i);
          int yStart = (int)Math.round(element.getY() + entry1.offset.getValue(element.getHeight()));
          int yEnd = (int)Math.round(element.getY() + entry2.offset.getValue(element.getHeight()));
          r.renderQuad(
              element.getX(),
              yStart,
              element.getWidth(),
              yEnd - yStart,
              entry1.color,
              entry1.color,
              entry2.color,
              entry2.color);
        }
      }
    }
  }
  
  public void deactivate() {
  }

  private class Entry {
    public SizeValue offset;
    public Color color;

    public Entry(final SizeValue offset, final Color color) {
      this.offset = offset;
      this.color = color;
    }
  }
}
