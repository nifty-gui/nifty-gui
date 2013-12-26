package de.lessvoid.nifty.render.image.areaprovider;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FullImageAreaProvider implements AreaProvider {

  @Override
  public void setParameters(@Nullable String parameters) {
    if (parameters != null) {
      throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
          + "] : expected no parameters, found [" + parameters + "].");
    }
  }

  @Nonnull
  @Override
  public Box getSourceArea(@Nonnull RenderImage renderImage) {
    return new Box(0, 0, renderImage.getWidth(), renderImage.getHeight());
  }

  @Nonnull
  @Override
  public Size getNativeSize(@Nonnull NiftyImage image) {
    return new Size(image.getWidth(), image.getHeight());
  }
}
