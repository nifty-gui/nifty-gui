package de.lessvoid.nifty.loaderv2.types;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.tools.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.logging.Logger;

public class RegisterMouseCursorType extends XmlBaseType {
  @Override
  @Nonnull
  public String output(final int offset) {
    return StringHelper.whitespace(offset) + "<registerMouseCursor> " + super.output(offset);
  }

  public void materialize(@Nonnull final Nifty nifty, @Nonnull final Logger log) {
    try {
      nifty.getNiftyMouse().registerMouseCursor(getId(), getFilename(), getHotspotX(), getHotspotY());
      log.fine("Registering mouseCursor with id [" + getId() + "]");
    } catch (IOException e) {
      log.warning("Error registering mouse cursor: " + e.getMessage());
    }
  }

  @Nullable
  private String getId() {
    return getAttributes().get("id");
  }

  @Nullable
  private String getFilename() {
    return getAttributes().get("filename");
  }

  private int getHotspotX() {
    return getAttributes().getAsInteger("hotspotX", 0);
  }

  private int getHotspotY() {
    return getAttributes().getAsInteger("hotspotY", 0);
  }
}
