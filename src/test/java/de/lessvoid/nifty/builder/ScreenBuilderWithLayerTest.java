package de.lessvoid.nifty.builder;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.ScreenCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class ScreenBuilderWithLayerTest {
  private Nifty niftyMock;
  private ScreenCreator screenCreator;
  private Screen screen;
  private Element screenRootElement;
  private Element layerElement;

  @Before
  public void before() {
    screenRootElement = createMock(Element.class);
    replay(screenRootElement);

    layerElement = createMock(Element.class);
    replay(layerElement);

    niftyMock = createMock(Nifty.class);
    replay(niftyMock);

    screen = createMock(Screen.class);
    expect(screen.getRootElement()).andReturn(screenRootElement);
    replay(screen);

    screenCreator = createMock(ScreenCreator.class);
    expect(screenCreator.create(niftyMock)).andReturn(screen);
    replay(screenCreator);
  }

  @After
  public void after() {
    verify(niftyMock);
    verify(screenCreator);
    verify(screen);
    verify(screenRootElement);
    verify(layerElement);
  }

  @Test
  public void testWithOneLayer() {
    LayerBuilder layerBuilder = createLayerBuilder();

    ScreenBuilder screenBuilder = new ScreenBuilderCreatorMock();
    screenBuilder.layer(layerBuilder);
    assertEquals(screen, screenBuilder.build(niftyMock));

    verify(layerBuilder);
  }

  @Test
  public void testWithTwoLayers() {
    LayerBuilder layerBuilder1 = createLayerBuilder();
    LayerBuilder layerBuilder2 = createLayerBuilder();

    ScreenBuilder screenBuilder = new ScreenBuilderCreatorMock();
    screenBuilder.layer(layerBuilder1);
    screenBuilder.layer(layerBuilder2);
    assertEquals(screen, screenBuilder.build(niftyMock));

    verify(layerBuilder1);
    verify(layerBuilder2);
  }

  @Test
  public void testWithOneLayerParentAlreadySet() {
    LayerBuilder layerBuilder = createLayerBuilderWithParent();

    ScreenBuilder screenBuilder = new ScreenBuilderCreatorMock();
    screenBuilder.layer(layerBuilder);
    assertEquals(screen, screenBuilder.build(niftyMock));

    verify(layerBuilder);
  }

  private LayerBuilder createLayerBuilder() {
    LayerBuilder layerBuilder = createMock(LayerBuilder.class);
    layerBuilder.screen(screen);
    expect(layerBuilder.hasParent()).andReturn(false);
    layerBuilder.parent(screenRootElement);
    expect(layerBuilder.build(niftyMock, screen, screenRootElement)).andReturn(layerElement);
    replay(layerBuilder);
    return layerBuilder;
  }

  private LayerBuilder createLayerBuilderWithParent() {
    LayerBuilder layerBuilder = createMock(LayerBuilder.class);
    layerBuilder.screen(screen);
    expect(layerBuilder.hasParent()).andReturn(true);
    expect(layerBuilder.build(niftyMock, screen, screenRootElement)).andReturn(layerElement);
    replay(layerBuilder);
    return layerBuilder;
  }

  private class ScreenBuilderCreatorMock extends ScreenBuilder {
    public ScreenBuilderCreatorMock() {
      super("myid");
    }

    ScreenCreator createScreenCreator(final String id) {
      return screenCreator;
    }
  }
}
