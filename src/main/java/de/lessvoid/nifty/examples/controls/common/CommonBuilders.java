package de.lessvoid.nifty.examples.controls.common;

import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.LabelBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;

/**
 * This is a helper class of reused builders so that we don't repeat ourself too much.
 * @author void
 */
public class CommonBuilders {

  public EffectBuilder createMoveEffect(final String mode, final String direction, final int length) {
    return new EffectBuilder("move") {{
      parameter("mode", mode);
      parameter("direction", direction);
      parameter("timeType", "exp");
      parameter("factor", "3.5");
      length(length);
      startDelay(0);
      inherit(true);
    }};
  }

  public EffectBuilder createFadeEffect() {
    return new EffectBuilder("fade") {{
      parameter("start", "#f");
      parameter("end", "#0");
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

  public PanelBuilder hspacer(final String width) {
    return new PanelBuilder() {{
      width(width);
      height("0px");
    }};
  }

  public LabelBuilder createLabel(final String text) {
    return new LabelBuilder() {{
      text(text);
      width("100px");
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
