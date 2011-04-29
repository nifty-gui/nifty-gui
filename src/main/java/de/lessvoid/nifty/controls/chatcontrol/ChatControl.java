package de.lessvoid.nifty.controls.chatcontrol;

import java.util.Comparator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * This is the main controller for the chat control.
 * 
 * @author Mark
 * @version 0.1
 */
public class ChatControl extends AbstractController implements Chat {

    private static final String CHAT_BOX = "chatBox-panel";
    private static final String PLAYER_LIST = "playerList";
    private static final String CHAT_TEXT_INPUT = "#chat-text-input";
    private static final Logger logger = Logger.getLogger(ChatControl.class.getName());
    private SendTextEventListener listener;
    private Screen screen;
    private Element element;
    private TextField textControl;
    private PlayerComparator playerComparator = new PlayerComparator();

    /**
     * Default constructor.
     */
    public ChatControl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void bind(final Nifty niftyParam, final Screen screenParam, final Element newElement,
            final Properties properties, final Attributes controlDefinitionAttributes) {
        logger.fine("binding chat control");
        screen = screenParam;
        element = newElement;

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
        logger.fine("starting chat screen");
        textControl = this.element.findNiftyControl(CHAT_TEXT_INPUT, TextField.class);
        //TODO: figure how to set this the right way.
        //element.findElementByName(CHAT_TEXT_INPUT).addInputHandler(this);
    }

    /**
     * {@inheritDoc 
     */
    @Override
    public final void receivedChatLine(final String text, final NiftyImage icon) {
        final ListBox<ChatEntryModelClass> chatBox = getListBox(CHAT_BOX);
        logger.log(Level.FINE, "adding message {0}", (chatBox.itemCount() + 1));
        final ChatEntryModelClass item = new ChatEntryModelClass(text, icon);
        chatBox.addItem(item);
        chatBox.showItem(item);
    }

    /**
     * {@inheritDoc 
     */
    @Override
    public final void addPlayer(final String playerName, final NiftyImage playerIcon) {
        final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
        logger.log(Level.FINE, "adding player {0}", (playerList.itemCount() + 1));
        final ChatEntryModelClass item = new ChatEntryModelClass(playerName, playerIcon);
        playerList.addItem(item);
        playerList.sortAllItems(playerComparator);
        playerList.showItem(item);
    }

    /**
     * {@inheritDoc 
     */
    @Override
    public final void removePlayer(final String playerName) {
        final ListBox<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
        logger.log(Level.FINE, "removing player {0}", playerName);
        final ChatEntryModelClass item = new ChatEntryModelClass(playerName, null);
        playerList.removeItem(item);
    }

    /**
     * This method is called when the player either presses the send button or
     * the Return key.
     */
    public final void sendText() {
        final String text = textControl.getText();
        listener.sendText(text);
        textControl.setText("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        if (inputEvent == NiftyInputEvent.SubmitText) {
            sendText();
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private ListBox<ChatEntryModelClass> getListBox(final String name) {
        final ListBox<ChatEntryModelClass> listBox = (ListBox<ChatEntryModelClass>) screen.findNiftyControl(name,
                ListBox.class);
        return listBox;
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

    /**
     * {@inheritDoc }
     */
    @Override
    public final void setSendTextEventListener(final SendTextEventListener sendTextEventListener) {
        listener = sendTextEventListener;
    }
}
