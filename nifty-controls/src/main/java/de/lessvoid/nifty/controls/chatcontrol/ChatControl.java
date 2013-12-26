package de.lessvoid.nifty.controls.chatcontrol;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main controller for the chat control.
 *
 * @author Mark
 * @version 0.1
 */
@Deprecated
public class ChatControl extends AbstractController implements Chat, KeyInputHandler {

  private static final String CHAT_BOX = "#chatBox";
  private static final String PLAYER_LIST = "#playerList";
  private static final String CHAT_TEXT_INPUT = "#chat-text-input";
  private static final Logger LOGGER = Logger.getLogger(ChatControl.class.getName());
  @Nullable
  private TextField textControl;
  @Nonnull
  private final PlayerComparator playerComparator = new PlayerComparator();
  private Nifty nifty;
  @Nonnull
  private final List<ChatEntryModelClass> playerBuffer = new ArrayList<ChatEntryModelClass>();
  @Nonnull
  private final List<ChatEntryModelClass> linesBuffer = new ArrayList<ChatEntryModelClass>();

  /**
   * Default constructor.
   */
  public ChatControl() {
  }

  @Override
  public final void bind(
      @Nonnull final Nifty niftyParam,
      @Nonnull final Screen screenParam,
      @Nonnull final Element newElement,
      @Nonnull final Parameters properties) {
    super.bind(newElement);
    LOGGER.fine("binding chat control");
    nifty = niftyParam;

    // this buffer is needed because in some cases the entry is added to either list before the element is bound.
    final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
    if (playerList == null) {
      LOGGER.severe("Element for player list \"" + PLAYER_LIST + "\" not found. ChatControl will not work.");
    } else {
      while (!playerBuffer.isEmpty()) {
        ChatEntryModelClass player = playerBuffer.remove(0);
        LOGGER.log(Level.FINE, "adding player {0}", (playerList.itemCount() + 1));
        playerList.addItem(player);
        playerList.sortAllItems(playerComparator);
        playerList.showItem(player);
      }
    }
    final ListBox<ChatEntryModelClass> chatBox = getListBox(CHAT_BOX);
    if (chatBox == null) {
      LOGGER.severe("Element for chat box \"" + CHAT_BOX + "\" not found. ChatControl will not work.");
    } else {
      while (!linesBuffer.isEmpty()) {
        ChatEntryModelClass line = linesBuffer.remove(0);
        LOGGER.log(Level.FINE, "adding message {0}", (chatBox.itemCount() + 1));
        chatBox.addItem(line);
        chatBox.showItemByIndex(chatBox.itemCount() - 1);
      }
    }
  }

  @Override
  public void onFocus(final boolean arg0) {
    if (textControl != null) {
      textControl.setFocus();
    }
  }

  @Override
  public final void onStartScreen() {
    Element element = getElement();
    if (element != null) {
      textControl = element.findNiftyControl(CHAT_TEXT_INPUT, TextField.class);
      if (textControl == null) {
        LOGGER.severe("Text input field for chat box was not found!");
      } else {
        Element textControlElement = textControl.getElement();
        if (textControlElement != null) {
          textControlElement.addInputHandler(this);
        }
      }
    }
  }

  @Override
  public final void receivedChatLine(@Nonnull String text, @Nullable NiftyImage icon) {
    receivedChatLine(text, icon, null);
  }

  @Override
  public void receivedChatLine(@Nonnull String text, @Nullable NiftyImage icon, @Nullable String style) {
    if (linesBuffer.isEmpty()) {
      final ListBox<ChatEntryModelClass> chatBox = getListBox(CHAT_BOX);
      if (chatBox != null) {
        LOGGER.log(Level.FINE, "adding message {0}", (chatBox.itemCount() + 1));
        final ChatEntryModelClass item = new ChatEntryModelClass(text, icon, style);
        chatBox.addItem(item);
        chatBox.showItemByIndex(chatBox.itemCount() - 1);
      } else {
        linesBuffer.add(new ChatEntryModelClass(text, icon, style));
      }
    } else {
      linesBuffer.add(new ChatEntryModelClass(text, icon, style));
    }
  }

  @Override
  public final void addPlayer(@Nonnull final String playerName, @Nullable final NiftyImage playerIcon) {
    addPlayer(playerName, playerIcon, null);
  }

  @Override
  public void addPlayer(@Nonnull String playerName, @Nullable NiftyImage playerIcon, @Nullable String style) {
    if (playerBuffer.isEmpty()) {
      final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
      if (playerList != null) {
        LOGGER.log(Level.FINE, "adding player {0}", (playerList.itemCount() + 1));
        final ChatEntryModelClass item = new ChatEntryModelClass(playerName, playerIcon, style);
        playerList.addItem(item);
        playerList.sortAllItems(playerComparator);
        playerList.showItem(item);
      } else {
        playerBuffer.add(new ChatEntryModelClass(playerName, playerIcon, style));
      }
    } else {
      playerBuffer.add(new ChatEntryModelClass(playerName, playerIcon, style));
    }
  }

  @Override
  public final void removePlayer(@Nonnull final String playerName) {
    final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
    if (playerList != null) {
      LOGGER.log(Level.FINE, "removing player {0}", playerName);

      final List<ChatEntryModelClass> itemList = playerList.getItems();
      final int playerCount = itemList.size();
      for (int i = 0; i < playerCount; i++) {
        ChatEntryModelClass item = itemList.get(i);
        if (item.getLabel().equals(playerName)) {
          playerList.removeItemByIndex(i);
          break;
        }
      }
    }
  }

  @Nonnull
  @Override
  public List<ChatEntryModelClass> getPlayers() {
    final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
    if (playerList == null) {
      return Collections.emptyList();
    }
    return playerList.getItems();
  }

  @Nonnull
  @Override
  public List<ChatEntryModelClass> getLines() {
    final ListBox<ChatEntryModelClass> chatBox = getListBox(CHAT_BOX);
    if (chatBox == null) {
      return Collections.emptyList();
    }
    return chatBox.getItems();
  }

  @Override
  public void update() {
    final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
    if (playerList != null) {
      playerList.refresh();
    }
  }

  /**
   * This method is called when the player either presses the send button or
   * the Return key.
   */
  public final void sendText() {
    final String text;
    if (textControl == null) {
      text = "";
    } else {
      text = textControl.getRealText();
      textControl.setText("");
    }
    final String id = getId();
    if (id != null) {
      nifty.publishEvent(id, new ChatTextSendEvent(this, text));
    }
  }

  @Nullable
  @SuppressWarnings("unchecked")
  private ListBox<ChatEntryModelClass> getListBox(@Nonnull final String name) {
    Element element = getElement();
    if (element == null) {
      return null;
    }
    return element.findNiftyControl(name, ListBox.class);
  }

  @Override
  public boolean keyEvent(@Nonnull final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.SubmitText) {
      sendText();
      return true;
    }
    return false;
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return keyEvent(inputEvent);
  }

  /**
   * Class used to sort the list of players by name.
   *
   * @author Mark
   * @version 0.2
   */
  private static class PlayerComparator implements Comparator<ChatEntryModelClass> {

    /**
     * Default constructor.
     */
    public PlayerComparator() {
    }

    @Override
    public int compare(@Nonnull final ChatEntryModelClass player1, @Nonnull final ChatEntryModelClass player2) {
      return player1.getLabel().compareToIgnoreCase(player2.getLabel());
    }
  }
}
