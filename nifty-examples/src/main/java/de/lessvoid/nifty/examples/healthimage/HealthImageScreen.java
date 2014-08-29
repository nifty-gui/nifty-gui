package de.lessvoid.nifty.examples.healthimage;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.render.image.ImageModeHelper;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * @author void
 */
public class HealthImageScreen implements ScreenController, NiftyExample {
  private Nifty nifty;
  private NiftyImage healthImage;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    this.nifty = newNifty;
    healthImage = newScreen.findElementById("health").getRenderer(ImageRenderer.class).getImage();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "healthimage/healthimage.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Health Image Example";
  }

  @Override
  public void prepareStart(@Nonnull Nifty nifty) {
  }

  @NiftyEventSubscriber(id="value")
  public void valueChanged(final String id, final SliderChangedEvent event) {
    // yep, a bit brute force ;) 
    int value = (int)event.getValue();
    String parameter = "subImageDirect:0," + (150 - value) + ",150," + value;
    String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(parameter);
    String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(parameter);
    healthImage.setImageMode(ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty));
  }
}
