package de.lessvoid.nifty.render.image.renderstrategy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class NinePartResizeStrategyTest {

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithNoParameters() {
		NinePartResizeStrategy strategy = new NinePartResizeStrategy();
		strategy.setParameters(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithInvalidParameterCount() {
		NinePartResizeStrategy strategy = new NinePartResizeStrategy();
		strategy.setParameters("1,2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithInvalidParameters() {
		NinePartResizeStrategy strategy = new NinePartResizeStrategy();
		strategy.setParameters("1,2,3,4,5,6,7,8,9,10,11,p");
	}
/*
	@Test(expected = IllegalArgumentException.class)
	public void testRenderThrowsIllegalArgumentExceptionWithNinePartDefinitionLargerThanSourceArea() {
		RenderImage image = createMock(RenderImage.class);

		RenderDevice renderDevice = createMock(RenderDevice.class);

		Box sourceArea = new Box(3, 5, 7, 11);

		NinePartResizeStrategy strategy = new NinePartResizeStrategy();
		strategy.setParameters("1,2,3,4,5,6,7,8,9,10,11,12");
		strategy.render(renderDevice, image, sourceArea, 1, 2, 3, 4, Color.NONE, 5);
	}
*/
	@Test
	public void testRenderDrawsNinePartsThatMatchStrategyParameters() {
		RenderImage image = createMock(RenderImage.class);

		RenderDevice renderDevice = createMock(RenderDevice.class);
		renderDevice.renderImage(image, 1, 2, 2, 2, 3, 5, 2, 2, Color.NONE, 5, 16, 22);
		renderDevice.renderImage(image, 3, 2, 26, 2, 5, 5, 8, 2, Color.NONE, 5, 16, 22);
		renderDevice.renderImage(image, 29, 2, 2, 2, 13, 5, 2, 2, Color.NONE, 5, 16, 22);

		renderDevice.renderImage(image, 1, 4, 1, 37, 3, 7, 1, 8, Color.NONE, 5, 16, 22);
		renderDevice.renderImage(image, 2, 4, 28, 37, 4, 7, 10, 8, Color.NONE, 5, 16, 22);
		renderDevice.renderImage(image, 30, 4, 1, 37, 14, 7, 1, 8, Color.NONE, 5, 16, 22);

		renderDevice.renderImage(image, 1, 41, 3, 1, 3, 15, 3, 1, Color.NONE, 5, 16, 22);
		renderDevice.renderImage(image, 4, 41, 24, 1, 6, 15, 3, 1, Color.NONE, 5, 16, 22);
		renderDevice.renderImage(image, 28, 41, 3, 1, 9, 15, 3, 1, Color.NONE, 5, 16, 22);
		replay(renderDevice);

		Box sourceArea = new Box(3, 5, 12, 15);

		NinePartResizeStrategy strategy = new NinePartResizeStrategy();
		strategy.setParameters("2,8,2,2,1,10,1,8,3,3,3,1");
		strategy.render(renderDevice, image, sourceArea, 1, 2, 30, 40, Color.NONE, 5);

		verify(renderDevice);
	}
}
