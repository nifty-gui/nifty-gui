package de.lessvoid.nifty.render.image.areaprovider;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

public class CachedAreaProviderTest {

	@Test
	public void testGetSourceAreaReturnsSameAreaAsCachedProvider() {
		RenderImage image = createMock(RenderImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(1, 2, 3, 4)).anyTimes();
		replay(areaProvider);

		CachedAreaProvider cachedAreaProvider = new CachedAreaProvider(areaProvider);
		assertEquals(cachedAreaProvider.getSourceArea(image), areaProvider.getSourceArea(image));

		verify(areaProvider);
	}

	@Test
	public void testGetSourceAreaCachesReturnedValueForASingleImage() {
		RenderImage image = createMock(RenderImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(1, 2, 3, 4));
		replay(areaProvider);

		CachedAreaProvider cachedAreaProvider = new CachedAreaProvider(areaProvider);
		assertSame(cachedAreaProvider.getSourceArea(image), cachedAreaProvider.getSourceArea(image));

		verify(areaProvider);
	}

	@Test
	public void testGetSourceAreaInvalidatesCachedAreaForADifferentImage() {
		RenderImage firstImage = createMock(RenderImage.class);
		RenderImage secondImage = createMock(RenderImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getSourceArea(firstImage)).andReturn(new Box(1, 2, 3, 4));
		expect(areaProvider.getSourceArea(secondImage)).andReturn(new Box(1, 2, 3, 4));
		replay(areaProvider);

		CachedAreaProvider cachedAreaProvider = new CachedAreaProvider(areaProvider);
		assertNotSame(cachedAreaProvider.getSourceArea(firstImage), cachedAreaProvider.getSourceArea(secondImage));

		verify(areaProvider);
	}

	@Test
	public void testSetParametersInvalidatesCachedArea() {
		RenderImage image = createMock(RenderImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);

		CachedAreaProvider cachedAreaProvider = new CachedAreaProvider(areaProvider);
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(1, 2, 3, 4));
		expect(areaProvider.getSourceArea(image)).andReturn(new Box(1, 2, 3, 4));
		replay(areaProvider);

		Box firstArea = cachedAreaProvider.getSourceArea(image);
		cachedAreaProvider.setParameters(null);

		Box secondArea = cachedAreaProvider.getSourceArea(image);
		assertNotSame(firstArea, secondArea);

		verify(areaProvider);
	}

	@Test
	public void testGetNativeSizeReturnsReturnsSameSizeAsCachedProvider() {
		NiftyImage image = createMock(NiftyImage.class);

		AreaProvider areaProvider = createMock(AreaProvider.class);
		expect(areaProvider.getNativeSize(image)).andReturn(new Size(1, 2)).anyTimes();
		replay(areaProvider);

		CachedAreaProvider cachedAreaProvider = new CachedAreaProvider(areaProvider);
		assertEquals(cachedAreaProvider.getNativeSize(image), areaProvider.getNativeSize(image));

		verify(areaProvider);
	}
}
