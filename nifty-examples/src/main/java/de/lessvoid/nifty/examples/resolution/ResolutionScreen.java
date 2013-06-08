package de.lessvoid.nifty.examples.resolution;

import java.util.Collection;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * ScreenController for Hello World Example.
 * @author void
 */
public class ResolutionScreen<T> implements ScreenController, NiftyExample {
  private Nifty nifty;
  private Screen screen;
  private DropDown<T> dropDown;
  private ListBox<String> listBox;
  
  private ResolutionControl<T> resControl;

  public ResolutionScreen(final ResolutionControl<T> newControl) {
    resControl = newControl;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void bind(final Nifty newNifty, final Screen newScreen) {
    this.nifty = newNifty;
    this.screen = newScreen;
    this.dropDown = screen.findNiftyControl("resolutions", DropDown.class);
    this.listBox = screen.findNiftyControl("listBox", ListBox.class);

    // get all DisplayModes from LWJGL and add their descriptions to the DropDown
    fillResolutionDropDown(screen);

    // and make sure the current is selected too
    dropDown.selectItem(resControl.getCurrentResolution());

    listBox.addItem("Test");
    listBox.addItem("TestTestTestTestTestTestTestTestTestTestTestTest");
    listBox.selectItem("Test");
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  @NiftyEventSubscriber(id="resolutions")
  public void onResolution(final String id, final DropDownSelectionChangedEvent<T> event) {
    resControl.setResolution(event.getSelection());
    nifty.resolutionChanged();
  }

  /**
   * Get all LWJGL DisplayModes into the DropDown
   * @param screen
   */
  private void fillResolutionDropDown(final Screen screen) {
    final Collection<T> resolutions = resControl.getResolutions();
    for (T mode : resolutions) {
      dropDown.addItem(mode);
    }
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "resolution/resolution.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Screen Resolution";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    nifty.registerScreenController(this);
  }
}
