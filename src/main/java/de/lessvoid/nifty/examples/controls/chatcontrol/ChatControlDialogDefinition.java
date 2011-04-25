package de.lessvoid.nifty.examples.controls.chatcontrol;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.examples.controls.common.DialogPanelControlDefinition;

/**
 * The ChatControlDialogRegister registers a new control (the whole ChatControlDialog) with
 * Nifty. We can later simply generate the whole dialog using a control with the given NAME.
 * 
 * @author void
 */
public class ChatControlDialogDefinition {
  public static String NAME = "chatControlDialogControl";

  public static void register(final Nifty nifty) {
    new ControlDefinitionBuilder(NAME) {{
      controller(new ChatControlDialogController());
      control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
        control(new ControlBuilder("chat-control") {{
        }});
      }});
    }}.registerControlDefintion(nifty);
  }
}
