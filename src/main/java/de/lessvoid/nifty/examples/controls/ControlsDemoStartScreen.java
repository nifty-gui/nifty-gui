package de.lessvoid.nifty.examples.controls;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dropdown.DropDownControl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ControlsDemoStartScreen implements ScreenController {
  private Nifty nifty;
  private Screen screen;

  public void bind(final Nifty newNifty, final Screen newScreen) {
    screen = newScreen;
    nifty = newNifty;
    DropDownControl dropDown1 = findDropDownControl("dropDown1");
    dropDown1.addItem("Nifty GUI");
    dropDown1.addItem("Slick2d");
    dropDown1.addItem("Lwjgl");
    dropDown1.setSelectedItemIdx(0);

    DropDownControl dropDown2 = findDropDownControl("dropDown2");
    dropDown2.addItem("rocks!");
    dropDown2.addItem("rules!");
    dropDown2.addItem("kicks ass!");
    dropDown2.addItem("is awesome!");
    dropDown2.addItem("shizzles :D");
    dropDown2.setSelectedItem("rocks!");
  }

  private DropDownControl findDropDownControl(final String id) {
    DropDownControl dropDown1 = screen.findControl(id, DropDownControl.class);
    return dropDown1;
  }

  public void onStartScreen() {
    screen.findElementByName("backButton").setFocus();
  }

  public void onEndScreen() {
  }

  public final void back() {
    DropDownControl dropDown1 = findDropDownControl("dropDown1");
    System.out.println(dropDown1.getSelectedItemIdx() + ":" + dropDown1.getSelectedItem());
    DropDownControl dropDown2 = findDropDownControl("dropDown2");
    System.out.println(dropDown2.getSelectedItemIdx() + ":" + dropDown2.getSelectedItem());
    nifty.fromXml("all/intro.xml", "menu");
  }
}
