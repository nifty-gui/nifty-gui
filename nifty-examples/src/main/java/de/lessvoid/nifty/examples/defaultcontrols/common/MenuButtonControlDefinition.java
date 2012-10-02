package de.lessvoid.nifty.examples.defaultcontrols.common;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;

public class MenuButtonControlDefinition {
  private static final String NAME = "menuButtonControl";
  private static final String PARAMETER_LABEL = "menuButtonLabel";
  private static final String PARAMETER_HINT = "menuButtonHint";
  private static final String PARAMETER_COLOR_0 = "menuButtonColor0";
  private static final String PARAMETER_COLOR_1 = "menuButtonColor1";
  private static final String PARAMETER_COLOR_2 = "menuButtonColor2";
  private static final String PARAMETER_COLOR_3 = "menuButtonColor3";

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new MenuButtonController());
      panel(new PanelBuilder() {{
        backgroundColor(controlParameter(PARAMETER_COLOR_0));
        width("110px");
        alignCenter();
        valignCenter();
        childLayoutCenter();
        focusable(true);
        visibleToMouse();
        onActiveEffect(new EffectBuilder("border") {{
          effectParameter("color", "#112f");
        }});
        onHoverEffect(new HoverEffectBuilder("changeMouseCursor") {{
          effectParameter("id", "hand");
        }});
        onHoverEffect(new HoverEffectBuilder("border") {{
          effectParameter("color", "#800f");
        }});
        onHoverEffect(new HoverEffectBuilder("gradient") {{
          effectValue("offset", "0%", "color", controlParameter(PARAMETER_COLOR_1));
          effectValue("offset", "100%", "color", controlParameter(PARAMETER_COLOR_3));
        }});
        onFocusEffect(new EffectBuilder("gradient") {{
          effectValue("offset", "0%", "color", controlParameter(PARAMETER_COLOR_1));
          effectValue("offset", "100%", "color", controlParameter(PARAMETER_COLOR_2));
        }});
        onCustomEffect(new EffectBuilder("gradient") {{
          effectParameter("customKey", "selected");
          effectParameter("timeType", "infinite");
          effectParameter("neverStopRendering", "true");
          effectValue("offset", "0%", "color", controlParameter(PARAMETER_COLOR_1));
          effectValue("offset", "100%", "color", controlParameter(PARAMETER_COLOR_2));
        }});
        onHoverEffect(new HoverEffectBuilder("hint") {{
          effectParameter("hintText", controlParameter(PARAMETER_HINT));
          effectParameter("hintStyle", "special-hint");
          effectParameter("hintDelay", "750");
          effectParameter("offsetX", "center");
          effectParameter("offsetY", "50");
        }});
        control(new LabelBuilder() {{
          color("#000f");
          text(controlParameter(PARAMETER_LABEL));
          alignCenter();
          valignCenter();
          onCustomEffect(new EffectBuilder("textColor") {{
            effectParameter("color", "#eeef");
            effectParameter("customKey", "selected");
            effectParameter("timeType", "infinite");
            effectParameter("neverStopRendering", "true");
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }

  public static ControlBuilder getControlBuilder(final String id, final String text, final String hintText) {
    return new ControlBuilder(id, NAME) {{
      parameter(PARAMETER_LABEL, text);
      parameter(PARAMETER_HINT, hintText);
      parameter(PARAMETER_COLOR_0, "#800a");
      parameter(PARAMETER_COLOR_1, "#222f");
      parameter(PARAMETER_COLOR_2, "#feef");
      parameter(PARAMETER_COLOR_3, "#f77f");
    }};
  }

  public static ControlBuilder getControlBuilderUser(final String id, final String text, final String hintText) {
    return new ControlBuilder(id, NAME) {{
      parameter(PARAMETER_LABEL, text);
      parameter(PARAMETER_HINT, hintText);
      parameter(PARAMETER_COLOR_0, "#44fa");
      parameter(PARAMETER_COLOR_1, "#222f");
      parameter(PARAMETER_COLOR_2, "#eeff");
      parameter(PARAMETER_COLOR_3, "#eeff");
    }};
  }

  public static ControlBuilder getControlBuilderSpecial(final String id, final String text, final String hintText) {
    return new ControlBuilder(id, NAME) {{
      parameter(PARAMETER_LABEL, text);
      parameter(PARAMETER_HINT, hintText);
      parameter(PARAMETER_COLOR_0, "#2c2a");
      parameter(PARAMETER_COLOR_1, "#222f");
      parameter(PARAMETER_COLOR_2, "#efef");
      parameter(PARAMETER_COLOR_3, "#efef");
    }};
  }

  public static ControlBuilder getControlBuilder(final String id, final String text, final String hintText, final String width) {
    return new ControlBuilder(id, NAME) {{
      parameter(PARAMETER_LABEL, text);
      parameter(PARAMETER_HINT, hintText);
      parameter(PARAMETER_COLOR_0, "#800a");
      parameter(PARAMETER_COLOR_1, "#222f");
      parameter(PARAMETER_COLOR_2, "#feef");
      parameter(PARAMETER_COLOR_3, "#f77f");
      width(width);
    }};
  }
}
