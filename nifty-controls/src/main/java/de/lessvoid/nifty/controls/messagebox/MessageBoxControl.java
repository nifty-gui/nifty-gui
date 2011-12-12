package de.lessvoid.nifty.controls.messagebox;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.MessageBox;

/**
 * Only used to satisfy the control setup in the XML document. This always uses an actual control.
 * @author ractoc
 *
 */
public class MessageBoxControl extends MessageBox {
	
	public MessageBoxControl() {
	}

	public MessageBoxControl(Nifty nifty, MessageType messageType, String message,
			String buttonCaption, String icon) {
		super(nifty, messageType, message, buttonCaption, icon);
	}

	public MessageBoxControl(Nifty nifty, MessageType messageType, String message,
			String buttonCaption) {
		super(nifty, messageType, message, buttonCaption);
	}

	public MessageBoxControl(Nifty nifty, MessageType messageType, String message,
			String[] buttonCaptions, String icon) {
		super(nifty, messageType, message, buttonCaptions, icon);
	}

	public MessageBoxControl(Nifty nifty, MessageType messageType, String message,
			String[] buttonCaptions) {
		super(nifty, messageType, message, buttonCaptions);
	}
	
}
