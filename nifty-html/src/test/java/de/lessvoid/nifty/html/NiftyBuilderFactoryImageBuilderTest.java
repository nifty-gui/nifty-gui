package de.lessvoid.nifty.html;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
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
    imageBuilderMock.filename("src");
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", null, null, null, null, null));
  }

  @Test
  public void testCreateImageBuilderComplete() {
    imageBuilderMock.filename("src");
    imageBuilderMock.width("100");
    imageBuilderMock.height("100");
    imageBuilderMock.backgroundColor("bgcolor");
    imageBuilderMock.align(Align.Center);
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", "middle", "100", "100", "bgcolor", null));
  }

  @Test
  public void testCreateImageBuilderWithPercent() {
    imageBuilderMock.filename("src");
    imageBuilderMock.width("100%");
    imageBuilderMock.height("100%");
    imageBuilderMock.backgroundColor("bgcolor");
    imageBuilderMock.align(Align.Center);
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
    imageBuilderMock.filename("src");
    imageBuilderMock.backgroundColor("bgcolor");
    imageBuilderMock.align(expectedAlign);
    imageBuilderMock.padding("12");
    replay(imageBuilderMock);

    assertEquals(imageBuilderMock, builderFactory.createImageBuilder("src", sourceAlign, null, null, "bgcolor", "12"));
  }
}
