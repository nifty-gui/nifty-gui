package de.lessvoid.nifty.examples.allcontrols;

import de.lessvoid.nifty.Nifty;
//import de.lessvoid.nifty.controls.dropdown.DropDownControl;
//import de.lessvoid.nifty.controls.listbox.ListBoxControl;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class AllControlsDemoStartScreen implements ScreenController, NiftyExample {
  public void bind(final Nifty newNifty, final Screen screen) {
    // FIXME
//    DropDownControl dropDownControl = screen.findControl("dropDown", DropDownControl.class);
//    dropDownControl.addItem("Item 1");
//    dropDownControl.addItem("Item 2");
//    dropDownControl.addItem("Item 3");
//    dropDownControl.addItem("Item 4");
//    dropDownControl.addItem("Item 5");
//    dropDownControl.addItem("Item 6");
//    dropDownControl.setSelectedItemIdx(0);
//
//    ListBoxControl listBoxControl = screen.findControl("listBox", ListBoxControl.class);
//    listBoxControl.addItem("Item 1");
//    listBoxControl.addItem("Item 2");
//    listBoxControl.addItem("Item 3");
//    listBoxControl.addItem("Item 4");
//    listBoxControl.addItem("Item 5");
//    listBoxControl.addItem("Item 6");
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Override
  public String getMainXML() {
    return "allcontrols/allcontrols.xml";
  }

  @Override
  public String getTitle() {
    return "Nifty Standard Controls Demonstation";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
