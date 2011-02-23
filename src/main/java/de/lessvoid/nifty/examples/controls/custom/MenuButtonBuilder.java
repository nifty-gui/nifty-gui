package de.lessvoid.nifty.examples.controls.custom;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;

public class MenuButtonBuilder {
  private static final String CONTROL_NAME = "menuButtonControl";
  private static final String PARAM_LABEL = "menuButtonLabel";

  public MenuButtonBuilder(final Nifty nifty) {
    new ControlDefinitionBuilder(CONTROL_NAME) {{
      controller(new MenuButtonController());
      panel(new PanelBuilder() {{
        backgroundColor("#800a");
        width("123px");
        alignCenter();
        valignCenter();
        childLayoutCenter();
        focusable(true);
        visibleToMouse();
        onActiveEffect(new EffectBuilder("border") {{
          parameter("color", "#112f");
        }});
        onHoverEffect(new HoverEffectBuilder("changeMouseCursor") {{
          parameter("id", "hand");
        }});
        onHoverEffect(new HoverEffectBuilder("border") {{
          parameter("color", "#800f");
        }});
        onHoverEffect(new HoverEffectBuilder("gradient") {{
          effectValue("offset", "0%", "color", "#222f");
          effectValue("offset", "100%", "color", "#f77f");
        }});
        onFocusEffect(new EffectBuilder("gradient") {{
          effectValue("offset", "0%", "color", "#222f");
          effectValue("offset", "100%", "color", "#feef");
        }});
        onCustomEffect(new EffectBuilder("gradient") {{
          parameter("customKey", "selected");
          parameter("timeType", "infinite");
          parameter("neverStopRendering", "true");
          effectValue("offset", "0%", "color", "#222f");
          effectValue("offset", "100%", "color", "#feef");
        }});
        control(new LabelBuilder() {{
          color("#000f");
          text(controlParameter(PARAM_LABEL));
          alignCenter();
          valignCenter();
          onCustomEffect(new EffectBuilder("textColor") {{
            parameter("color", "#eeef");
            parameter("customKey", "selected");
            parameter("timeType", "infinite");
            parameter("neverStopRendering", "true");
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }

  public ControlBuilder getControlBuilder(final String id, final String text) {
    return new ControlBuilder(id, CONTROL_NAME) {{
      parameter(PARAM_LABEL, text);
    }};
  }
}
