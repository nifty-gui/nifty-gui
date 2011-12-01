package de.lessvoid.nifty.controls;

import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.messagebox.builder.MessageBoxBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class MessageBox extends AbstractController {

	private String[] buttonCaptions;

	private MessageType messageType = MessageType.INFO;

	private NiftyImage icon;

	private String message;
	
	private String buttonWidth = "100px";
	private String buttonHeight = "25px";

	private Nifty nifty;
	private Element element;
	
	private Element messageboxPopup;

	public MessageBox(Nifty nifty, final MessageType messageType, final String message,
			final String buttonCaption, final String icon) {
		this.nifty = nifty;
		messageboxPopup = nifty.createPopup("niftyPopupMessageBox");

		setMessageType(messageType);
		setMessage(message);
		setButtonCaption(buttonCaption);
		setIcon(icon);
		setupMessageBox();
		
		
//		new MessageBoxBuilder("MessageBox_" + messageType.name()) {{
//			icon(iconParam);
//			message(message);
//			messageBoxType(messageType.name());
//			buttonCaption(buttonCaption);
//		}}.build(nifty, nifty.getCurrentScreen(), messageboxPopup);
	}

	public MessageBox(Nifty nifty, MessageType messageType, String message,
			String buttonCaption) {
		this(nifty, messageType, message, buttonCaption, null);
	}

	public MessageBox(Nifty nifty, final MessageType messageType, final String message,
			final String[] buttonCaptions, final String icon) {
		this.nifty = nifty;
		messageboxPopup = nifty.createPopup("niftyPopupMessageBox");
		
		setMessageType(messageType);
		setMessage(message);
		setButtonCaptions(buttonCaptions);
		setIcon(icon);
		setupMessageBox();
		
//		String captionsTemp = "";
//		for (String buttonCaption : buttonCaptions) {
//			if (captionsTemp.length() > 0) {
//				captionsTemp = captionsTemp + ",";
//			}
//			captionsTemp = captionsTemp + buttonCaption;
//		}
//		final String captions = captionsTemp;
//		
//		new MessageBoxBuilder("MessageBox_" + messageType.name()) {{
//			icon(icon);
//			message(message);
//			messageBoxType(messageType.name());
//			buttonCaptions(captions);
//		}}.build(nifty, nifty.getCurrentScreen(), messageboxPopup);
	}

	public MessageBox(Nifty nifty, MessageType messageType, String message,
			String[] buttonCaptions) {
		this(nifty, messageType, message, buttonCaptions, null);
	}

	@Override
	public void bind(Nifty nifty, Screen screen, Element element,
			Properties parameter, Attributes controlDefinitionAttributes) {
		System.out.println("binding MessageBox");
		if (controlDefinitionAttributes.isSet("buttonCaptions")) {
			setButtonCaptions(controlDefinitionAttributes.get("buttonCaptions")
					.split(","));
		} else if (controlDefinitionAttributes.isSet("buttonCaption")) {
			setButtonCaption(controlDefinitionAttributes.get("buttonCaption"));
		}
		
		if (messageType != MessageType.CUSTOM) {
			setIcon("/messagebox/" + messageType.name() + ".jpg");
		}
		setupMessageBox();
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean inputEvent(NiftyInputEvent inputEvent) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void show() {
		nifty.showPopup(nifty.getCurrentScreen(), messageboxPopup.getId(), null);
	}
	
	public void hide(String command) {
		closeMessageBox(command);
		nifty.closePopup(messageboxPopup.getId());
	}

	public void setIcon(String icon) {
		if (icon != null) {
			this.icon = nifty.createImage(icon, false);
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setButtonCaption(String buttonCaption) {
		this.buttonCaptions = new String[] { buttonCaption };
	}

	public void setButtonCaptions(String[] buttonCaptions) {
		this.buttonCaptions = buttonCaptions;
	}

	public void setButtonCaptions(String buttonCaptions) {
		System.out.println("setting button captions");
		this.buttonCaptions = buttonCaptions.split(",");
	}

	public void setMessageType(String messageType) {
		this.messageType = MessageType.valueOf(messageType);
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	private void setupMessageBox() {
		final Element imgIcon = messageboxPopup.findElementByName("#messagebox").findElementByName("#message-icon");
		final ImageRenderer iconRenderer = imgIcon
				.getRenderer(ImageRenderer.class);
		iconRenderer.setImage(icon);
		final Element text = messageboxPopup.findElementByName("#messagebox").findElementByName("#message-text");
		final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
		textRenderer.setText(message);
		
		int i = 0;
		for (String buttonCaption : buttonCaptions) {
			i++;
			createButton(buttonCaption, buttonCaption, "button_" + i);
		}
		
		messageboxPopup.findElementByName("#messagebox").layoutElements();
		
	}
	
	private void closeMessageBox(String command) {
		clearButtons();
		messageboxPopup.findElementByName("#messagebox").findElementByName("#buttons");
	}
	
	private void createButton(final String buttonCaption, final String command, final String buttonId) {
        Element buttonPanel = messageboxPopup.findElementByName("#messagebox").findElementByName("#buttons");
        if (buttonPanel.findElementByName("#" + buttonId) == null) {
            new ButtonBuilder("#" + buttonId) {

                {
                    style("nifty-button");
                    childLayout(ChildLayoutType.Horizontal);
                    interactOnClick("close(" + command + ")");
                    if (buttonWidth != null) {
                        width(buttonWidth);
                    }
                    if (buttonHeight != null) {
                        height(percentage(100));
                    } else {
                        height("25px");
                    }
                    label(buttonCaption);
                }
            }.build(nifty, nifty.getCurrentScreen(), buttonPanel);
        }
    }
	
	private void clearButtons() {
		List<Element> buttons = messageboxPopup.findElementByName("#messagebox").findElementByName("#buttons").getElements();
		for (Element button : buttons) {
			button.markForRemoval();
		}
	}
	
	protected Element getMessageBoxPopup() {
		return messageboxPopup;
	}

	public enum MessageType {
		CUSTOM, INFO, WARNING, ERROR
	}
	
}
