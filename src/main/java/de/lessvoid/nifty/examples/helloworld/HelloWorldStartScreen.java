package de.lessvoid.nifty.examples.helloworld;

import java.util.Date;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class HelloWorldStartScreen implements ScreenController {

  /** nifty instance. */
  private Nifty nifty;
  private Screen screen;

  /**
   * Bind this ScreenController to a screen.
   * @param newNifty nifty
   * @param newScreen screen
   */
  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
  }

  @NiftyEventSubscriber(id="add")
  public void onAddClicked(final String id, final ButtonClickedEvent event) {
    ListBox listbox = screen.findElementByName("listbox").getNiftyControl(ListBox.class);
    listbox.addItem(new Date().getTime() + "ewroiwueroiuweoiruoiweuroiwedkfhdkhdfkjghdkfjghkdjfhgkjdhfgkjdhfkjghdoiewuroiwueroiuweroiuweoiruwoeiruowieuroiweuoiwuer");
    screen.findElementByName("panel").hide();
    screen.findElementByName("panel").show();

  }

  @NiftyEventSubscriber(id="remove")
  public void onRemoveClicked(final String id, final ButtonClickedEvent event) {
    ListBox listbox = screen.findElementByName("listbox").getNiftyControl(ListBox.class);
    listbox.removeItemByIndex(0);
  }

  /**
   * on start screen interactive.
   */
  public void onStartScreen() {
    System.out.println(screen.debugOutput());

    ListBox listbox = screen.findElementByName("listbox").getNiftyControl(ListBox.class);
    listbox.addItem(new Object());

  }

  @Override
  public void onEndScreen() {
  }
}
