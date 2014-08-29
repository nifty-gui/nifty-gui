package de.lessvoid.nifty.examples.defaultcontrols.common;

import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;

import javax.annotation.Nonnull;

/**
 * This is a helper class of reused builders so that we don't repeat ourself too much.
 *
 * @author void
 */
public class CommonBuilders {

  @Nonnull
  public EffectBuilder createMoveEffect(@Nonnull final String mode, @Nonnull final String direction, final int length) {
    return new EffectBuilder("move") {{
      effectParameter("mode", mode);
      effectParameter("direction", direction);
      effectParameter("timeType", "exp");
      effectParameter("factor", "3.5");
      length(length);
      startDelay(0);
      inherit(true);
    }};
  }

  @Nonnull
  public EffectBuilder createFadeEffect() {
    return new EffectBuilder("fade") {{
      effectParameter("start", "#f");
      effectParameter("end", "#0");
      length(300);
      startDelay(300);
      inherit(true);
    }};
  }

  @Nonnull
  public PanelBuilder vspacer() {
    return new PanelBuilder() {{
      childLayoutHorizontal();
      height("9px");
      width("0px");
    }};
  }

  @Nonnull
  public PanelBuilder vspacer(@Nonnull final String height) {
    return new PanelBuilder() {{
      childLayoutHorizontal();
      height(height);
      width("100%");
    }};
  }

  @Nonnull
  public PanelBuilder hspacer(@Nonnull final String width) {
    return new PanelBuilder() {{
      width(width);
      height("0px");
    }};
  }

  @Nonnull
  public LabelBuilder createLabel(@Nonnull final String text) {
    return createLabel(text, "100px");
  }

  @Nonnull
  public LabelBuilder createLabel(@Nonnull final String text, @Nonnull final String width) {
    return new LabelBuilder() {{
      text(text);
      width(width);
      alignLeft();
      textVAlignCenter();
      textHAlignLeft();
    }};
  }

  @Nonnull
  public LabelBuilder createLabel(@Nonnull final String id, @Nonnull final String text, @Nonnull final String width) {
    return new LabelBuilder(id, text) {{
      width(width);
      alignLeft();
      textVAlignCenter();
      textHAlignLeft();
    }};
  }

  @Nonnull
  public LabelBuilder createShortLabel(@Nonnull final String name, @Nonnull final String width) {
    LabelBuilder result = createShortLabel(name);
    result.width(width);
    return result;
  }

  @Nonnull
  public LabelBuilder createShortLabel(@Nonnull final String name) {
    return new LabelBuilder() {{
      text(name);
      alignLeft();
      textVAlignCenter();
      textHAlignLeft();
    }};
  }
}
