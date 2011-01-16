package de.lessvoid.nifty.examples.controls.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.LabelBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;

/**
 * The TextFieldDialogBuilder registers a new control (the whole TextFieldDialog) with
 * Nifty and gives us an appropriate ControlBuilder back we can use to Builder-construct
 * the actual control.
 * 
 * @author void
 */
public class TextFieldDialogBuilder {
  private static final String CONTROL_NAME = "textFieldDialogControl";
  private CommonBuilders builders = new CommonBuilders();

  /**
   * This registers the dialog as a new ControlDefintion with Nifty so that we can
   * later create the dialog dynamically.
   * @param nifty
   */
  public TextFieldDialogBuilder(final Nifty nifty) {
    new ControlDefinitionBuilder(CONTROL_NAME) {{
      controller(new TextFieldDialogController());
      panel(new PanelBuilder() {{
        visible(false);
        childLayoutCenter();
        panel(new PanelBuilder("effectPanel") {{
          style("nifty-panel");
          childLayoutVertical();
          padding("18px,28px,28px,16px");
          width("55%");
          height("58%");
          alignCenter();
          valignCenter();
          onShowEffect(builders.createMoveEffect("in", "right", 500));
          onHideEffect(builders.createMoveEffect("out", "left", 600));
          onHideEffect(builders.createFadeEffect());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("Textfield:"));
            control(new ControlBuilder("mainTextField", "textfield") {{
              width("*");
            }});
          }});
          panel(builders.vspacer());
          panel(builders.vspacer());
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("Password Mode:"));
            control(new ControlBuilder("passwordCharCheckBox", "checkbox") {{
              set("checked", "false"); // start with uncheck
            }});
            panel(builders.hspacer("20px"));
            label(builders.createShortLabel("Char:", "40px"));
            panel(builders.hspacer("10px"));
            control(new ControlBuilder("passwordCharTextField", "textfield") {{
              set("maxLength", "1");
              set("text", "*");
              width("20px");
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("Enable Length:"));
            control(new ControlBuilder("maxLengthEnableCheckBox", "checkbox") {{
              set("checked", "false");
            }});
            panel(builders.hspacer("20px"));
            label(builders.createShortLabel("Max:", "40px"));
            panel(builders.hspacer("10px"));
            control(new ControlBuilder("maxLengthTextField", "textfield") {{
              width("50px");
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("Changed Event:"));
            label(new LabelBuilder("textChangedLabel") {{
              text("---");
              width("120px");
              alignLeft();
              textVAlignCenter();
              textHAlignLeft();
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("Key Event:"));
            label(new LabelBuilder("keyEventLabel") {{
              text("---");
              width("120px");
              alignLeft();
              textVAlignCenter();
              textHAlignLeft();
            }});
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }

  public static ControlBuilder getControlBuilder(final String id) {
    return new ControlBuilder(id, CONTROL_NAME);
  }
}
