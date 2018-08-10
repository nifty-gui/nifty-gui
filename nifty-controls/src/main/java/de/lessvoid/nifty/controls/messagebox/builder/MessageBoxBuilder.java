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

  public MessageBoxBuilder messageBoxType(@Nonnull String type) {
    set("messageBoxType", type);
    return this;
  }

  public MessageBoxBuilder message(@Nonnull String message) {
    set("message", message);
    return this;
  }

  public MessageBoxBuilder icon(@Nullable String icon) {
    if (icon != null) {
      set("icon", icon);
    }
    return this;
  }

  public MessageBoxBuilder buttonCaption(@Nonnull String buttonCaption) {
    set("buttonCaption", buttonCaption);
    return this;
  }

  public MessageBoxBuilder buttonCaptions(@Nonnull String buttonCaptions) {
    set("buttonCaptions", buttonCaptions);
    return this;
  }

}
