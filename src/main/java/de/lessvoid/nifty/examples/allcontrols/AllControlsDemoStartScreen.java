package de.lessvoid.nifty.examples.allcontrols;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dropdown.controller.DropDownControl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class AllControlsDemoStartScreen implements ScreenController {
  public void bind(final Nifty newNifty, final Screen screen) {
    DropDownControl dropDownControl = screen.findControl("dropDown", DropDownControl.class);
    dropDownControl.addItem("Item 1");
    dropDownControl.addItem("Item 2");
    dropDownControl.addItem("Item 3");
    dropDownControl.setSelectedItemIdx(0);
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }
}
