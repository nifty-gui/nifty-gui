package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.ParagraphTag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.elements.Element;

public class NiftyVisitorTest {
  private NiftyVisitor visitor;
  private NiftyBuilderFactory builderFactoryMock;
  private Element element;

  @Before
  public void before() {
    builderFactoryMock = createMock(NiftyBuilderFactory.class);
    visitor = new NiftyVisitor(null, builderFactoryMock);
    element = createMock(Element.class);
    replay(element);
  }

  @After
  public void after() {
    verify(builderFactoryMock);
    verify(element);
  }

  @Test
  public void simpleBodyTagSuccess() throws Exception {
    PanelBuilder mainPanelBuilderMock = createMock(PanelBuilder.class);
    expectMainPanelAttributes(mainPanelBuilderMock);
    replayMainPanel(mainPanelBuilderMock);

    expect(builderFactoryMock.createPanelBuilder()).andReturn(mainPanelBuilderMock);
    replay(builderFactoryMock);

    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);
    visitor.visitEndTag(bodyTag);

    assertEquals(element, visitor.build(null, null, null));
    verify(mainPanelBuilderMock);
  }

  @Test
  public void paragraphTagRequiresBody() throws Exception {
    replay(builderFactoryMock);

    ParagraphTag p = new ParagraphTag();
    visitor.visitTag(p);
    visitor.visitEndTag(p);

    try {
      visitor.build(null, null, null);
    } catch (Exception e) {
      assertEquals("This looks like HTML with a missing <body> tag\n", e.getMessage());
    }
  }

  @Test
  public void paragraphSuccess() throws Exception {
    PanelBuilder mainPanel = createMock(PanelBuilder.class);
    PanelBuilder paragraphPanel = createMock(PanelBuilder.class);

    // main panel
    expectMainPanelAttributes(mainPanel);
    mainPanel.panel(paragraphPanel);
    replayMainPanel(mainPanel);

    // paragraph panel
    expectParagraphAttributes(paragraphPanel);
    replay(paragraphPanel);

    // builder factory mock
    expect(builderFactoryMock.createPanelBuilder()).andReturn(mainPanel);
    expect(builderFactoryMock.createPanelBuilder()).andReturn(paragraphPanel);
    replay(builderFactoryMock);

    // create body
    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);
    
      // create paragraph
      ParagraphTag p = new ParagraphTag();
      visitor.visitTag(p);
      visitor.visitEndTag(p);

    visitor.visitEndTag(bodyTag);

    assertEquals(element, visitor.build(null, null, null));
    verify(mainPanel);
    verify(paragraphPanel);
  }

  private void expectMainPanelAttributes(final PanelBuilder panelBuilderMock) {
    panelBuilderMock.width("100%");
    panelBuilderMock.height("100%");
    panelBuilderMock.childLayoutVertical();
  }

  private void expectParagraphAttributes(final PanelBuilder panelBuilderMock) {
    panelBuilderMock.width("100%");
    panelBuilderMock.childLayoutVertical();
    expect(panelBuilderMock.getElementBuilders()).andReturn(new ArrayList<ElementBuilder>());
    panelBuilderMock.height("5");
  }
  
  private void replayMainPanel(final PanelBuilder panelBuilderMock) {
    expect(panelBuilderMock.build(null, null, null)).andReturn(element);
    replay(panelBuilderMock);
  }
}
