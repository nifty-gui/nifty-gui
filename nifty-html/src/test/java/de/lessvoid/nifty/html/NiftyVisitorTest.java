package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.ParagraphTag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;

public class NiftyVisitorTest {
  private NiftyVisitor visitor;
  private NiftyBuilderFactory builderFactoryMock;

  @Before
  public void before() {
    builderFactoryMock = createMock(NiftyBuilderFactory.class);
    visitor = new NiftyVisitor(null, builderFactoryMock, null);
  }

  @After
  public void after() {
    verify(builderFactoryMock);
  }

  @Test
  public void simpleBodyTagSuccess() throws Exception {
    PanelBuilder bodyPanelBuilder = new PanelBuilder();

    expect(builderFactoryMock.createBodyPanelBuilder()).andReturn(bodyPanelBuilder);
    replay(builderFactoryMock);

    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);
    visitor.visitEndTag(bodyTag);

    assertEquals(bodyPanelBuilder, visitor.builder());
    assertTrue(bodyPanelBuilder.getElementBuilders().isEmpty());
  }

  @Test
  public void simpleBodyWithBasicImageSuccess() throws Exception {
    PanelBuilder bodyPanelBuilder = new PanelBuilder();
    ImageBuilder imageBuilder = new ImageBuilder();

    expect(builderFactoryMock.createBodyPanelBuilder()).andReturn(bodyPanelBuilder);
    expect(builderFactoryMock.createImageBuilder("src", null, null, null, null)).andReturn(imageBuilder);
    replay(builderFactoryMock);

    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);

      // add image
      ImageTag imageTag = new ImageTag();
      imageTag.setAttribute("src", "src");
      visitor.visitTag(imageTag);
      visitor.visitEndTag(imageTag);

    // close body
    visitor.visitEndTag(bodyTag);

    assertEquals(bodyPanelBuilder, visitor.builder());
    assertEquals(1, bodyPanelBuilder.getElementBuilders().size());
    assertEquals(imageBuilder, bodyPanelBuilder.getElementBuilders().get(0));
  }

  @Test
  public void paragraphTagRequiresBody() throws Exception {
    replay(builderFactoryMock);

    ParagraphTag p = new ParagraphTag();
    visitor.visitTag(p);
    visitor.visitEndTag(p);

    try {
      visitor.builder();
    } catch (Exception e) {
      assertEquals("This looks like HTML with a missing <body> tag\n", e.getMessage());
    }
  }
}
