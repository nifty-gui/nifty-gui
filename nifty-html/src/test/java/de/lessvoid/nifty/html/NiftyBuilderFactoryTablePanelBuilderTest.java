package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.builder.EffectBuilder;
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
  public void testCreateTableTagPanelBuilderNoAttributes() {
    panelBuilderMock.childLayoutVertical();
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createTableTagPanelBuilder(null, null, null, null));
  }

  @Test
  public void testCreateTableTagPanelBuilderFull() {
    panelBuilderMock.childLayoutVertical();
    assertGeneralAttributes(panelBuilderMock);
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createTableTagPanelBuilder("20", "#ff00ff", "2", "#00ff00"));
  }

  @Test
  public void testCreateTableRowPanelBuilderNoAttributes() {
    panelBuilderMock.childLayoutHorizontal();
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createTableRowPanelBuilder(null, null, null, null));
  }

  @Test
  public void testCreateTableRowPanelBuilderFull() {
    panelBuilderMock.childLayoutHorizontal();
    assertGeneralAttributes(panelBuilderMock);
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createTableRowPanelBuilder("20", "#ff00ff", "2", "#00ff00"));
  }

  @Test
  public void testCreateTableDataPanelBuilderNoAttributes() {
    panelBuilderMock.childLayoutVertical();
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createTableDataPanelBuilder(null, null, null, null));
  }

  @Test
  public void testCreateTableDataPanelBuilderFull() {
    panelBuilderMock.childLayoutVertical();
    assertGeneralAttributes(panelBuilderMock);
    replay(panelBuilderMock);

    assertEquals(panelBuilderMock, builderFactory.createTableDataPanelBuilder("20", "#ff00ff", "2", "#00ff00"));
  }

  private void assertGeneralAttributes(final PanelBuilder panelBuilder) {
    panelBuilder.width("20");
    panelBuilder.backgroundColor("#ff00ff");
    panelBuilder.onActiveEffect(isA(EffectBuilder.class)); // can't test the actual effect values unfortunatly
  }
}
