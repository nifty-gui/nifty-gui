package de.lessvoid.nifty.controls.chatcontrol.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

public class ChatCreator extends ControlAttributes {
  private static final String NAME = "nifty-chat";

  public ChatCreator(final int lines) {
    setAutoId(NiftyIdCreator.generate());
    setName(NAME);
    setLines(lines);
  }

  public ChatCreator(final String id, final int lines) {
    setId(id);
    setName(NAME);
    setLines(lines);
  }

  public void setLines(final int lines) {
    set("lines", String.valueOf(lines));
  }

  public void setSendLabel(final String sendLabel) {
    set("sendLabel", sendLabel);
  }

  public void setChatLineIconWidth(final SizeValue value) {
    set("chatLineIconWidth", value.toString());
  }

  public void setChatLineIconHeight(final SizeValue value) {
    set("chatLineIconHeight", value.toString());
  }

  public void setChatLineHeight(final SizeValue value) {
    set("chatLineHeight", value.toString());
  }

  public Chat create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), Chat.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
