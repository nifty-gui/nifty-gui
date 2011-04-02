package de.lessvoid.nifty.examples.controls.textfield;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;
import de.lessvoid.nifty.examples.controls.common.DialogPanelControlDefinition;

/**
 * The TextFieldDialogControlDefinition registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 * @author void
 */
public class TextFieldDialogControlDefinition {
  public static final String NAME = "textFieldDialogControl";
  private static CommonBuilders builders = new CommonBuilders();

  /**
   * This registers the dialog as a new ControlDefintion with Nifty so that we can
   * later create the dialog dynamically.
   * @param nifty
   */
  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new TextFieldDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
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
    }}.registerControlDefintion(nifty);
  }
}
