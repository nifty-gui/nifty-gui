package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A TextButton Control.
 *
 * @author void
 */
public interface Button extends NiftyControl {

  /**
   * Activate/Click this button.
   */
  void activate();

  /**
   * Get the current text the button shows.
   *
   * @return text
   */
  @Nonnull
  String getText();

  /**
   * Set the current text the button shows.
   *
   * @param text new text to show
   */
  void setText(@Nonnull final String text);

  /**
   * Get the width of the text.
   *
   * @return width of text in px
   */
  int getTextWidth();

  /**
   * Get the height of the text.
   *
   * @return height of text in px
   */
  int getTextHeight();

  /**
   * Get the current Font of the button text.
   *
   * @return the current Font
   */
  @Nullable
  RenderFont getFont();

  /**
   * Set the Font of the button text.
   *
   * @param fontParam new font or {@code null} to use the default font
   */
  void setFont(@Nullable final RenderFont fontParam);

  /**
   * Get the Vertical Align of the Button text.
   *
   * @return VerticalAlign
   */
  @Nonnull
  VerticalAlign getTextVAlign();

  /**
   * Set the Vertical Align of the Button text.
   *
   * @param newTextVAlign VerticalAlign
   */
  void setTextVAlign(@Nonnull final VerticalAlign newTextVAlign);

  /**
   * Get the Horizontal Align of the Button text.
   *
   * @return HorizontalAlign
   */
  @Nonnull
  HorizontalAlign getTextHAlign();

  /**
   * Set the Horizontal Align of the Button text.
   *
   * @param newTextHAlign HorizontalAlign
   */
  void setTextHAlign(@Nonnull final HorizontalAlign newTextHAlign);

  /**
   * Get the text color of the Button Text.
   *
   * @return Color of the Text
   */
  @Nonnull
  Color getTextColor();

  /**
   * Set the text color of the Button Text.
   *
   * @param newColor new Color for the button text
   */
  void setTextColor(@Nonnull final Color newColor);
}
