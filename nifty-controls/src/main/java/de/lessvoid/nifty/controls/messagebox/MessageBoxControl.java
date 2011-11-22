package de.lessvoid.nifty.controls.messagebox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.MessageBox;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class MessageBoxControl extends AbstractController implements MessageBox {
	
	private String[] buttonCaptions;
	
	private MessageType messageType = MessageType.INFO;
	
	private NiftyImage icon;
	
	private String message;
	
	private Nifty nifty;
	private Screen screen;
	private Element element;
	
	@Override
	public void bind(Nifty nifty, Screen screen, Element element,
			Properties parameter, Attributes controlDefinitionAttributes) {
		if (controlDefinitionAttributes.isSet("buttonCaptions")) {
			setButtonCaptions(controlDefinitionAttributes.get("buttonCaptions").split(","));
		}
		this.nifty = nifty;
		this.screen = screen;
		this.element = element;
		
		if (messageType != MessageType.CUSTOM) {
			setIcon("/messagebox/" + messageType.name() + ".jpg");
		}
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

	@Override
	public void setIcon(String icon) {
		this.icon = nifty.createImage(icon, false);
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void setButtonCaption(String buttonCaption) {
		this.buttonCaptions = new String[]{buttonCaption};
	}

	@Override
	public void setButtonCaptions(String[] buttonCaptions) {
		this.buttonCaptions = buttonCaptions;
	}

	@Override
	public void setButtonCaptions(String buttonCaptions) {
		this.buttonCaptions = buttonCaptions.split(",");
	}

	@Override
	public void setMessageType(String messageType) {
		this.messageType = MessageType.valueOf(messageType);
	}

	@Override
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	private void setupMessageBox() {
		final Element imgIcon = element.findElementByName("");
		final ImageRenderer iconRenderer = imgIcon.getRenderer(ImageRenderer.class);
		iconRenderer.setImage(icon);
        final Element text = element.findElementByName("");
        final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
        textRenderer.setText(message);
        
	}

}
