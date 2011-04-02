package de.lessvoid.nifty.examples.controls.dropdown;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;
import de.lessvoid.nifty.examples.controls.common.DialogPanelControlDefinition;

/**
 * The DropDownDialogControlDefinition registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 * @author void
 */
public class DropDownDialogControlDefinition {
  public static final String NAME = "dropDownDialogControl";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new DropDownDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("DropDown:"));
          control(new DropDownBuilder("dropDown") {{
            width("*");
          }});
        }});
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Append:"));
          control(new ControlBuilder("addDropDownItemText", "textfield"));
          panel(builders.hspacer("9px"));
          control(new ButtonBuilder("addDropDownItemButton", "Append"));
        }});
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Changed Event:"));
          control(new LabelBuilder("selectedItem") {{
            width("*");
            alignLeft();
            textVAlignCenter();
            textHAlignLeft();
          }});
          panel(builders.hspacer("9px"));
          control(new ButtonBuilder("removeDropDownItemButton", "Remove"));
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
