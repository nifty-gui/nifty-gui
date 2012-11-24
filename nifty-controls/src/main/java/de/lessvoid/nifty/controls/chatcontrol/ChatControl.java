package de.lessvoid.nifty.controls.chatcontrol;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * This is the main controller for the chat control.
 * 
 * @author Mark
 * @version 0.1
 */
public class ChatControl extends AbstractController implements Chat, KeyInputHandler {

    private static final String CHAT_BOX = "#chatBox";
    private static final String PLAYER_LIST = "#playerList";
    private static final String CHAT_TEXT_INPUT = "#chat-text-input";
    private static final Logger LOGGER = Logger.getLogger(ChatControl.class.getName());
    private TextField textControl;
    private PlayerComparator playerComparator = new PlayerComparator();
    private Nifty nifty;
    private List<ChatEntryModelClass> playerBuffer = new ArrayList<ChatEntryModelClass>();
    private List<ChatEntryModelClass> linesBuffer = new ArrayList<ChatEntryModelClass>();

    /**
     * Default constructor.
     */
    public ChatControl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void bind(final Nifty niftyParam, final Screen screenParam, final Element newElement, final Properties properties, final Attributes controlDefinitionAttributes) {
        super.bind(newElement);
        LOGGER.fine("binding chat control");
        nifty = niftyParam;

        // this buffer is needed because in some cases the entry is added to either list before the emelent is bound.
        final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
        while (!playerBuffer.isEmpty()) {
            ChatEntryModelClass player = playerBuffer.remove(0);
            LOGGER.log(Level.FINE, "adding player {0}", (playerList.itemCount() + 1));
            playerList.addItem(player);
            playerList.sortAllItems(playerComparator);
            playerList.showItem(player);
        }
        final ListBox<ChatEntryModelClass> chatBox = getListBox(CHAT_BOX);
        while (!linesBuffer.isEmpty()) {
            ChatEntryModelClass line = linesBuffer.remove(0);
            LOGGER.log(Level.FINE, "adding message {0}", (chatBox.itemCount() + 1));
            chatBox.addItem(line);
            chatBox.showItemByIndex(chatBox.itemCount() - 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFocus(final boolean arg0) {
        textControl.setFocus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onStartScreen() {
        LOGGER.fine("starting chat screen");
        textControl = getElement().findNiftyControl(CHAT_TEXT_INPUT, TextField.class);
        textControl.getElement().addInputHandler(this);
    }

    /**
     * {@inheritDoc 
     */
    @Override
    public final void receivedChatLine(final String text, final NiftyImage icon) {
        receivedChatLine(text, icon, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivedChatLine(String text, NiftyImage icon, String style) {
        if (linesBuffer.isEmpty()) {
            try {
                final ListBox<ChatEntryModelClass> chatBox = getListBox(CHAT_BOX);
                LOGGER.log(Level.FINE, "adding message {0}", (chatBox.itemCount() + 1));
                final ChatEntryModelClass item = new ChatEntryModelClass(text, icon, style);
                chatBox.addItem(item);
                chatBox.showItemByIndex(chatBox.itemCount() - 1);
            } catch (NullPointerException npe) {
                linesBuffer.add(new ChatEntryModelClass(text, icon, style));
            }
        } else {
            linesBuffer.add(new ChatEntryModelClass(text, icon, style));
        }
    }

    /**
     * {@inheritDoc 
     */
    @Override
    public final void addPlayer(final String playerName, final NiftyImage playerIcon) {
        addPlayer(playerName, playerIcon, null);
    }

    /**
     * {@inheritDoc 
     */
    @Override
    public void addPlayer(String playerName, NiftyImage playerIcon, String style) {
        if (playerBuffer.isEmpty()) {
            try {
                final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
                LOGGER.log(Level.FINE, "adding player {0}", (playerList.itemCount() + 1));
                final ChatEntryModelClass item = new ChatEntryModelClass(playerName, playerIcon, style);
                playerList.addItem(item);
                playerList.sortAllItems(playerComparator);
                playerList.showItem(item);
            } catch (NullPointerException npe) {
                playerBuffer.add(new ChatEntryModelClass(playerName, playerIcon, style));
            }
        } else {
            playerBuffer.add(new ChatEntryModelClass(playerName, playerIcon, style));
        }
    }

    /**
     * {@inheritDoc 
     */
    @Override
    public final void removePlayer(final String playerName) {
        final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
        LOGGER.log(Level.FINE, "removing player {0}", playerName);
        final ChatEntryModelClass item = new ChatEntryModelClass(playerName, null);
        playerList.removeItem(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChatEntryModelClass> getPlayers() {
        final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
        return playerList.getItems();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChatEntryModelClass> getLines() {
        final ListBox<ChatEntryModelClass> chatBox = getListBox(CHAT_BOX);
        return chatBox.getItems();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        getListBox(PLAYER_LIST).refresh();
    }

    /**
     * This method is called when the player either presses the send button or
     * the Return key.
     */
    public final void sendText() {
        final String text = textControl.getText();
        nifty.publishEvent(getId(), new ChatTextSendEvent(this, text));
        textControl.setText("");
    }

    private ListBox<ChatEntryModelClass> getListBox(final String name) {

        return getElement().findNiftyControl(name, ListBox.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyEvent(final NiftyInputEvent inputEvent) {
        if (inputEvent == NiftyStandardInputEvent.SubmitText) {
            sendText();
            return true;
        } else if (inputEvent == NiftyStandardInputEvent.MoveCursorRight) {
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        return keyEvent(inputEvent);
    }

    /**
     * Class used to sort the list of players by name.
     * 
     * @author Mark
     * @version 0.2
     */
    private class PlayerComparator implements Comparator<ChatEntryModelClass> {

        /**
         * Default constructor.
         */
        public PlayerComparator() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(final ChatEntryModelClass player1, final ChatEntryModelClass player2) {
            return player1.getLabel().compareToIgnoreCase(player2.getLabel());
        }
    }
}
