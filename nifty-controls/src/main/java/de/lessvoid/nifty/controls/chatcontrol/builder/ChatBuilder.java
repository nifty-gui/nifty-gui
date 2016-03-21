package de.lessvoid.nifty.controls.chatcontrol.builder;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;

/**
 * @author ractoc
 */
public class ChatBuilder extends ControlBuilder {
  public ChatBuilder(final int lines) {
    super("nifty-chat");
    lines(lines);
  }

  public ChatBuilder(@Nonnull final String id, final int lines) {
    super(id, "nifty-chat");
    lines(lines);
  }

  public void lines(final int lines) {
    set("lines", String.valueOf(lines));
  }

  public void sendLabel(@Nonnull final String sendLabel) {
    set("sendLabel", sendLabel);
  }

  public void chatLineIconWidth(@Nonnull final SizeValue value) {
    set("chatLineIconWidth", value.getValueAsString());
  }

  public void chatLineIconHeight(@Nonnull final SizeValue value) {
    set("chatLineIconHeight", value.getValueAsString());
  }

  public void chatLineHeight(@Nonnull final SizeValue value) {
    set("chatLineHeight", value.getValueAsString());
  }
}
