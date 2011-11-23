package de.lessvoid.nifty.examples.defaultcontrols.messagebox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlDefinitionBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.messagebox.builder.MessageBoxBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class MessageBoxDialogController implements Controller {
	
	private Nifty nifty;
	private Element element;

	@Override
	public void bind(Nifty nifty, Screen screen, Element element,
			Properties parameter, Attributes controlDefinitionAttributes) {
		this.nifty = nifty;
		this.element = element;
	}

	@Override
	public void init(Properties parameter,
			Attributes controlDefinitionAttributes) {
	}

	@Override
	public void onStartScreen() {
	}

	@Override
	public void onFocus(boolean getFocus) {
	}

	@Override
	public boolean inputEvent(NiftyInputEvent inputEvent) {
		return false;
	}

	public void displayMessageBoxOk() {
		System.out.println("displaying ok message box");
		// create the messagebox with just the ok button
		new ControlDefinitionBuilder(MessageBoxDialogDefinition.NAME + "_OK") {{
			control(new MessageBoxBuilder() {{
				messageBoxType("INFO");
				message("Just showing an info messagebox with one button.");
				buttonCaption("ok");
			}});
		}}.build(nifty, nifty.getCurrentScreen(), element);
		
	}

	public void displayMessageBoxOkCancel() {
		System.out.println("displaying ok/cancel message box");
		// create the messagebox with the ok and cancel button
		new ControlDefinitionBuilder(MessageBoxDialogDefinition.NAME + "_OK") {{
			control(new MessageBoxBuilder() {{
				messageBoxType("INFO");
				message("Just showing an info messagebox with two buttons.");
				buttonCaptions("ok,cancel");
			}});
		}}.buildElementType();
	}

}
