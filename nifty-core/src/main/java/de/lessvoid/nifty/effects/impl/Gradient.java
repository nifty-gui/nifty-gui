package de.lessvoid.nifty.effects.impl;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Color - color overlay.
 *
 * @author void
 */
public class Gradient implements EffectImpl {
  @Nonnull
  private final List<Entry> entries = new ArrayList<Entry>();
  private boolean horizontal = false;

  @Override
  public void activate(
      @Nonnull final Nifty nifty,
      @Nonnull final Element element,
      @Nonnull final EffectProperties parameter) {
    entries.clear();
    for (Attributes entry : parameter.getEffectValues().getValues()) {
      SizeValue offset = new SizeValue(entry.get("offset"));
      Color color = entry.getAsColor("color");
      if (color != null) {
        entries.add(new Entry(offset, color));
      }
    }
    horizontal = "horizontal".equals(parameter.getProperty("direction", "vertical"));
  }

  @Override
  public void execute(
      @Nonnull final Element element,
      final float normalizedTime,
      @Nullable final Falloff falloff,
      @Nonnull final NiftyRenderEngine r) {
    if (normalizedTime > 0.0f) {
      if (horizontal) {
        for (int i = 1; i < entries.size(); i++) {
          Entry entry1 = entries.get(i - 1);
          Entry entry2 = entries.get(i);
          r.renderQuad(
              element.getX() + entry1.offset.getValueAsInt(element.getWidth()),
              element.getY(),
              entry2.offset.getValueAsInt(element.getWidth()) - entry1.offset.getValueAsInt(element.getWidth()),
              element.getHeight(),
              entry1.color,
              entry2.color,
              entry2.color,
              entry1.color);
        }
      } else {
        for (int i = 1; i < entries.size(); i++) {
          Entry entry1 = entries.get(i - 1);
          Entry entry2 = entries.get(i);
          int yStart = element.getY() + entry1.offset.getValueAsInt(element.getHeight());
          int yEnd = element.getY() + entry2.offset.getValueAsInt(element.getHeight());
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

  @Override
  public void deactivate() {
  }

  private static class Entry {
    @Nonnull
    public final SizeValue offset;
    @Nonnull
    public final Color color;

    public Entry(@Nonnull final SizeValue offset, @Nonnull final Color color) {
      this.offset = offset;
      this.color = color;
    }
  }
}
