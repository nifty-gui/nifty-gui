package de.lessvoid.nifty.examples.controls.tabs;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.controls.chatcontrol.builder.ChatBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabsBuilder;
import de.lessvoid.nifty.examples.controls.common.DialogPanelControlDefinition;

/**
 * The ChatControlDialogRegister registers a new control (the whole ChatControlDialog) with
 * Nifty. We can later simply generate the whole dialog using a control with the given NAME.
 * 
 * @author void
 */
public class TabsControlDialogDefinition {
  public static String NAME = "tabsControlDialogControl";

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new TabsControlDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        control(new TabsBuilder("tabs") {{
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
