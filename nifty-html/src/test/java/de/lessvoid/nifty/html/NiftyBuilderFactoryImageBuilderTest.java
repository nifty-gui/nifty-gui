package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.builder.ElementBuilder.Align;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.render.NiftyImage;

public class NiftyBuilderFactoryImageBuilderTest {
  private ImageBuilder imageBuilderMock;
  private NiftyBuilderFactory builderFactory;
  private NiftyImage image;

  @Before
  public void before() {
    imageBuilderMock = createMock(ImageBuilder.class);

    builderFactory = new NiftyBuilderFactory() {
      @Override
      public ImageBuilder createImageBuilder() {
        return imageBuilderMock;
      }
    };

    image = createMock(NiftyImage.class);
  }

  @After
  public void after() {
    verify(imageBuilderMock);
    verify(image);
  }

  @Test
  public void testCreateImageBuilderMinimal() {
    replay(image);

    imageBuilderMock.filename("src");
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder(image, "src", null, null, null, null, null));
  }

  @Test
  public void testCreateImageBuilderComplete() {
    expect(image.getWidth()).andReturn(100);
    expect(image.getHeight()).andReturn(100);
    replay(image);

    imageBuilderMock.filename("src");
    imageBuilderMock.width("100");
    imageBuilderMock.height("100");
    imageBuilderMock.backgroundColor("bgcolor");
    imageBuilderMock.align(Align.Center);
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder(image, "src", "center", "100", "100", "bgcolor", null));
  }

  @Test
  public void testCreateImageBuilderWithPercent() {
    expect(image.getWidth()).andReturn(100);
    expect(image.getHeight()).andReturn(100);
    replay(image);

    imageBuilderMock.filename("src");
    imageBuilderMock.width("10");
    imageBuilderMock.height("50");
    imageBuilderMock.backgroundColor("bgcolor");
    imageBuilderMock.align(Align.Center);
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder(image, "src", "center", "10%", "50%", "bgcolor", null));
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
  public void testCreateImageBuilderAlignCenter() {
    performAlignTest(Align.Center, "center");
  }

  @Test
  public void testCreateImageBuilderAlignMiddle() {
    performAlignTest(Align.Center, "middle");
  }

  private void performAlignTest(final Align expectedAlign, final String sourceAlign) {
    replay(image);

    imageBuilderMock.filename("src");
    imageBuilderMock.backgroundColor("bgcolor");
    imageBuilderMock.align(expectedAlign);
    imageBuilderMock.padding("12");
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder(image, "src", sourceAlign, null, null, "bgcolor", "12"));
  }
}
