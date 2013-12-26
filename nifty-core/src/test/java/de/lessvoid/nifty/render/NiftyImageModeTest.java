package de.lessvoid.nifty.render;

import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

public class NiftyImageModeTest {
  @Test
  public void spriteModeUpperLeft() {
    RenderImage renderImage = createMock(RenderImage.class);
    expect(renderImage.getWidth()).andReturn(256);
    expect(renderImage.getHeight()).andReturn(256);
    replay(renderImage);

    RenderDevice renderDevice = createMock(RenderDevice.class);
    renderDevice.renderImage(renderImage, 100, 100, 16, 16, 0, 0, 16, 16, Color.BLACK, 1.0f, 108, 108);
    replay(renderDevice);

    NiftyImageMode spriteMode = NiftyImageMode.valueOf("sprite:16,16,0");
    spriteMode.render(renderDevice, renderImage, 100, 100, 16, 16, Color.BLACK, 1.0f);

    verify(renderDevice);
  }

  @Test
  public void spriteModeSecondRowLeft() {
    RenderImage renderImage = createMock(RenderImage.class);
    expect(renderImage.getWidth()).andReturn(256);
    expect(renderImage.getHeight()).andReturn(256);
    replay(renderImage);

    RenderDevice renderDevice = createMock(RenderDevice.class);
    renderDevice.renderImage(renderImage, 100, 100, 16, 16, 0, 16, 16, 16, Color.BLACK, 1.0f, 108, 108);
    replay(renderDevice);

    NiftyImageMode spriteMode = NiftyImageMode.valueOf("sprite:16,16,16");
    spriteMode.render(renderDevice, renderImage, 100, 100, 16, 16, Color.BLACK, 1.0f);

    verify(renderDevice);
  }
}
