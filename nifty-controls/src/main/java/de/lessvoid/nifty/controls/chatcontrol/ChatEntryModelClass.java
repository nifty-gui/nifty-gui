package de.lessvoid.nifty.controls.chatcontrol;

import de.lessvoid.nifty.render.NiftyImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Handles a line in the chat controller. This can be either a chat line or an
 * entry in the list of players.
 *
 * @author Mark
 * @version 0.1
 */
public final class ChatEntryModelClass {

  @Nonnull
  private final ChatEntry chatEntry;

  /**
   * Constructor excepting the line and the icon.
   *
   * @param labelParam The label to put in the entry. This can be either a chat line or a player name.
   * @param iconParam  The icon to display in the entry, this one is optional.
   */
  public ChatEntryModelClass(@Nonnull final String labelParam, final NiftyImage iconParam) {
    this.chatEntry = new ChatEntry(labelParam, iconParam);
  }

  public ChatEntryModelClass(@Nonnull final String labelParam, final NiftyImage iconParam, String style) {
    this.chatEntry = new ChatEntry(labelParam, iconParam, style);
  }

  @Nonnull
  @Override
  public String toString() {
    return chatEntry.getLabel();
  }

  /**
   * Return the supplied label. This can be either a chat line or a player name.
   *
   * @return The supplied label.
   */
  @Nonnull
  public String getLabel() {
    return chatEntry.getLabel();
  }

  /**
   * Set a new label to replace the current one.
   *
   * @param label The new label.
   */
  public void setLabel(@Nonnull String label) {
    chatEntry.setLabel(label);
  }

  /**
   * Return the supplied icon.
   *
   * @return The supplied icon.
   */
  @Nullable
  public NiftyImage getIcon() {
    return chatEntry.getIcon();
  }

  /**
   * Supply a new icon which replaces the current one.
   *
   * @param icon The icon.
   */
  public void setIcon(@Nullable NiftyImage icon) {
    chatEntry.setIcon(icon);
  }

  /**
   * Returns the style of the current entry.
   *
   * @return The style.
   */
  @Nullable
  public String getStyle() {
    return this.chatEntry.getStyle();
  }

  /**
   * Supply a new style which replaces the current one, null reverts to the
   * default style from the XML.
   *
   * @param style The new style.
   */
  public void setStyle(@Nullable String style) {
    this.chatEntry.setStyle(style);
  }

  @Override
  public boolean equals(@Nullable final Object obj) {
    if (!(obj instanceof ChatEntryModelClass)) {
      return false;
    }
    return this.getLabel().equalsIgnoreCase(((ChatEntryModelClass) obj).getLabel());
  }

  @Override
  public int hashCode() {
    return this.getLabel().hashCode();
  }

}
