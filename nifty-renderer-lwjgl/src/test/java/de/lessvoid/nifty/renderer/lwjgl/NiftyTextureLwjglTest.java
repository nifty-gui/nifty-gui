package de.lessvoid.nifty.renderer.lwjgl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.coregl.CoreFactory;
import de.lessvoid.coregl.CoreTexture2D;
import de.lessvoid.coregl.CoreTexture2D.ColorFormat;
import de.lessvoid.coregl.CoreTexture2D.ResizeFilter;
import de.lessvoid.coregl.CoreTexture2D.Type;

public class NiftyTextureLwjglTest {
  private static final int TEXTURE_WIDTH = 200;
  private static final int TEXTURE_HEIGHT = 100;

  private CoreFactory coreFactoryMock;
  private CoreTexture2D coreTextureMock;

  @Before
  public void before() {
    coreFactoryMock = createMock(CoreFactory.class);
    coreTextureMock = createMock(CoreTexture2D.class);
  }

  @After
  public void after() {
    verify(coreFactoryMock);
    verify(coreTextureMock);
  }

  @Test
  public void testBind() {
    prepareCoreTextureMockBind();
    prepareCoreFactoryMock(coreTextureMock);

    NiftyTextureLwjgl niftyTexture = new NiftyTextureLwjgl(coreFactoryMock, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    niftyTexture.bind();
  }

  @Test
  public void testGetWidth() {
    prepareCoreTextureMockGetWidth();
    prepareCoreFactoryMock(coreTextureMock);

    NiftyTextureLwjgl niftyTexture = new NiftyTextureLwjgl(coreFactoryMock, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    assertEquals(TEXTURE_WIDTH, niftyTexture.getWidth());
  }

  @Test
  public void testGetHeight() {
    prepareCoreTextureMockGetHeight();
    prepareCoreFactoryMock(coreTextureMock);

    NiftyTextureLwjgl niftyTexture = new NiftyTextureLwjgl(coreFactoryMock, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    assertEquals(TEXTURE_HEIGHT, niftyTexture.getHeight());
  }

  private void prepareCoreTextureMockBind() {
    coreTextureMock.bind();
    replay(coreTextureMock);
  }

  private void prepareCoreTextureMockGetWidth() {
    expect(coreTextureMock.getWidth()).andReturn(TEXTURE_WIDTH);
    replay(coreTextureMock);
  }

  private void prepareCoreTextureMockGetHeight() {
    expect(coreTextureMock.getHeight()).andReturn(TEXTURE_HEIGHT);
    replay(coreTextureMock);
  }

  private void prepareCoreFactoryMock(final CoreTexture2D coreTexture) {
    expect(coreFactoryMock.createEmptyTexture(ColorFormat.RGBA, Type.UNSIGNED_BYTE, TEXTURE_WIDTH, TEXTURE_HEIGHT, ResizeFilter.Linear)).andReturn(coreTexture);
    replay(coreFactoryMock);
  }
}
