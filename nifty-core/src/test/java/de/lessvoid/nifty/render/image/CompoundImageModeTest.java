package de.lessvoid.nifty.render.image;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.image.areaprovider.AreaProvider;
import de.lessvoid.nifty.render.image.renderstrategy.RenderStrategy;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class CompoundImageModeTest {

	@Test
	public void testRenderForwardsRenderCallToRenderStrategyWithAreaProvidedByAreaProvider() {
		RenderImage image = createMock(RenderImage.class);

		RenderDevice renderDevice = createMock(RenderDevice.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(1, 2, 3, 4));
		replay(areaProvider);

		RenderStrategy renderStrategy = createMock(RenderStrategy.class);
		renderStrategy.render(renderDevice, image, new Box(1, 2, 3, 4), 1, 2, 3, 4, Color.NONE, 5);
		replay(renderStrategy);

		CompoundImageMode compoundImageMode = new CompoundImageMode(areaProvider, renderStrategy);
		compoundImageMode.render(renderDevice, image, 1, 2, 3, 4, Color.NONE, 5);

		verify(renderStrategy);
		verify(areaProvider);
	}

	@Test
	public void testGetNativeSizeReturnsSameSizeAsAreaProvider() {
		NiftyImage image = createMock(NiftyImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getNativeSize(image)).andReturn(new Size(1, 2)).anyTimes();
		replay(areaProvider);

		RenderStrategy renderStrategy = createMock(RenderStrategy.class);

		CompoundImageMode imageMode = new CompoundImageMode(areaProvider, renderStrategy);
		assertEquals(imageMode.getImageNativeSize(image), areaProvider.getNativeSize(image));

		verify(areaProvider);
	}
}
