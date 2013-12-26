/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.NiftyEvent;

import javax.annotation.Nonnull;

/**
 * @author ractoc
 */
public class ChatTextSendEvent implements NiftyEvent {
  @Nonnull
  private final Chat chatControl;
  @Nonnull
  private final String text;

  public ChatTextSendEvent(@Nonnull final Chat chatControl, @Nonnull final String textParam) {
    this.chatControl = chatControl;
    this.text = textParam;
  }

  @Nonnull
  public Chat getChatControl() {
    return chatControl;
  }

  @Nonnull
  public String getText() {
    return text;
  }

}
