/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;
import de.lessvoid.nifty.controls.chatcontrol.ChatControl;

/**
 *
 * @author ractoc
 */
public class ChatTextSendEvent implements NiftyEvent {
    private ChatControl chatControl;
    private String text;

    public ChatTextSendEvent(final ChatControl chatControl, final String textParam) {
      this.chatControl = chatControl;
      this.text = textParam;
    }

    public ChatControl getChatControl() {
      return chatControl;
    }

    public String getText() {
        return text;
    }
    
}
