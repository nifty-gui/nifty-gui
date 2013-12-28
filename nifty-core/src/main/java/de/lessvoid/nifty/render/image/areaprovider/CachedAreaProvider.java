package de.lessvoid.nifty.render.image.areaprovider;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CachedAreaProvider implements AreaProvider {
  private final AreaProvider m_cachedProvider;

  @Nullable
  private RenderImage m_lastProcessedImage;
  @Nullable
  private Box m_cachedArea;

  public CachedAreaProvider(AreaProvider cachedProvider) {
    m_cachedProvider = cachedProvider;
  }

  @Override
  public void setParameters(String parameters) {
    m_lastProcessedImage = null;
    m_cachedArea = null;
  }

  @Nullable
  @Override
  public Box getSourceArea(@Nonnull RenderImage renderImage) {
    if (renderImage != m_lastProcessedImage) {
      m_lastProcessedImage = renderImage;
      m_cachedArea = m_cachedProvider.getSourceArea(renderImage);
    }

    return m_cachedArea;
  }

  @Nonnull
  @Override
  public Size getNativeSize(@Nonnull NiftyImage image) {
    return m_cachedProvider.getNativeSize(image);
  }
}
