package de.lessvoid.nifty.builder;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.controls.dynamic.ScreenCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ScreenBuilder {
  private ScreenCreator creator;
  private List<LayerBuilder> layerBuilders = new ArrayList<LayerBuilder>();

  public ScreenBuilder(final String id) {
    creator = createScreenCreator(id);
  }

  public ScreenBuilder(final String id, final ScreenController controller) {
    this(id);
    creator.setScreenController(controller);
  }

  public void controller(final ScreenController controller) {
    creator.setScreenController(controller);
  }

  public void defaultFocusElement(final String defaultFocusElement) {
    creator.setDefaultFocusElement(defaultFocusElement);
  }

  public void inputMapping(final String inputMapping) {
    creator.setInputMapping(inputMapping);
  }

  public void layer(final LayerBuilder layerBuilder) {
    layerBuilders.add(layerBuilder);
  }

  public Screen build(final Nifty nifty) {
    NiftyStopwatch.start();
    Screen screen = creator.create(nifty);

    Element screenRootElement = screen.getRootElement();
    for (LayerBuilder layerBuilder : layerBuilders) {
      layerBuilder.build(nifty, screen, screenRootElement);
    }

    NiftyStopwatch.stop("ScreenBuilder.build ()");
    return screen;
  }

  ScreenCreator createScreenCreator(final String id) {
    return new ScreenCreator(id);
  }
}
