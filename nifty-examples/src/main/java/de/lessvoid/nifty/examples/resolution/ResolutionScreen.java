package de.lessvoid.nifty.examples.resolution;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * ScreenController for Hello World Example.
 *
 * @author void
 */
public class ResolutionScreen<T> implements ScreenController, NiftyExample {
  private Nifty nifty;
  @Nullable
  private DropDown<T> dropDown;

  private ResolutionControl<T> resControl;

  public ResolutionScreen(final ResolutionControl<T> newControl) {
    resControl = newControl;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void bind(@Nonnull final Nifty newNifty, @Nonnull final Screen screen) {
    this.nifty = newNifty;
    this.dropDown = screen.findNiftyControl("resolutions", DropDown.class);
    ListBox<String> listBox = screen.findNiftyControl("listBox", ListBox.class);

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

  @NiftyEventSubscriber(id = "resolutions")
  public void onResolution(final String id, @Nonnull final DropDownSelectionChangedEvent<T> event) {
    resControl.setResolution(event.getSelection());
    nifty.resolutionChanged();
  }

  /**
   * Get all LWJGL DisplayModes into the DropDown
   *
   * @param screen
   */
  private void fillResolutionDropDown(final Screen screen) {
    final Collection<T> resolutions = resControl.getResolutions();
    for (T mode : resolutions) {
      dropDown.addItem(mode);
    }
  }

  @Nonnull
  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nonnull
  @Override
  public String getMainXML() {
    return "resolution/resolution.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Nifty Screen Resolution";
  }

  @Override
  public void prepareStart(@Nonnull Nifty nifty) {
    nifty.registerScreenController(this);
  }
}
