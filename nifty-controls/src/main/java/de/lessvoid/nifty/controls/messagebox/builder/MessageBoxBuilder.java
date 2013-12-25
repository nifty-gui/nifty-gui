package de.lessvoid.nifty.controls.messagebox.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MessageBoxBuilder extends ControlBuilder {

  public MessageBoxBuilder() {
    super("nifty-messagebox");
  }

  public MessageBoxBuilder(@Nonnull String id) {
    super("nifty-messagebox", id);
  }

  public void messageBoxType(@Nonnull String type) {
    set("messageBoxType", type);
  }

  public void message(@Nonnull String message) {
    set("message", message);
  }

  public void icon(@Nullable String icon) {
    if (icon != null) {
      set("icon", icon);
    }
  }

  public void buttonCaption(@Nonnull String buttonCaption) {
    set("buttonCaption", buttonCaption);
  }

  public void buttonCaptions(@Nonnull String buttonCaptions) {
    set("buttonCaptions", buttonCaptions);
  }

}
