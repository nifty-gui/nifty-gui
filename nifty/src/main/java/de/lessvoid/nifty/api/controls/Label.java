package de.lessvoid.nifty.api.controls;

import de.lessvoid.nifty.api.NiftyCanvas;
import de.lessvoid.nifty.api.NiftyCanvasPainter;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyNode;

public class Label extends NiftyAbstractControl {
  private NiftyColor textColor = NiftyColor.WHITE();
  private String text;

  public void init(final NiftyNode niftyNode) {
    super.init(niftyNode);
    niftyNode.setContent(new LabelCanvasPainter());
  }

  /**
   * Change the Label text.
   *
   * @param text new text
   */
  public void setText(final String text) {
    this.text = text;
  }

  /**
   * Get the Label text.
   *
   * @return label text
   */
  public String getText() {
    return text;
  }

  /**
   * Set the Label color.
   *
   * @param color the color
   */
  public void setColor(final NiftyColor color) {
    this.textColor = color;
  }

  /**
   * Get the current Label color.
   *
   * @return the current color of the label
   */
  public NiftyColor getColor() {
    return textColor;
  }

  private class LabelCanvasPainter implements NiftyCanvasPainter {
    @Override
    public void paint(final NiftyNode node, final NiftyCanvas canvas) {
      canvas.setFillStyle(textColor);
      canvas.fillRect(10, 10, 100, 100);
    }
  }
}
