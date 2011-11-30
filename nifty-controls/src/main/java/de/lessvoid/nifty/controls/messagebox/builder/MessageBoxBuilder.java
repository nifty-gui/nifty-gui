package de.lessvoid.nifty.controls.messagebox.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class MessageBoxBuilder extends ControlBuilder {

	public MessageBoxBuilder() {
		super("nifty-messagebox");
	}

	public MessageBoxBuilder(String id) {
		super("nifty-messagebox", id);
	}
	
	public void messageBoxType(String type) {
		set("messageBoxType", type);
	}
	
	public void message(String message) {
		set("message", message);
	}

	public void icon(String icon) {
		if (icon != null) {
			set("icon", icon);
		}
	}
	
	public void buttonCaption(String buttonCaption) {
		set("buttonCaption", buttonCaption);
	}
	
	public void buttonCaptions(String buttonCaptions) {
		set("buttonCaptions", buttonCaptions);
	}
	
}
