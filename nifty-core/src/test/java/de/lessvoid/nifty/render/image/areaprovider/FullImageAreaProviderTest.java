package de.lessvoid.nifty.render.image.areaprovider;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class FullImageAreaProviderTest {

  @Test(expected = IllegalArgumentException.class)
  public void testSetParametersThrowsIllegalArgumentExceptionWithParameters() {
    FullImageAreaProvider areaProvider = new FullImageAreaProvider();
    areaProvider.setParameters("");
  }

  @Test
  public void testGetSourceAreaReturnsAZeroedOriginAndImageSizedArea() {
    RenderImage image = createMock(RenderImage.class);
    expect(image.getWidth()).andReturn(1);
    expect(image.getHeight()).andReturn(2);
    replay(image);

    FullImageAreaProvider areaProvider = new FullImageAreaProvider();
    assertEquals(new Box(0, 0, 1, 2), areaProvider.getSourceArea(image));
  }

  @Test
  public void testGetNativeSizeReturnsImageSize() {
    NiftyImage image = createMock(NiftyImage.class);
    expect(image.getWidth()).andReturn(1);
    expect(image.getHeight()).andReturn(2);
    replay(image);

    FullImageAreaProvider areaProvider = new FullImageAreaProvider();
    assertEquals(areaProvider.getNativeSize(image), new Size(1, 2));

    verify(image);
  }
}
