package de.lessvoid.nifty.examples.multiplayer;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;

/**
 * @author void
 */
public class StartScreenController implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;
  private int id = 10000;

  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
    addPanel();
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void quit() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  public void addPanel() {
    CustomControlCreator createMultiplayerPanel = new CustomControlCreator(String.valueOf(id++), "multiplayerPanel");
    createMultiplayerPanel.create(nifty, screen, screen.findElementById("box-parent"));
  }

  @NiftyEventSubscriber(pattern = ".*#imageSelect")
  public void onImageSelectSelectionChanged(final String id, @Nonnull final ImageSelectSelectionChangedEvent event) {
    System.out.println("ImageSelect [" + id + "] changed selection to [" + event.getSelectedIndex() + "]");
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "multiplayer/multiplayer.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Multiplayer Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
