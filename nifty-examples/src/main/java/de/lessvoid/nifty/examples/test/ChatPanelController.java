package de.lessvoid.nifty.examples.test;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class ChatPanelController implements Controller, KeyInputHandler {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private TextFieldControl chatsend;
  public static Element chatField;

  public void bind(final Nifty niftyParam, final Screen screenParam,
      final Element newElement, final Properties properties,
      final Attributes controlDefinitionAttributes) {

    nifty = niftyParam;
    screen = screenParam;
    element = newElement;
    chatField = screen.findElementByName("chatfield");

    System.out.println("Setup chat field:" + chatField.getId());
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  public void onStartScreen() {

    chatsend = screen.findControl("chatsend", TextFieldControl.class);
    
    screen.addKeyboardInputHandler(new DefaultInputMapping(), this);

  }

  public void onFocus(final boolean getFocus) {
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    System.out.println("woah");
    return true;
  }

  public void removePanel() {
    nifty.removeElement(screen, element);
  }

  @Override
  public boolean keyEvent(NiftyInputEvent arg0) {
    System.out.println("keyEvent ChatPanelController");

    if (arg0 == NiftyStandardInputEvent.Activate) {
      String message = chatsend.getText();
      if (message.length() >= 1000) {
        return false;
      }
      chatsend.setText("");
      return true;
    }
    System.out.println("keykey");
    return false;

  }

}