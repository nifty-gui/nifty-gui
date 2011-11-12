package de.lessvoid.nifty.render.image.areaprovider;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.image.areaprovider.SpriteAreaProvider;
import de.lessvoid.nifty.spi.render.RenderImage;

public class SpriteAreaProviderTest {

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithNoParameters() {
		SpriteAreaProvider areaProvider = new SpriteAreaProvider();
		areaProvider.setParameters(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithInvalidParameterCount() {
		SpriteAreaProvider areaProvider = new SpriteAreaProvider();
		areaProvider.setParameters("1,2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetParametersThrowsIllegalArgumentExceptionWithInvalidParameters() {
		SpriteAreaProvider areaProvider = new SpriteAreaProvider();
		areaProvider.setParameters("1,2,p");
	}
/*
	@Test(expected = IllegalArgumentException.class)
	public void testGetSourceAreaThrowsIllegalArgumentExceptionWhenOutOfImageBounds() {
		RenderImage image = createMock(RenderImage.class);
		expect(image.getWidth()).andReturn(12).anyTimes();
		expect(image.getHeight()).andReturn(12).anyTimes();
		replay(image);

		SpriteAreaProvider areaProvider = new SpriteAreaProvider();
		areaProvider.setParameters("5,5,4");
		areaProvider.getSourceArea(image);
	}
*/
	@Test
	public void testGetSourceAreaReturnsAnAreaMatchingTheSizeOfTheSprite() {
		RenderImage image = createMock(RenderImage.class);
		expect(image.getWidth()).andReturn(8).anyTimes();
		expect(image.getHeight()).andReturn(8).anyTimes();
		replay(image);

		SpriteAreaProvider areaProvider = new SpriteAreaProvider();
		areaProvider.setParameters("2,2,7");
		assertTrue(areaProvider.getSourceArea(image).getWidth() == 2);
		assertTrue(areaProvider.getSourceArea(image).getHeight() == 2);

		verify(image);
	}

	@Test
	public void testGetSourceAreaReturnsUpperLeftBoundsWithFirstSpriteIndex() {
		RenderImage image = createMock(RenderImage.class);
		expect(image.getWidth()).andReturn(8).anyTimes();
		expect(image.getHeight()).andReturn(8).anyTimes();
		replay(image);

		SpriteAreaProvider areaProvider = new SpriteAreaProvider();
		areaProvider.setParameters("2,2,0");
		assertEquals(new Box(0, 0, 2, 2), areaProvider.getSourceArea(image));

		verify(image);
	}

	@Test
	public void testGetSourceAreaReturnsUpperLeftBoundsWithLastSpriteIndex() {
		RenderImage image = createMock(RenderImage.class);
		expect(image.getWidth()).andReturn(8).anyTimes();
		expect(image.getHeight()).andReturn(8).anyTimes();
		replay(image);

		SpriteAreaProvider areaProvider = new SpriteAreaProvider();
		areaProvider.setParameters("2,2,15");
		assertEquals(new Box(6, 6, 2, 2), areaProvider.getSourceArea(image));

		verify(image);
	}

	@Test
	public void testGetNativeSizeReturnsSpriteSize() {
		NiftyImage image = createMock(NiftyImage.class);

		SpriteAreaProvider areaProvider = new SpriteAreaProvider();
		areaProvider.setParameters("1,2,0");
		assertEquals(areaProvider.getNativeSize(image), new Size(1, 2));
	}
}
