package de.lessvoid.nifty.examples.defaultcontrols.chatcontrol;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * The ChatControlDialogController registers a new control with Nifty
 * that represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 *
 * @author void
 */
public class ChatControlDialogController implements Controller {
  @Nullable
  private Chat chat;
  @Nullable
  private NiftyImage chatIconNiftyUser;
  @Nullable
  private NiftyImage chatIconVoid;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    this.chat = screen.findNiftyControl("chat", Chat.class);
    this.chatIconVoid = nifty.createImage("defaultcontrols/chatcontrol/chat-icon-ninja.png", false);
    this.chatIconNiftyUser = nifty.createImage("defaultcontrols/chatcontrol/chat-icon-user.png", false);
    if (chat != null) {
      chat.addPlayer("void", chatIconVoid);
      chat.addPlayer("nifty-user", chatIconNiftyUser);
    }
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onEndScreen() {

  }

  @NiftyEventSubscriber(id="chat")
  public void onChatTextSendEvent(final String id, @Nonnull final ChatTextSendEvent event) {
    // You should post that text to the server ... 

    if (chat != null) {
      // here we simply post it to the chat window
      chat.receivedChatLine(event.getText(), chatIconNiftyUser);

      // and we generate a random reaction from the void player =)
      chat.receivedChatLine(getRandomResponse(), chatIconVoid);
    }
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
