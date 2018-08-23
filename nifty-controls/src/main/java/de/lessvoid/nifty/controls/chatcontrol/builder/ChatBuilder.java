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

  public ChatBuilder lines(final int lines) {
    set("lines", String.valueOf(lines));
    return this;
  }

  public ChatBuilder sendLabel(@Nonnull final String sendLabel) {
    set("sendLabel", sendLabel);
    return this;
  }

  public ChatBuilder chatLineIconWidth(@Nonnull final SizeValue value) {
    set("chatLineIconWidth", value.getValueAsString());
    return this;
  }

  public ChatBuilder chatLineIconHeight(@Nonnull final SizeValue value) {
    set("chatLineIconHeight", value.getValueAsString());
    return this;
  }

  public ChatBuilder chatLineHeight(@Nonnull final SizeValue value) {
    set("chatLineHeight", value.getValueAsString());
    return this;
  }
}
