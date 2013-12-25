package de.lessvoid.nifty.examples.defaultcontrols.treebox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.treebox.builder.TreeBoxBuilder;
import de.lessvoid.nifty.examples.defaultcontrols.common.CommonBuilders;
import de.lessvoid.nifty.examples.defaultcontrols.common.DialogPanelControlDefinition;

import javax.annotation.Nonnull;

/**
 * The ChatControlDialogRegister registers a new control (the whole
 * ChatControlDialog) with Nifty. We can later simply generate the whole dialog
 * using a control with the given NAME.
 *
 * @author void
 */
public class TreeBoxControlDialogDefinition {
  @Nonnull
  public static final String NAME = "treeboxControlDialogControl";
  @Nonnull
  private static final CommonBuilders builders = new CommonBuilders();

  public static void register(@Nonnull final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new TreeboxControlDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        panel(new PanelBuilder() {{
          childLayoutVertical();
          control(builders.createLabel("TreeBox:"));
          control(new TreeBoxBuilder("tree-box") {{
            displayItems(4);
            selectionModeSingle();
            showVerticalScrollbar();
            showHorizontalScrollbar();
            width("*");
          }});
        }});
        panel(new PanelBuilder() {{
          childLayoutHorizontal();
          control(builders.createLabel("Selected:"));
          control(new ControlBuilder("selectedItemText", "textfield"));
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
