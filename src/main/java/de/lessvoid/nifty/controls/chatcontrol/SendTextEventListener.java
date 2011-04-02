package de.lessvoid.nifty.controls.chatcontrol;

/**
 * Callback interface for text entered in the Chat control.
 * @author Mark
 * @version 0.1 
 */
public interface SendTextEventListener {
    
    /**
     * The supplied text is to be sent to the other people in the chat.
     * @param text The text to send.
     */
    void sendText(String text);

}
