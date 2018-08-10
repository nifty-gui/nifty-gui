package de.lessvoid.nifty.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyStopwatch;
import de.lessvoid.nifty.controls.dynamic.ScreenCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ScreenBuilder {
  private final ScreenCreator creator;
  @Nonnull
  private final List<LayerBuilder> layerBuilders = new ArrayList<LayerBuilder>();

  public ScreenBuilder(@Nonnull final String id) {
    creator = createScreenCreator(id);
  }

  public ScreenBuilder(final String id, final ScreenController controller) {
    this(id);
    creator.setScreenController(controller);
  }

  public ScreenBuilder controller(final ScreenController controller) {
    creator.setScreenController(controller);
    return this;
  }

  public ScreenBuilder defaultFocusElement(final String defaultFocusElement) {
    creator.setDefaultFocusElement(defaultFocusElement);
    return this;
  }

  public ScreenBuilder inputMapping(final String inputMapping) {
    creator.setInputMapping(inputMapping);
    return this;
  }

  public ScreenBuilder inputMappingPre(final String inputMappingPre) {
    creator.setInputMappingPre(inputMappingPre);
    return this;
  }

  public ScreenBuilder layer(final LayerBuilder layerBuilder) {
    layerBuilders.add(layerBuilder);
    return this;
  }

  @Nonnull
  public Screen build(@Nonnull final Nifty nifty) {
    NiftyStopwatch.start();
    Screen screen = creator.create(nifty);

    Element screenRootElement = screen.getRootElement();
    for (LayerBuilder layerBuilder : layerBuilders) {
      layerBuilder.build(nifty, screen, screenRootElement);
    }

    NiftyStopwatch.stop("ScreenBuilder.build ()");
    return screen;
  }

  ScreenCreator createScreenCreator(@Nonnull final String id) {
    return new ScreenCreator(id);
  }
}
