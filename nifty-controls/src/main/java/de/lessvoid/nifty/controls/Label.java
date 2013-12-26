package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Label extends NiftyControl {
  /**
   * Change the Label text.
   *
   * @param text new text
   */
  void setText(@Nullable String text);

  /**
   * Get the Label text.
   *
   * @return label text
   */
  @Nullable
  String getText();

  /**
   * Set the Label color.
   *
   * @param color the color
   */
  void setColor(@Nonnull Color color);

  /**
   * Get the current Label color.
   *
   * @return the current color of the label
   */
  @Nullable
  Color getColor();
}
