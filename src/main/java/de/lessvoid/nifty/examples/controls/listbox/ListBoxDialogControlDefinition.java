package de.lessvoid.nifty.examples.controls.listbox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.examples.controls.common.CommonBuilders;
import de.lessvoid.nifty.examples.controls.common.DialogPanelControlDefinition;

/**
 * The ListBoxDialogControlDefinition registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 * @author void
 */
public class ListBoxDialogControlDefinition {
  public static final String NAME = "listBoxDialogControl";
  private static CommonBuilders builders = new CommonBuilders();

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new ListBoxDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{

        // the actual list box panel at the top
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("ListBox:"));
          control(new ListBoxBuilder("listBox") {{
            displayItems(4);
            selectionModeSingle();
            showVerticalScrollbar();
            showHorizontalScrollbar();
            width("*");
          }});
        }});

        // the panel for the append button
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Append:"));
          control(new ControlBuilder("addTextField", "textfield"));
          panel(builders.hspacer("9px"));
          control(new ButtonBuilder("appendButton", "Append"));
        }});

        // the panel for the checkbox to select multi selection and stuff
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Multi Selection:"));
          control(new CheckboxBuilder("multiSelectionCheckBox") {{
            checked(false);
          }});
          panel(builders.hspacer("17px"));
          control(builders.createShortLabel("Force Selection:"));
          panel(builders.hspacer("9px"));
          control(new CheckboxBuilder("forceSelectionCheckBox") {{
            checked(false);
          }});
          panel(builders.hspacer("17px"));
          control(builders.createShortLabel("Disable Selection:"));
          panel(builders.hspacer("9px"));
          control(new CheckboxBuilder("disableSelectionCheckBox") {{
            checked(false);
          }});
        }});

        // another listbox to display the current selection
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Current Sel.:"));
          control(new ListBoxBuilder("selectionListBox") {{
            displayItems(4);
            selectionModeDisabled();
            hideHorizontalScrollbar();
            hideVerticalScrollbar();
            width("*");
            childLayoutVertical();
            optionalHorizontalScrollbar();
            optionalVerticalScrollbar();
          }});
        }});

        // a simple label to display the new item index feature for selections
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Current Sel. Idx.:"));
          control(new ControlBuilder("selectedIndices", "label") {{
            alignLeft();
            textHAlignLeft();
            width("250px");
          }});
        }});

        // the remove selection button
        panel(builders.vspacer());
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Remove Sel.:"));
          control(new ControlBuilder("removeSelectionButton", "button") {{
            set("label", "Remove Selection From ListBox");
            width("250px");
          }});
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
