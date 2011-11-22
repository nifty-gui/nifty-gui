package de.lessvoid.nifty.controls;

public interface MessageBox extends NiftyControl {
	
	void setIcon(String icon);
	
	void setMessage(String message);

	void setButtonCaption(String buttonCaption);

	void setButtonCaptions(String buttonCaptions);
	
	void setButtonCaptions(String[] buttonCaptions);
	
	void setMessageType(String messageType);
	
	void setMessageType(MessageType messageType);
	
	enum MessageType {
		CUSTOM,
		INFO,
		WARNING,
		ERROR
	}

}
