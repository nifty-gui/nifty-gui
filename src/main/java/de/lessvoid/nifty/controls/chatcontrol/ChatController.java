package de.lessvoid.nifty.controls.chatcontrol;

import java.util.Comparator;
import java.util.Properties;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
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
public class ChatController implements Controller, KeyInputHandler {

    private static final String CHAT_BOX = "chatBox";

    private static final String PLAYER_LIST = "playerList";

    private static final String CHAT_TEXT_INPUT = "chat-text-input";

    private static Logger logger = Logger.getLogger(ChatController.class.getName());

    private SendTextEventListener listener;

    private Screen screen;
    private Element element;

    private TextFieldControl textControl;

    private PlayerComparator playerComparator = new PlayerComparator();

    /**
     * Default constructor.
     */
    public ChatController() {
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
    public final boolean inputEvent(final NiftyInputEvent inputEvent) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFocus(final boolean arg0) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onStartScreen() {
        logger.fine("starting chat screen");
//        textControl = this.element.findElementByName(CHAT_TEXT_INPUT).getControl(TextFieldControl.class);
//        element.findElementByName(CHAT_TEXT_INPUT).addInputHandler(this);
    }

    /**
     * This method is called when text is received which should be displayed in
     * the chat control.
     * 
     * @param text
     *            The text to display.
     * @param icon
     *            Optionally, an icon can be supplied which is then displayed at
     *            the start of the chat line.
     */
    public final void receivedText(final String text, final NiftyImage icon) {

        final ListBoxControl<ChatEntryModelClass> chatBox = getListBox(CHAT_BOX);
        logger.fine("adding message " + (chatBox.itemCount() + 1));
        final ChatEntryModelClass item = new ChatEntryModelClass(text, icon);
        chatBox.addItem(item);
        chatBox.showItem(item);
    }

    /**
     * This method is called when a new player enters the room. This adds that
     * player to the list of players already in the room. If more then one
     * player needs to be added, this method will have to be called multiple
     * times.
     * 
     * @param playerName
     *            The player to add.
     * @param playerIcon
     *            Optionally, an icon can be supplied which is then displayed in
     *            front of the player name.
     */
    public final void addPlayer(final String playerName, final NiftyImage playerIcon) {
        final ListBoxControl<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
        logger.fine("adding player " + (playerList.itemCount() + 1));
        final ChatEntryModelClass item = new ChatEntryModelClass(playerName, playerIcon);
        playerList.addItem(item);
        playerList.sortAllItems(playerComparator);
        playerList.showItem(item);
    }

    /**
     * This method is called when a player leaves the rome and needs to be
     * removed from the list.
     * 
     * @param playerName
     *            The player name to remove.
     */
    public final void removePlayer(final String playerName) {
        final ListBoxControl<ChatEntryModelClass> playerList = getListBox(PLAYER_LIST);
        logger.fine("removing player " + playerName);
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
    public final boolean keyEvent(final NiftyInputEvent inputEvent) {
        if (inputEvent == NiftyInputEvent.SubmitText) {
            sendText();
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private ListBoxControl<ChatEntryModelClass> getListBox(final String name) {
        final ListBox<ChatEntryModelClass> listBox = (ListBox<ChatEntryModelClass>) screen.findNiftyControl(name,
                ListBox.class);
        return (ListBoxControl<ChatEntryModelClass>) listBox;
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
     * Sets the SendTextEventListener. This listener is used for a callback when
     * the player wants to send a chat message.
     * 
     * @param sendTextEventListener
     *            The SendTextEventListener which is used for a callback when
     *            the player wants to send a message.
     */
    public final void setSendTextEventListener(final SendTextEventListener sendTextEventListener) {
        listener = sendTextEventListener;
    }
}
