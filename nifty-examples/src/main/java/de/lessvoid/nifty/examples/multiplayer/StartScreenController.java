package de.lessvoid.nifty.examples.multiplayer;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * StartScreenController for Multiplayer.
 * @author void
 */
public class StartScreenController implements ScreenController {
  private Nifty nifty;
  private Screen screen;
  private int id = 10000;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
    addPanel();
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public void quit() {
    nifty.fromXml("all/intro.xml", "menu");
  }

  public void addPanel() {
    CustomControlCreator createMultiplayerPanel = new CustomControlCreator(String.valueOf(id++), "multiplayerPanel");
    createMultiplayerPanel.create(nifty, screen, screen.findElementByName("box-parent"));
  }

  @NiftyEventSubscriber(pattern=".*#imageSelect")
  public void onImageSelectSelectionChanged(final String id, final ImageSelectSelectionChangedEvent event) {
    System.out.println("ImageSelect [" + id + "] changed selection to [" + event.getSelectedIndex() + "]");
  }
}
