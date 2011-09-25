package de.lessvoid.nifty.html;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.ElementBuilder.Align;

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
    imageBuilderMock.filename("src");
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", null, null, null, null));
  }

  @Test
  public void testCreateImageBuilderComplete() {
    imageBuilderMock.filename("src");
    imageBuilderMock.width("width");
    imageBuilderMock.height("height");
    imageBuilderMock.backgroundColor("bgcolor");
    imageBuilderMock.align(Align.Center);
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", "center", "width", "height", "bgcolor"));
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
    imageBuilderMock.filename("src");
    imageBuilderMock.width("width");
    imageBuilderMock.height("height");
    imageBuilderMock.backgroundColor("bgcolor");
    imageBuilderMock.align(expectedAlign);
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", sourceAlign, "width", "height", "bgcolor"));
  }

}
