package de.lessvoid.nifty.examples.controls.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;

/**
 * The ListBoxDialogBuilder registers a new control (the whole ListBoxDialog) with
 * Nifty and gives us an appropriate ControlBuilder back we can use to Builder-construct
 * the actual control.
 * 
 * @author void
 */
public class ListBoxDialogBuilder {
  /**
   * We'll register the whole ListBoxDialog as a Nifty control with this name here
   * when the class gets instantiated. The getControlBuilder() method can then be
   * used to dynamically create the Dialog (the control) later.
   */
  private static final String CONTROL_NAME = "listBoxDialogControl";

  /**
   * Just a helper class to assist in Builder-create elements.
   */
  private CommonBuilders builders = new CommonBuilders();

  /**
   * This registers the dialog as a new ControlDefintion with Nifty so that we can
   * later create the dialog dynamically.
   * @param nifty
   */
  public ListBoxDialogBuilder(final Nifty nifty) {
    new ControlDefinitionBuilder(CONTROL_NAME) {{
      controller(new ListBoxDialogController());
      panel(new PanelBuilder() {{
        visible(false);
        childLayoutCenter();
        panel(new PanelBuilder("effectPanel") {{
          style("nifty-panel");
          childLayoutVertical();
          alignCenter();
          valignCenter();
          width("50%");
          height("60%");
          padding("18px,28px,28px,16px");
          onShowEffect(builders.createMoveEffect("in", "left", 500));
          onHideEffect(builders.createMoveEffect("out", "right", 600));
          onHideEffect(builders.createFadeEffect());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("ListBox:"));
            control(new ControlBuilder("listBox", "listBox") {{
              set("displayItems", "4");
              set("vertical", "true");
              set("horizontal", "true");
              width("*");
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("Append:"));
            control(new ControlBuilder("addTextField", "textfield"));
            panel(builders.hspacer("9px"));
            control(new ControlBuilder("appendButton", "button") {{
              set("label", "Append");
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("Multi Selection:"));
            control(new ControlBuilder("multiSelectionCheckBox", "checkbox") {{
              set("checked", "false"); // start with uncheck
            }});
            panel(builders.hspacer("17px"));
            label(builders.createShortLabel("Force Selection:"));
            panel(builders.hspacer("9px"));
            control(new ControlBuilder("forceSelectionCheckBox", "checkbox") {{
              set("checked", "false"); // start with uncheck
            }});
            panel(builders.hspacer("17px"));
            label(builders.createShortLabel("Disable Selection:"));
            panel(builders.hspacer("9px"));
            control(new ControlBuilder("disableSelectionCheckBox", "checkbox") {{
              set("checked", "false"); // start with uncheck
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("Current Sel.:"));
            control(new ControlBuilder("selectionListBox", "listBox") {{
              set("displayItems", "4");
              set("selectionMode", "Disabled");
              set("vertical", "true");
              set("horizontal", "false");
              width("*");
              childLayoutVertical();
            }});
          }});
          panel(builders.vspacer());
          panel(new PanelBuilder() {{
            childLayoutHorizontal();
            label(builders.createLabel("Remove Sel.:"));
            control(new ControlBuilder("removeSelectionButton", "button") {{
              set("label", "Remove Selection From ListBox");
              width("250px");
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
