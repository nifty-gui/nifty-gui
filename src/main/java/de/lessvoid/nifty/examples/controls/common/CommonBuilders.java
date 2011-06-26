package de.lessvoid.nifty.examples.controls.common;

import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;

/**
 * This is a helper class of reused builders so that we don't repeat ourself too much.
 * @author void
 */
public class CommonBuilders {

  public EffectBuilder createMoveEffect(final String mode, final String direction, final int length) {
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

  public EffectBuilder createFadeEffect() {
    return new EffectBuilder("fade") {{
      effectParameter("start", "#f");
      effectParameter("end", "#0");
      length(300);
      startDelay(300);
      inherit(true);
    }};
  }

  public PanelBuilder vspacer() {
    return new PanelBuilder() {{
      childLayoutHorizontal();
      height("9px");
      width("0px");
    }};
  }

  public PanelBuilder vspacer(final String height) {
    return new PanelBuilder() {{
      childLayoutHorizontal();
      height(height);
      width("100%");
    }};
  }

  public PanelBuilder hspacer(final String width) {
    return new PanelBuilder() {{
      width(width);
      height("0px");
    }};
  }

  public LabelBuilder createLabel(final String text) {
    return createLabel(text, "100px");
  }

  public LabelBuilder createLabel(final String text, final String width) {
    return new LabelBuilder() {{
      text(text);
      width(width);
      alignLeft();
      textVAlignCenter();
      textHAlignLeft();
    }};
  }

  public LabelBuilder createLabel(final String id, final String text, final String width) {
    return new LabelBuilder(id, text) {{
      width(width);
      alignLeft();
      textVAlignCenter();
      textHAlignLeft();
    }};
  }

  public LabelBuilder createShortLabel(final String name, final String width) {
    LabelBuilder result = createShortLabel(name);
    result.width(width);
    return result;
  }

  public LabelBuilder createShortLabel(final String name) {
    return new LabelBuilder() {{
      text(name);
      alignLeft();
      textVAlignCenter();
      textHAlignLeft();
    }};
  }
}
