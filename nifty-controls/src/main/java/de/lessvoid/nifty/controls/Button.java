package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.Color;

/**
 * A TextButton Control.
 * @author void
 */
public interface Button extends NiftyControl {

  /**
   * Activate/Click this button.
   */
  void activate();

  /**
   * Get the current text the button shows.
   * @return text
   */
  String getText();

  /**
   * Set the current text the button shows.
   * @param text new text to show
   */
  void setText(final String text);

  /**
   * Get the width of the text.
   * @return width of text in px
   */
  int getTextWidth();

  /**
   * Get the height of the text.
   * @return height of text in px
   */
  int getTextHeight();

  /**
   * Get the current Font of the button text.
   * @return the current Font
   */
  RenderFont getFont();

  /**
   * Set the Font of the button text.
   * @param fontParam new font
   */
  void setFont(final RenderFont fontParam);

  /**
   * Get the Vertical Align of the Button text.
   * @return VerticalAlign
   */
  VerticalAlign getTextVAlign();

  /**
   * Set the Vertical Align of the Button text.
   * @param newTextVAlign VerticalAlign
   */
  void setTextVAlign(final VerticalAlign newTextVAlign);

  /**
   * Get the Horizontal Align of the Button text.
   * @return HorizontalAlign
   */
  HorizontalAlign getTextHAlign();

  /**
   * Set the Horizontal Align of the Button text.
   * @param newTextHAlign HorizontalAlign
   */
  void setTextHAlign(final HorizontalAlign newTextHAlign);

  /**
   * Get the text color of the Button Text.
   * @return Color of the Text
   */
  Color getTextColor();

  /**
   * Set the text color of the Button Text.
   * @param newColor new Color for the button text
   */
  void setTextColor(final Color newColor);
}
