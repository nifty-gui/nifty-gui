package de.lessvoid.nifty.render.image.renderstrategy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.image.areaprovider.AreaProvider;
import de.lessvoid.nifty.render.image.renderstrategy.ResizeStrategy;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class ResizeStrategyTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithParameters() {
		ResizeStrategy resizeStrategy = new ResizeStrategy();
		resizeStrategy.setParameters("");
	}

	@Test
	public void testRenderForwardsFullSourceAreaAndFullDestinationAreaToRenderDevice() {
		RenderImage image = createMock(RenderImage.class);
		expect(image.getWidth()).andReturn(8).anyTimes();
		expect(image.getHeight()).andReturn(10).anyTimes();
		replay(image);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(1, 2, 3, 4)).anyTimes();
		replay(areaProvider);

		RenderDevice renderDevice = createMock(RenderDevice.class);
		renderDevice.renderImage(image, 5, 6, 14, 16, 1, 2, 3, 4, Color.NONE, 1, 12, 14);
		replay(renderDevice);

		ResizeStrategy resizeStrategy = new ResizeStrategy();
		resizeStrategy.setParameters(null);
		resizeStrategy.render(renderDevice, image, areaProvider.getSourceArea(image), 5, 6, 14, 16, Color.NONE, 1);

		verify(renderDevice);
	}
}
