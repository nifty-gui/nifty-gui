package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.tools.Color;

public interface Label extends NiftyControl {

  /**
   * Change the Label text.
   * @param text new text
   */
  void setText(String text);

  /**
   * Get the Label text.
   * @return label text
   */
  String getText();

  /**
   * Set the Label color.
   * @param color the color
   */
  void setColor(Color color);

  /**
   * Get the current Label color.
   * @return the current color of the label
   */
  Color getColor();
}
