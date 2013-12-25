package de.lessvoid.nifty.render.image;

import de.lessvoid.nifty.ParameterizedObjectFactory;
import de.lessvoid.nifty.render.image.areaprovider.*;
import de.lessvoid.nifty.render.image.renderstrategy.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ImageModeFactory {

  @Nullable
  private static ImageModeFactory s_sharedInstance = null;

  @Nonnull
  private final ParameterizedObjectFactory<AreaProvider> m_areaProviderFactory;
  @Nonnull
  private final ParameterizedObjectFactory<RenderStrategy> m_renderStrategyFactory;

  public ImageModeFactory(
      @Nonnull Map<String, Class<? extends AreaProvider>> areaProviderMapping,
      @Nonnull String fallbackAreaProvider,
      @Nonnull Map<String, Class<? extends RenderStrategy>> renderStrategyMapping,
      @Nonnull String fallbackRenderStrategyName) {
    m_areaProviderFactory = new ParameterizedObjectFactory<AreaProvider>(areaProviderMapping, fallbackAreaProvider);
    m_renderStrategyFactory = new ParameterizedObjectFactory<RenderStrategy>(renderStrategyMapping,
        fallbackRenderStrategyName);
  }

  @Nonnull
  public ImageMode createImageMode(
      @Nullable final String areaProviderDescription,
      @Nullable final String renderStrategyDescription) {
    return new CompoundImageMode(
        new CachedAreaProvider(
            getAreaProvider(areaProviderDescription)),
        getRenderStrategy(renderStrategyDescription));
  }

  @Nonnull
  AreaProvider getAreaProvider(@Nullable final String areaProviderDescription) {
    return m_areaProviderFactory.create(areaProviderDescription);
  }

  @Nonnull
  RenderStrategy getRenderStrategy(@Nullable final String renderStrategyDescription) {
    return m_renderStrategyFactory.create(renderStrategyDescription);
  }

  @Nonnull
  synchronized public static ImageModeFactory getSharedInstance() {
    if (s_sharedInstance == null) {
      Map<String, Class<? extends AreaProvider>> areaProviderMapping = new HashMap<String,
          Class<? extends AreaProvider>>();
      areaProviderMapping.put("fullimage", FullImageAreaProvider.class);
      areaProviderMapping.put("sprite", SpriteAreaProvider.class);
      areaProviderMapping.put("subimage", SubImageAreaProvider.class);

      Map<String, Class<? extends RenderStrategy>> renderStrategyMapping = new HashMap<String,
          Class<? extends RenderStrategy>>();
      renderStrategyMapping.put("clamp", ClampStrategy.class);
      renderStrategyMapping.put("nine-part", NinePartResizeStrategy.class);
      renderStrategyMapping.put("repeat", RepeatStrategy.class);
      renderStrategyMapping.put("resize", ResizeStrategy.class);
      renderStrategyMapping.put("direct", RenderDirectlyStrategy.class);

      s_sharedInstance = new ImageModeFactory(areaProviderMapping, "fullimage", renderStrategyMapping, "resize");
    }

    return s_sharedInstance;
  }
}
