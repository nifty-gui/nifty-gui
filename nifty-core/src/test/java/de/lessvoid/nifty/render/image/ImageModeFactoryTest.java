package de.lessvoid.nifty.render.image;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.image.areaprovider.AreaProvider;
import de.lessvoid.nifty.render.image.renderstrategy.RenderStrategy;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class ImageModeFactoryTest {
	ImageModeFactory m_imageModeFactory = null;

	@Before
	public void setUp() {
		Map<String, Class<? extends AreaProvider>> areaProviderMapping = new HashMap<String, Class<? extends AreaProvider>>();
		areaProviderMapping.put("dummy", DummyAreaProvider.class);

		Map<String, Class<? extends RenderStrategy>> renderStrategyMapping = new HashMap<String, Class<? extends RenderStrategy>>();
		renderStrategyMapping.put("dummy", DummyRenderStrategy.class);

		m_imageModeFactory = new ImageModeFactory(areaProviderMapping, "dummy", renderStrategyMapping, "dummy");
	}

	@Test
	public void testCreateImageModeReturnsACompoundImageMode() {
		ImageMode imageMode = m_imageModeFactory.createImageMode("dummy", "dummy");
		assertTrue(imageMode instanceof CompoundImageMode);
	}

	public static class DummyAreaProvider implements AreaProvider {

		@Override
		public void setParameters(String parameters) {
		}

		@Override
		public Box getSourceArea(RenderImage renderImage) {
			return null;
		}

		@Override
		public Size getNativeSize(NiftyImage image) {
			return null;
		}
	}

	public static class DummyRenderStrategy implements RenderStrategy {

		@Override
		public void setParameters(String parameters) {
		}

		@Override
		public void render(RenderDevice renderDevice, RenderImage renderImage, Box sourceArea, int x, int y, int width,
				int height, Color color, float scale) {
		}
	}
}
