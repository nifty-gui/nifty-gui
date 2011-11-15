package de.lessvoid.nifty.examples.defaultcontrols.treebox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.controls.treebox.builder.TreeBoxBuilder;
import de.lessvoid.nifty.examples.defaultcontrols.common.DialogPanelControlDefinition;

/**
 * The ChatControlDialogRegister registers a new control (the whole ChatControlDialog) with
 * Nifty. We can later simply generate the whole dialog using a control with the given NAME.
 * 
 * @author void
 */
public class TreeBoxControlDialogDefinition {
  public static String NAME = "treeboxControlDialogControl";

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new TreeboxControlDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        control(new TreeBoxBuilder("tree-box") {{
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
