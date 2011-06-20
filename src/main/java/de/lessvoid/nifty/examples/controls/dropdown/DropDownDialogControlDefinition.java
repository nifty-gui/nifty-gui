package de.lessvoid.nifty.examples.controls.dropdown;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.radiobutton.builder.RadioButtonBuilder;
import de.lessvoid.nifty.controls.radiobutton.builder.RadioGroupBuilder;
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

        // here is the drop down control
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("DropDown:"));
          control(new DropDownBuilder("dropDown") {{
            width("*");
          }});
        }});

        // and the append button to add more items to the drop down
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Append:"));
          control(new ControlBuilder("addDropDownItemText", "textfield"));
          panel(builders.hspacer("9px"));
          control(new ButtonBuilder("addDropDownItemButton", "Append"));
        }});

        // the changed event and the remove item button
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

        // and additionally the index of the selected item
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Current Sel. Idx.:"));
          control(new ControlBuilder("#selectedIndices", "label") {{
            alignLeft();
            textHAlignLeft();
            width("250px");
          }});
        }});

        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          width("100%");
          height("1px");
          backgroundColor("#0008");
        }});
        panel(builders.vspacer());
        control(new RadioGroupBuilder("RadioGroup-1")); // the RadioGroup id is used to link radiobuttons logical together so that only one of them can be active at a certain time
        panel(new PanelBuilder() {{
          control(builders.createLabel("Radio Buttons"));
          childLayoutHorizontal();
          panel(new PanelBuilder() {{
            childLayoutVertical();
            backgroundColor("#8001");
            paddingLeft("7px");
            paddingRight("7px");
            paddingTop("4px");
            paddingBottom("4px");
            width("105px");
            onActiveEffect(new EffectBuilder("border") {{
              effectParameter("color", "#0008");
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Option 1", "60px"));
              control(new RadioButtonBuilder("option-1") {{
                group("RadioGroup-1");
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Option 2", "60px"));
              control(new RadioButtonBuilder("option-2") {{
                group("RadioGroup-1");
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Option 3", "60px"));
              control(new RadioButtonBuilder("option-3") {{
                group("RadioGroup-1");
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Option 4", "60px"));
              control(new RadioButtonBuilder("option-4") {{
                group("RadioGroup-1");
              }});
            }});
          }});
          panel(builders.hspacer("10px"));
          panel(new PanelBuilder() {{
            childLayoutVertical();
            backgroundColor("#8001");
            paddingLeft("7px");
            paddingRight("7px");
            paddingTop("4px");
            paddingBottom("4px");
            onActiveEffect(new EffectBuilder("border") {{
              effectParameter("color", "#0008");
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Option 1 Event:"));
              control(new LabelBuilder("option-1-changed") {{
                width("*");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Option 2 Event:"));
              control(new LabelBuilder("option-2-changed") {{
                width("*");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Option 3 Event:"));
              control(new LabelBuilder("option-3-changed") {{
                width("*");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Option 4 Event:"));
              control(new LabelBuilder("option-4-changed") {{
                width("*");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
          }});
        }});
        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Group Event:"));
          control(new LabelBuilder("RadioGroup-1-changed") {{
            width("*");
            alignLeft();
            textVAlignCenter();
            textHAlignLeft();
          }});
        }});
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Allow Deselect:"));
          control(new CheckboxBuilder("radioGroupAllowDeselection") {{
            checked(false);
          }});
        }});
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Disable:"));
          control(new CheckboxBuilder("radioGroupDisable") {{
            checked(false);
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
