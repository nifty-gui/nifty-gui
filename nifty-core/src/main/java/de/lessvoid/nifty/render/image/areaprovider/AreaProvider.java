package de.lessvoid.nifty.render.image.areaprovider;

import de.lessvoid.nifty.Parameterizable;
import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface AreaProvider extends Parameterizable {
  @Nullable
  Box getSourceArea(@Nonnull RenderImage renderImage);

  @Nonnull
  Size getNativeSize(@Nonnull NiftyImage image);
}
