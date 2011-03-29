package de.lessvoid.nifty.render.image.areaprovider;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.image.areaprovider.SubImageAreaProvider;
import de.lessvoid.nifty.spi.render.RenderImage;

public class SubImageAreaProviderTest {

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithNoParameters() {
		SubImageAreaProvider areaProvider = new SubImageAreaProvider();
		areaProvider.setParameters(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithInvalidParameterCount() {
		SubImageAreaProvider areaProvider = new SubImageAreaProvider();
		areaProvider.setParameters("1,2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithInvalidParameters() {
		SubImageAreaProvider areaProvider = new SubImageAreaProvider();
		areaProvider.setParameters("1,2,3,p");
	}
/*
	@Test(expected = IllegalArgumentException.class)
	public void testGetSourceAreaThrowsIllegalArgumentExceptionWhenOutOfImageBounds() {
		RenderImage image = createMock(RenderImage.class);
		expect(image.getWidth()).andReturn(10);
		expect(image.getHeight()).andReturn(10);
		replay(image);

		SubImageAreaProvider areaProvider = new SubImageAreaProvider();
		areaProvider.setParameters("5,5,10,10");
		areaProvider.getSourceArea(image);

		verify(image);
	}
*/
	@Test
	public void testGetSourceAreaReturnsAnAreaMatchingInitializationParameters() {
		RenderImage image = createMock(RenderImage.class);
		expect(image.getWidth()).andReturn(10);
		expect(image.getHeight()).andReturn(10);
		replay(image);

		SubImageAreaProvider areaProvider = new SubImageAreaProvider();
		areaProvider.setParameters("1,2,3,4");
		assertEquals(new Box(1, 2, 3, 4), areaProvider.getSourceArea(image));

		verify(image);
	}

	@Test
	public void testGetNativeSizeReturnsImageSize() {
		NiftyImage image = createMock(NiftyImage.class);
		expect(image.getWidth()).andReturn(1);
		expect(image.getHeight()).andReturn(2);
		replay(image);

		SubImageAreaProvider areaProvider = new SubImageAreaProvider();
		assertEquals(areaProvider.getNativeSize(image), new Size(1, 2));

		verify(image);
	}
}
