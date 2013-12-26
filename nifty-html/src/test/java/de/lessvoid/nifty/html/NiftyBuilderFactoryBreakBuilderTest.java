package de.lessvoid.nifty.html;

import de.lessvoid.nifty.builder.PanelBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class NiftyBuilderFactoryBreakBuilderTest {
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
  public void testCreateBreakBuilderMinimal() {
    panelBuilderMock.height("42");
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createBreakPanelBuilder("42"));
  }
}
