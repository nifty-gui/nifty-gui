package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.ImageTag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;

public class NiftyVisitorImageTest {
  private NiftyVisitor visitor;
  private NiftyBuilderFactory builderFactoryMock;
  private NiftyImage image;
  private NiftyRenderEngine renderEngine;
  private Nifty nifty;

  @Before
  public void before() {
    builderFactoryMock = createMock(NiftyBuilderFactory.class);
    image = createMock(NiftyImage.class);
    replay(image);
    renderEngine = createMock(NiftyRenderEngine.class);
    nifty = createMock(Nifty.class);
    expect(nifty.getRenderEngine()).andReturn(renderEngine);
    replay(nifty);
    visitor = new NiftyVisitor(nifty, builderFactoryMock, null);
  }

  @After
  public void after() {
    verify(builderFactoryMock);
    verify(image);
    verify(renderEngine);
    verify(nifty);
  }

  @Test
  public void simpleBodyWithBasicImageSuccess() throws Exception {
    expect(renderEngine.createImage("src", false)).andReturn(image);
    replay(renderEngine);

    PanelBuilder bodyPanelBuilder = new PanelBuilder();
    ImageBuilder imageBuilder = new ImageBuilder();

    expect(builderFactoryMock.createBodyPanelBuilder()).andReturn(bodyPanelBuilder);
    expect(builderFactoryMock.createImageBuilder(eq("src"), (String)isNull(), (String)isNull(), (String)isNull(), (String)isNull(), (String)isNull())).andReturn(imageBuilder);
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
}
