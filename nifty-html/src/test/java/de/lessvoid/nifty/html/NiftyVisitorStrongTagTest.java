package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
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

public class NiftyVisitorStrongTagTest {
  private NiftyVisitor visitor;
  private Nifty niftyMock;
  private NiftyBuilderFactory builderFactoryMock;
  private RenderFont defaultFont;

  @Before
  public void before() {
    builderFactoryMock = createMock(NiftyBuilderFactory.class);
    defaultFont = createMock(RenderFont.class);

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
  public void simpleTextParagraphSuccess() throws Exception {
    replay(defaultFont);

    PanelBuilder bodyPanelBuilder = new PanelBuilder();
    PanelBuilder paragraphPanelBuilder = new PanelBuilder();
    TextBuilder textBuilder = new TextBuilder();

    expect(builderFactoryMock.createBodyPanelBuilder()).andReturn(bodyPanelBuilder);
    expect(builderFactoryMock.createParagraphPanelBuilder()).andReturn(paragraphPanelBuilder);
    expect(builderFactoryMock.createTextBuilder("text node", "aurulent-sans-bold-16.fnt", null)).andReturn(textBuilder);
    replay(builderFactoryMock);

    // create body
    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);

      // create paragraph
      ParagraphTag p = new ParagraphTag();
      visitor.visitTag(p);

        // create strong node
        TagNode strongNode = new TagNode();
        strongNode.setTagName("strong");
        visitor.visitTag(strongNode);
        
          // create text node
          Text textNode = new TextNode("text node");
          visitor.visitStringNode(textNode);

        visitor.visitEndTag(strongNode);

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
}
