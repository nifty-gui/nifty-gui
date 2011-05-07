/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

/**
 *
 * @author ractoc
 */
public class ChatTextSendEvent implements NiftyEvent<Void> {
    
    private String text;

    public ChatTextSendEvent(final String textParam) {
      text = textParam;
    }
    
    public String getText() {
        return text;
    }
    
}
