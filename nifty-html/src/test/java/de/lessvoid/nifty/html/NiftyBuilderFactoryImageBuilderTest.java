package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.builder.ElementBuilder.Align;
import de.lessvoid.nifty.builder.ImageBuilder;

public class NiftyBuilderFactoryImageBuilderTest {
  private ImageBuilder imageBuilderMock;
  private NiftyBuilderFactory builderFactory;

  @Before
  public void before() {
    imageBuilderMock = createMock(ImageBuilder.class);

    builderFactory = new NiftyBuilderFactory() {
      @Override
      public ImageBuilder createImageBuilder() {
        return imageBuilderMock;
      }
    };
  }

  @After
  public void after() {
    verify(imageBuilderMock);
  }

  @Test
  public void testCreateImageBuilderMinimal() {
    expect(imageBuilderMock.filename("src")).andReturn(imageBuilderMock);
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", null, null, null, null, null));
  }

  @Test
  public void testCreateImageBuilderComplete() {
    expect(imageBuilderMock.filename("src")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.width("100")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.height("100")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.backgroundColor("bgcolor")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.align(Align.Center)).andReturn(imageBuilderMock);
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", "middle", "100", "100", "bgcolor", null));
  }

  @Test
  public void testCreateImageBuilderWithPercent() {
    expect(imageBuilderMock.filename("src")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.width("100%")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.height("100%")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.backgroundColor("bgcolor")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.align(Align.Center)).andReturn(imageBuilderMock);
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", "middle", "100%", "100%", "bgcolor", null));
  }

  @Test
  public void testCreateImageBuilderAlignLeft() {
    performAlignTest(Align.Left, "left");
  }

  @Test
  public void testCreateImageBuilderAlignRight() {
    performAlignTest(Align.Right, "right");
  }

  @Test
  public void testCreateImageBuilderAlignMiddle() {
    performAlignTest(Align.Center, "middle");
  }

  private void performAlignTest(final Align expectedAlign, final String sourceAlign) {
    expect(imageBuilderMock.filename("src")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.backgroundColor("bgcolor")).andReturn(imageBuilderMock);
    expect(imageBuilderMock.align(expectedAlign)).andReturn(imageBuilderMock);
    expect(imageBuilderMock.padding("12")).andReturn(imageBuilderMock);
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", sourceAlign, null, null, "bgcolor", "12"));
  }
}
