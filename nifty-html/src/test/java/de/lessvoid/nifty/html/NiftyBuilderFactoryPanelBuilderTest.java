package de.lessvoid.nifty.html;

import de.lessvoid.nifty.builder.PanelBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertEquals;

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
    panelBuilderMock.width("100%");
    panelBuilderMock.height("100%");
    panelBuilderMock.childLayoutVertical();
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createBodyPanelBuilder());
  }

  @Test
  public void testCreateParagraphBuilder() {
    panelBuilderMock.width("100%");
    panelBuilderMock.childLayoutVertical();
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createParagraphPanelBuilder());
  }
}
