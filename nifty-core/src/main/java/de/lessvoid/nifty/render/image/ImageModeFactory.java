package de.lessvoid.nifty.render.image;

import java.util.HashMap;
import java.util.Map;

import de.lessvoid.nifty.ParameterizedObjectFactory;
import de.lessvoid.nifty.render.image.areaprovider.AreaProvider;
import de.lessvoid.nifty.render.image.areaprovider.CachedAreaProvider;
import de.lessvoid.nifty.render.image.areaprovider.FullImageAreaProvider;
import de.lessvoid.nifty.render.image.areaprovider.SpriteAreaProvider;
import de.lessvoid.nifty.render.image.areaprovider.SubImageAreaProvider;
import de.lessvoid.nifty.render.image.renderstrategy.ClampStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.NinePartResizeStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.RenderDirectlyStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.RenderStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.RepeatStrategy;
import de.lessvoid.nifty.render.image.renderstrategy.ResizeStrategy;

public class ImageModeFactory {

	private static ImageModeFactory s_sharedInstance = null;

	private final ParameterizedObjectFactory<AreaProvider> m_areaProviderFactory;
	private final ParameterizedObjectFactory<RenderStrategy> m_renderStrategyFactory;

	public ImageModeFactory(Map<String, Class<? extends AreaProvider>> areaProviderMapping,
			String fallbackAreaProvider, Map<String, Class<? extends RenderStrategy>> renderStrategyMapping,
			String fallbackRenderStrategyName) {
		m_areaProviderFactory = new ParameterizedObjectFactory<AreaProvider>(areaProviderMapping, fallbackAreaProvider);
		m_renderStrategyFactory = new ParameterizedObjectFactory<RenderStrategy>(renderStrategyMapping,
				fallbackRenderStrategyName);
	}

	public ImageMode createImageMode(String areaProviderDescription, String renderStrategyDescription) {
		return new CompoundImageMode(
		    new CachedAreaProvider(
		        getAreaProvider(areaProviderDescription)),
		        getRenderStrategy(renderStrategyDescription));
	}

  AreaProvider getAreaProvider(final String areaProviderDescription) {
    return m_areaProviderFactory.create(areaProviderDescription);
  }

  RenderStrategy getRenderStrategy(final String renderStrategyDescription) {
    return m_renderStrategyFactory.create(renderStrategyDescription);
  }

	synchronized public static ImageModeFactory getSharedInstance() {
		if (s_sharedInstance == null) {
			Map<String, Class<? extends AreaProvider>> areaProviderMapping = new HashMap<String, Class<? extends AreaProvider>>();
			areaProviderMapping.put("fullimage", FullImageAreaProvider.class);
			areaProviderMapping.put("sprite", SpriteAreaProvider.class);
			areaProviderMapping.put("subimage", SubImageAreaProvider.class);

			Map<String, Class<? extends RenderStrategy>> renderStrategyMapping = new HashMap<String, Class<? extends RenderStrategy>>();
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
