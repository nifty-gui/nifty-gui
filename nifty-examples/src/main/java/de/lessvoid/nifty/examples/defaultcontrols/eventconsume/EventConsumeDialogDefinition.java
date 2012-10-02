package de.lessvoid.nifty.examples.defaultcontrols.eventconsume;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.examples.defaultcontrols.common.CommonBuilders;
import de.lessvoid.nifty.examples.defaultcontrols.common.DialogPanelControlDefinition;

/**
 * The EventConsumeDialogDefinition registers a new control (the whole ChatControlDialog) with Nifty.
 *
 * @author void
 */
public class EventConsumeDialogDefinition {
  public static String NAME = "eventConsumeDialogControl";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new EventConsumeDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          height("150px");
          panel(new PanelBuilder() {{
            width("*");
            height("100%");
            childLayoutOverlay();
            panel(new PanelBuilder("eventConsumeLeftPanel") {{
              width("100%");
              height("100%");
              backgroundColor("#080a");
              visibleToMouse();
              childLayoutCenter();
              control(new LabelBuilder("eventConsumeLeftLabel", "visibleToMouse = true") {{
                textVAlignCenter();
                textHAlignCenter();
              }});
            }});
            panel(new PanelBuilder() {{
              width("100%");
              height("100%");
              childLayoutVertical();
              panel(new PanelBuilder() {{
                height("25%");
                valignBottom();
                alignCenter();
                childLayoutCenter();
                control(new ButtonBuilder("eventConsumeLeftButton", "Test Left"));
              }});
            }});
          }});
          panel(builders.hspacer("10px"));
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            height("150px");
            panel(new PanelBuilder() {{
              width("*");
              height("100%");
              childLayoutOverlay();
              panel(new PanelBuilder("eventConsumeRightPanel") {{
                width("100%");
                height("100%");
                backgroundColor("#800a");
                childLayoutCenter();
                control(new LabelBuilder("eventConsumeRightLabel", "visibleToMouse = false") {{
                  textVAlignCenter();
                  textHAlignCenter();
                }});
              }});
              panel(new PanelBuilder() {{
                width("100%");
                height("100%");
                childLayoutVertical();
                panel(new PanelBuilder() {{
                  height("25%");
                  valignBottom();
                  alignCenter();
                  childLayoutCenter();
                  control(new ButtonBuilder("eventConsumeRightButton", "Test Right"));
                }});
              }});
            }});
          }});
        }});
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          // left row
          panel(new PanelBuilder() {{
            childLayoutVertical();
            width("*");
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Mouse X:"));
              control(new LabelBuilder("mouseXText") {{
                width("120px");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Mouse Wheel:"));
              control(new LabelBuilder("mouseWheelText") {{
                width("120px");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Button:"));
              control(new LabelBuilder("mouseButtonText") {{
                width("120px");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Processed:"));
              control(new LabelBuilder("mouseProcessedText") {{
                width("120px");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
          }});
          panel(builders.hspacer("10px"));
          // right row
          panel(new PanelBuilder() {{
            childLayoutVertical();
            width("*");
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Mouse Y:"));
              control(new LabelBuilder("mouseYText") {{
                width("120px");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel(""));
              control(new LabelBuilder() {{
                width("120px");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("Button Down:"));
              control(new LabelBuilder("mouseDownText") {{
                width("120px");
                alignLeft();
                textVAlignCenter();
                textHAlignLeft();
              }});
            }});
            panel(new PanelBuilder() {{
              childLayoutHorizontal();
              control(builders.createLabel("eventConsumeButtonOut", "", "200px"));
            }});
          }});
        }});
        panel(builders.vspacer());
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Element:"));
          control(new DropDownBuilder("eventConsumeElementDropDown") {{
            width("*");
          }});
        }});
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Ignore Mouse:"));
          control(new CheckboxBuilder("eventConsumeIgnoreMouseEventsCheckBox") {{
            checked(false);
          }});
        }});
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Ignore Keyboard:"));
          control(new CheckboxBuilder("eventConsumeIgnoreKeyboardEventsCheckBox") {{
            checked(false);
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
