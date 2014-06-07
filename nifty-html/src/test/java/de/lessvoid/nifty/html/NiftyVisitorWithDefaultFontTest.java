package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.htmlparser.Text;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.ParagraphTag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.spi.render.RenderFont;

public class NiftyVisitorWithDefaultFontTest {
  private NiftyVisitor visitor;
  private Nifty niftyMock;
  private NiftyBuilderFactory builderFactoryMock;
  private RenderFont defaultFont;

  @Before
  public void before() {
    builderFactoryMock = createMock(NiftyBuilderFactory.class);
    defaultFont = createMock(RenderFont.class);
    expect(defaultFont.getHeight()).andReturn(12);
    replay(defaultFont);

    niftyMock = createMock(Nifty.class);
    expect(niftyMock.createFont("aurulent-sans-16.fnt")).andReturn(defaultFont);
    replay(niftyMock);

    visitor = new NiftyVisitor(niftyMock, builderFactoryMock, "aurulent-sans-16.fnt", "aurulent-sans-bold-16.fnt");
  }

  @After
  public void after() {
    verify(builderFactoryMock);
    verify(defaultFont);
    verify(niftyMock);
  }

  @Test
  public void emptyParagraphSuccess() throws Exception {

    PanelBuilder bodyPanelBuilder = new PanelBuilder();
    PanelBuilder paragraphPanelBuilder = new PanelBuilder();

    expect(builderFactoryMock.createBodyPanelBuilder()).andReturn(bodyPanelBuilder);
    expect(builderFactoryMock.createParagraphPanelBuilder()).andReturn(paragraphPanelBuilder);
    replay(builderFactoryMock);

    // create body
    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);

      // create paragraph
      ParagraphTag p = new ParagraphTag();
      visitor.visitTag(p);
      visitor.visitEndTag(p);

    visitor.visitEndTag(bodyTag);

    ElementBuilder builder = visitor.builder();
    assertEquals(bodyPanelBuilder, builder);
    assertEquals(1, builder.getElementBuilders().size());
    assertEquals(paragraphPanelBuilder, builder.getElementBuilders().get(0));

    // empty paragraph (one without any child elements) gets a default height
    assertEquals("12", paragraphPanelBuilder.get("height"));
  }

  @Test
  public void simpleTextParagraphSuccess() throws Exception {
    PanelBuilder bodyPanelBuilder = new PanelBuilder();
    PanelBuilder paragraphPanelBuilder = new PanelBuilder();
    TextBuilder textBuilder = new TextBuilder();

    expect(builderFactoryMock.createBodyPanelBuilder()).andReturn(bodyPanelBuilder);
    expect(builderFactoryMock.createParagraphPanelBuilder()).andReturn(paragraphPanelBuilder);
    expect(builderFactoryMock.createTextBuilder("text node", "aurulent-sans-16.fnt", null)).andReturn(textBuilder);
    replay(builderFactoryMock);

    // create body
    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);

      // create paragraph
      ParagraphTag p = new ParagraphTag();
      visitor.visitTag(p);

        // create text node
        Text textNode = new TextNode("text node");
        visitor.visitStringNode(textNode);

      // end paragraph
      visitor.visitEndTag(p);

    // end body
    visitor.visitEndTag(bodyTag);

    ElementBuilder builder = visitor.builder();
    assertEquals(bodyPanelBuilder, builder);
    assertEquals(1, builder.getElementBuilders().size());
    assertEquals(paragraphPanelBuilder, builder.getElementBuilders().get(0));

    assertEquals(1, paragraphPanelBuilder.getElementBuilders().size());
    assertEquals(textBuilder, paragraphPanelBuilder.getElementBuilders().get(0));
  }

  @Test
  public void simpleBodyWithBR() throws Exception {
    PanelBuilder bodyPanelBuilder = new PanelBuilder();
    PanelBuilder panelBuilder = new PanelBuilder();

    expect(builderFactoryMock.createBodyPanelBuilder()).andReturn(bodyPanelBuilder);
    expect(builderFactoryMock.createBreakPanelBuilder("12")).andReturn(panelBuilder);
    replay(builderFactoryMock);

    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);

      // add BR
      TagNode breakTag = new TagNode();
      breakTag.setTagName("br");
      visitor.visitTag(breakTag);
      visitor.visitEndTag(breakTag);

    // close body
    visitor.visitEndTag(bodyTag);

    assertEquals(bodyPanelBuilder, visitor.builder());
    assertEquals(1, bodyPanelBuilder.getElementBuilders().size());
    assertEquals(panelBuilder, bodyPanelBuilder.getElementBuilders().get(0));
  }
}
