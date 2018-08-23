package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.builder.PanelBuilder;

public class NiftyBuilderFactoryPanelBuilderTest {
  private PanelBuilder panelBuilderMock;
  private NiftyBuilderFactory builderFactory;

  @Before
  public void before() {
    panelBuilderMock = createMock(PanelBuilder.class);

    builderFactory = new NiftyBuilderFactory() {
      @Override
      public PanelBuilder createPanelBuilder() {
        return panelBuilderMock;
      }
    };
  }

  @After
  public void after() {
    verify(panelBuilderMock);
  }

  @Test
  public void testCreateBodyPanelBuilder() {
    expect(panelBuilderMock.width("100%")).andReturn(panelBuilderMock);
    expect(panelBuilderMock.height("100%")).andReturn(panelBuilderMock);
    expect(panelBuilderMock.childLayoutVertical()).andReturn(panelBuilderMock);
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createBodyPanelBuilder());
  }

  @Test
  public void testCreateParagraphBuilder() {
    expect(panelBuilderMock.width("100%")).andReturn(panelBuilderMock);
    expect(panelBuilderMock.childLayoutVertical()).andReturn(panelBuilderMock);
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createParagraphPanelBuilder());
  }
}
