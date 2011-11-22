package de.lessvoid.nifty.controls.messagebox.builder;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.controls.MessageBox;

public class MessageBoxBuilder extends ControlBuilder {

	public MessageBoxBuilder() {
		super("nifty-messagebox");
	}

	public MessageBoxBuilder(String id) {
		super("nifty-messagebox", id);
	}
	
	public void setMessageBoxType(String type) {
		set("messageBoxType", type);
	}
	
	public void setMessage(String message) {
		set("message", message);
	}

	public void setIcon(String icon) {
		set("icon", icon);
	}
	
	public void setButtonCaption(String buttonCaption) {
		set("buttonCaption", buttonCaption);
	}
	
	public void setButtonCaptions(String buttonCaptions) {
		set("buttonCaptions", buttonCaptions);
	}
	
}
