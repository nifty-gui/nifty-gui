package de.lessvoid.nifty.examples.controls.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
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
        panel(new PanelBuilder("#effectPanel") {{
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
            control(builders.createLabel("Textfield:"));
            control(new TextFieldBuilder("mainTextField") {{
              width("*");
            }});
          }});
          panel(builders.vspacer());
          panel(builders.vspacer());
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            control(builders.createLabel("Password Mode:"));
            control(new ControlBuilder("passwordCharCheckBox", "checkbox") {{
              set("checked", "false"); // start with uncheck
            }});
            panel(builders.hspacer("20px"));
            control(builders.createShortLabel("Char:", "40px"));
            panel(builders.hspacer("10px"));
            control(new TextFieldBuilder("passwordCharTextField", "*") {{
              maxLength(1);
              width("20px");
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            control(builders.createLabel("Enable Length:"));
            control(new ControlBuilder("maxLengthEnableCheckBox", "checkbox") {{
              set("checked", "false");
            }});
            panel(builders.hspacer("20px"));
            control(builders.createShortLabel("Max:", "40px"));
            panel(builders.hspacer("10px"));
            control(new TextFieldBuilder("maxLengthTextField") {{
              width("50px");
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            control(builders.createLabel("Changed Event:"));
            control(new LabelBuilder("textChangedLabel") {{
              width("*");
              alignLeft();
              textVAlignCenter();
              textHAlignLeft();
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            control(builders.createLabel("Key Event:"));
            control(new LabelBuilder("keyEventLabel") {{
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
