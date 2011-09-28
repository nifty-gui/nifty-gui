package de.lessvoid.nifty.html;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.builder.PanelBuilder;

public class NiftyBuilderFactoryTablePanelBuilderTest {
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
  public void testCreateTableTagPanelBuilder() {
    panelBuilderMock.childLayoutVertical();
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createTableTagPanelBuilder());
  }

  @Test
  public void testCreateTableRowPanelBuilder() {
    panelBuilderMock.childLayoutHorizontal();
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createTableRowPanelBuilder());
  }

  @Test
  public void testCreateTableDataPanelBuilder() {
    panelBuilderMock.childLayoutVertical();
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createTableDataPanelBuilder());
  }
}
