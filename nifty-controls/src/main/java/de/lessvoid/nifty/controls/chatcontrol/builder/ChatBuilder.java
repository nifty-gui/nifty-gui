package de.lessvoid.nifty.controls.chatcontrol.builder;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.tools.SizeValue;

/**
 *
 * @author ractoc
 */
public class ChatBuilder extends ControlBuilder {
  public ChatBuilder(final int lines) {
    super("nifty-chat");
    lines(lines);
  }

  public ChatBuilder(final String id, final int lines) {
    super(id, "nifty-chat");
    lines(lines);
  }

  public void lines(final int lines) {
    set("lines", String.valueOf(lines));
  }

  public void sendLabel(final String sendLabel) {
    set("sendLabel", sendLabel);
  }

  public void chatLineIconWidth(final SizeValue value) {
    set("chatLineIconWidth", value.toString());
  }

  public void chatLineIconHeight(final SizeValue value) {
    set("chatLineIconHeight", value.toString());
  }

  public void chatLineHeight(final SizeValue value) {
    set("chatLineHeight", value.toString());
  }
}
