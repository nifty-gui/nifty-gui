package de.lessvoid.nifty.examples.controls;

import javax.swing.Scrollable;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dropdown.DropDownControl;
import de.lessvoid.nifty.controls.dropdown.DropDownControlNotify;
import de.lessvoid.nifty.controls.scrollbar.GeneralScrollbar;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ControlsDemoStartScreen implements ScreenController, DropDownControlNotify {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;
    DropDownControl dropDown1 = findDropDownControl("dropDown1");
    if (dropDown1 != null) {
      dropDown1.addNotify(this);
      dropDown1.addItem("Nifty GUI");
      dropDown1.addItem("Slick2d");
      dropDown1.addItem("Lwjgl");
      dropDown1.setSelectedItemIdx(0);
    }

    DropDownControl dropDown2 = findDropDownControl("dropDown2");
    if (dropDown2 != null) {
      dropDown2.addNotify(this);
      dropDown2.addItem("rocks!");
      dropDown2.addItem("rules!");
      dropDown2.addItem("kicks ass!");
      dropDown2.addItem("is awesome!");
      dropDown2.addItem("shizzles :D");
      dropDown2.setSelectedItem("rocks!");
    }

    GeneralScrollbar scrollbar = screen.findControl("scrollbar", GeneralScrollbar.class);
    if (scrollbar != null) {
    }
  }

  private DropDownControl findDropDownControl(final String id) {
    DropDownControl dropDown1 = screen.findControl(id, DropDownControl.class);
    return dropDown1;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  public void back() {
    DropDownControl dropDown1 = findDropDownControl("dropDown1");
    System.out.println(dropDown1.getSelectedItemIdx() + ":" + dropDown1.getSelectedItem());
    DropDownControl dropDown2 = findDropDownControl("dropDown2");
    System.out.println(dropDown2.getSelectedItemIdx() + ":" + dropDown2.getSelectedItem());
    nifty.fromXml("all/intro.xml", "menu");
  }

  public void dropDownSelectionChanged(final DropDownControl dropDownControl) {
    System.out.println(
        "changed selection on [" + dropDownControl.toString() + "]"
        + " to [" + dropDownControl.getSelectedItemIdx() + "]"
        + " = [" + dropDownControl.getSelectedItem() + "]");
  }
}
