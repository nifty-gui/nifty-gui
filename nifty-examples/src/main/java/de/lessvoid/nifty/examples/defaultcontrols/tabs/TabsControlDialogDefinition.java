package de.lessvoid.nifty.examples.defaultcontrols.tabs;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabGroupBuilder;
import de.lessvoid.nifty.examples.defaultcontrols.common.DialogPanelControlDefinition;

/**
 * The ChatControlDialogRegister registers a new control (the whole ChatControlDialog) with Nifty. We can later simply
 * generate the whole dialog using a control with the given NAME.
 *
 * @author void
 */
public class TabsControlDialogDefinition {
  public static String NAME = "tabsControlDialogControl";

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new TabsControlDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        control(new TabGroupBuilder("tabs") {{
          height("100%");
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
