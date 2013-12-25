package de.lessvoid.nifty.controls.chatcontrol;

import de.lessvoid.nifty.render.NiftyImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author ractoc
 */
final class ChatEntry {
  @Nonnull
  private String label;
  @Nullable
  private NiftyImage icon;
  @Nullable
  private String style;

  public ChatEntry(@Nonnull String label, @Nullable NiftyImage icon) {
    setLabel(label);
    setIcon(icon);
  }

  public ChatEntry(@Nonnull String label, @Nullable NiftyImage icon, @Nullable String style) {
    setLabel(label);
    setIcon(icon);
    setStyle(style);
  }

  @Nullable
  public NiftyImage getIcon() {
    return icon;
  }

  public void setIcon(@Nullable NiftyImage icon) {
    this.icon = icon;
  }

  @Nonnull
  public String getLabel() {
    return label;
  }

  public void setLabel(@Nonnull String label) {
    this.label = label;
  }

  @Nullable
  public String getStyle() {
    return style;
  }

  public void setStyle(@Nullable String style) {
    this.style = style;
  }

}
