package de.lessvoid.nifty.render.image;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.image.areaprovider.AreaProvider;
import de.lessvoid.nifty.render.image.renderstrategy.RenderStrategy;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class CompoundImageMode implements ImageMode {
  private AreaProvider m_areaProvider;
  private RenderStrategy m_renderStrategy;

  public CompoundImageMode(AreaProvider areaProvider, RenderStrategy renderStrategy) {
    m_areaProvider = areaProvider;
    m_renderStrategy = renderStrategy;
  }

  @Override
  public void setParameters(final String parameters) {
    ImageModeFactory imageModeFactory = ImageModeFactory.getSharedInstance();

    String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(parameters);
    m_areaProvider = imageModeFactory.getAreaProvider(areaProviderProperty);

    String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(parameters);
    m_renderStrategy = imageModeFactory.getRenderStrategy(renderStrategyProperty);
  }

  @Override
  public void render(
      RenderDevice renderDevice,
      RenderImage renderImage,
      int x,
      int y,
      int width,
      int height,
      Color color,
      float scale) {
    m_renderStrategy.render(renderDevice, renderImage, m_areaProvider.getSourceArea(renderImage), x, y, width, height,
        color, scale);
  }

  @Override
  public Size getImageNativeSize(NiftyImage image) {
    return m_areaProvider.getNativeSize(image);
  }
}
