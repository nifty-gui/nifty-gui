package de.lessvoid.nifty.examples.defaultcontrols.messagebox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.examples.defaultcontrols.common.CommonBuilders;
import de.lessvoid.nifty.examples.defaultcontrols.common.DialogPanelControlDefinition;

public class MessageBoxDialogDefinition {
	public static String NAME = "messageBoxDialogControl";
	private static CommonBuilders builders = new CommonBuilders();

	public static void register(final Nifty nifty) {
		new ControlDefinitionBuilder(NAME) {{
			controller(new MessageBoxDialogController());
			control(new ControlBuilder(DialogPanelControlDefinition.NAME) {{
				panel(new PanelBuilder() {{
					childLayoutHorizontal();
					control(builders.createLabel("MessageBox Ok:"));
					panel(builders.hspacer("10px"));
					control(new ButtonBuilder("okButton", "MessageBoxOk") {{
						interactOnClick("displayMessageBoxOk()");
						label("Show");
					}});
				}});
		        panel(builders.vspacer());
		        panel(builders.vspacer());
		        panel(builders.vspacer());
				panel(new PanelBuilder() {{
					childLayoutHorizontal();
					control(builders.createLabel("MessageBox Ok/Cancel:"));
					panel(builders.hspacer("10px"));
					control(new ButtonBuilder("okCancelButton", "MessageBoxOkCancel") {{
						interactOnClick("displayMessageBoxOkCancel()");
						label("Show");
					}});
				}});
			}});
		}}.registerControlDefintion(nifty);
	  }
}
