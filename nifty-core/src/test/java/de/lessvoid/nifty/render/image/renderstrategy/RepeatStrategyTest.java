package de.lessvoid.nifty.render.image.renderstrategy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.image.areaprovider.AreaProvider;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class RepeatStrategyTest {

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithParameters() {
		RepeatStrategy repeatStrategy = new RepeatStrategy();
		repeatStrategy.setParameters("");
	}

	@Test
	public void testRenderAnArea1x1TimeItsSizeCallsRenderMethodOnce() {
		RenderImage image = createMock(RenderImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(3, 5, 7, 9));
		replay(areaProvider);

		RenderDevice renderDevice = createMock(RenderDevice.class);
		renderDevice.renderImage(image, 1, 2, 7, 9, 3, 5, 7, 9, Color.NONE, 11, 4, 6);
		replay(renderDevice);

		RepeatStrategy repeatStrategy = new RepeatStrategy();
		repeatStrategy.setParameters(null);
		repeatStrategy.render(renderDevice, image, areaProvider.getSourceArea(image), 1, 2, 7, 9, Color.NONE, 11);

		verify(renderDevice);
	}

	@Test
	public void testRenderAnArea0_5x0_5TimeItsSizeCallsRenderMethodOnce() {
		RenderImage image = createMock(RenderImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(3, 5, 8, 10));
		replay(areaProvider);

		RenderDevice renderDevice = createMock(RenderDevice.class);
		renderDevice.renderImage(image, 1, 2, 4, 5, 3, 5, 4, 5, Color.NONE, 11, 3, 4);
		replay(renderDevice);

		RepeatStrategy repeatStrategy = new RepeatStrategy();
		repeatStrategy.setParameters(null);
		repeatStrategy.render(renderDevice, image, areaProvider.getSourceArea(image), 1, 2, 4, 5, Color.NONE, 11);

		verify(renderDevice);
	}

	@Test
	public void testRenderAnArea2x2TimeItsSizeCallsRenderMethod4Times() {
		RenderImage image = createMock(RenderImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(3, 5, 7, 9));
		replay(areaProvider);

		RenderDevice renderDevice = createMock(RenderDevice.class);
		renderDevice.renderImage(image, 1, 2, 7, 9, 3, 5, 7, 9, Color.NONE, 11, 8, 11);
		renderDevice.renderImage(image, 8, 2, 7, 9, 3, 5, 7, 9, Color.NONE, 11, 8, 11);
		renderDevice.renderImage(image, 1, 11, 7, 9, 3, 5, 7, 9, Color.NONE, 11, 8, 11);
		renderDevice.renderImage(image, 8, 11, 7, 9, 3, 5, 7, 9, Color.NONE, 11, 8, 11);
		replay(renderDevice);

		RepeatStrategy repeatStrategy = new RepeatStrategy();
		repeatStrategy.setParameters(null);
		repeatStrategy.render(renderDevice, image, areaProvider.getSourceArea(image), 1, 2, 14, 18, Color.NONE, 11);

		verify(renderDevice);
	}

	@Test
	public void testRenderAnArea2_5x2_5TimeItsSizeCallsRenderMethod9Times() {
		RenderImage image = createMock(RenderImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(3, 5, 8, 10));
		replay(areaProvider);

		RenderDevice renderDevice = createMock(RenderDevice.class);
		renderDevice.renderImage(image, 1, 2, 8, 10, 3, 5, 8, 10, Color.NONE, 11, 11, 14);
		renderDevice.renderImage(image, 9, 2, 8, 10, 3, 5, 8, 10, Color.NONE, 11, 11, 14);
		renderDevice.renderImage(image, 17, 2, 4, 10, 3, 5, 4, 10, Color.NONE, 11, 11, 14);

		renderDevice.renderImage(image, 1, 12, 8, 10, 3, 5, 8, 10, Color.NONE, 11, 11, 14);
		renderDevice.renderImage(image, 9, 12, 8, 10, 3, 5, 8, 10, Color.NONE, 11, 11, 14);
		renderDevice.renderImage(image, 17, 12, 4, 10, 3, 5, 4, 10, Color.NONE, 11, 11, 14);

		renderDevice.renderImage(image, 1, 22, 8, 5, 3, 5, 8, 5, Color.NONE, 11, 11, 14);
		renderDevice.renderImage(image, 9, 22, 8, 5, 3, 5, 8, 5, Color.NONE, 11, 11, 14);
		renderDevice.renderImage(image, 17, 22, 4, 5, 3, 5, 4, 5, Color.NONE, 11, 11, 14);
		replay(renderDevice);

		RepeatStrategy repeatStrategy = new RepeatStrategy();
		repeatStrategy.setParameters(null);
		repeatStrategy.render(renderDevice, image, areaProvider.getSourceArea(image), 1, 2, 20, 25, Color.NONE, 11);

		verify(renderDevice);
	}
}
