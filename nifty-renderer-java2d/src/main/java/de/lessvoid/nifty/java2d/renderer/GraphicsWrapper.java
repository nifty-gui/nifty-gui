package de.lessvoid.nifty.java2d.renderer;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * Used by RenderDeviceJava2dImpl to render without knowing about which drawing
 * strategy is being used.
 *
 * @author acoppes
 */
public interface GraphicsWrapper {

  @Nonnull
  Graphics2D getGraphics2d();

  int getHeight();

  int getWidth();

}