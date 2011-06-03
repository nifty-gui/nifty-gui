package de.lessvoid.nifty.examples.controls.chatcontrol;

import java.util.Properties;
import java.util.Random;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The ChatControlDialogController registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 * @author void
 */
public class ChatControlDialogController implements Controller {
  private Chat chat;
  private NiftyImage chatIconNiftyUser;
  private NiftyImage chatIconVoid;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    this.chat = screen.findNiftyControl("chat", Chat.class);
    this.chatIconVoid = nifty.createImage("chatcontrol/chat-icon-ninja.png", false);
    this.chatIconNiftyUser = nifty.createImage("chatcontrol/chat-icon-user.png", false);
    chat.addPlayer("void", chatIconVoid);
    chat.addPlayer("nifty-user", chatIconNiftyUser);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @NiftyEventSubscriber(id="chat")
  public void onChatTextSendEvent(final String id, final ChatTextSendEvent event) {
    // You should post that text to the server ... 

    // here we simply post it to the chat window
    chat.receivedChatLine(event.getText(), chatIconNiftyUser);

    // and we generate a random reaction from the void player =)
    chat.receivedChatLine(getRandomResponse(), chatIconVoid);
  }

  private String getRandomResponse() {
      // and we generate a random response from void ;-)
      String[] responses = {
          "Hi there :)",
          "LOL",
          "Nifty GUI ftw \\o/",
          "Really?",
          "How awesome is this?",
          "I need a vacation ;-)",
          "Yeah, that rocks!",
          "wtf? o_O",
          "Are you sure about that?",
          "#winning xD",
          "shizzle?"
      };
      Random random = new Random();
      return responses[random.nextInt(responses.length)];
  }
}
