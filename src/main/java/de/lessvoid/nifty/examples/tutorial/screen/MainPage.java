package de.lessvoid.nifty.examples.tutorial.screen;

import java.util.ArrayList;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MainPage implements ScreenController {
  private Nifty nifty;
  private Screen screen;

  private ArrayList <String> pages = new ArrayList <String> ();
  private int pageIndex = 0;
  private int lastPageIndex = 0;
  
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    pages.add("page1");
    pages.add("page2");
    pages.add("page3");

    pageIndex = 0;
    lastPageIndex = 0;
  }
  
  public void onStartScreen() {
  }
  
  public void onEndScreen() {
  }

  public void back() {
    pageIndex--;
    if (pageIndex < 0) {
      pageIndex = pages.size() - 1;
    }

    nifty.setAlternateKey("back");

    CreateButtonControl buttonControl = new CreateButtonControl(pages.get(pageIndex), "page_" + pageIndex);
    buttonControl.create(nifty, screen, screen.findElementByName("pp"));

    nifty.removeElement(screen, screen.findElementByName("page_" + lastPageIndex), new EndNotify() {
      public void perform() {
      }
    });
    lastPageIndex = pageIndex;
  }

  public void next() {
    pageIndex++;
    if (pageIndex >= pages.size()) {
      pageIndex = 0;
    }

    nifty.setAlternateKey(null);

    CreateButtonControl buttonControl = new CreateButtonControl(pages.get(pageIndex), "page_" + pageIndex);
    buttonControl.create(nifty, screen, screen.findElementByName("pp"));

    nifty.removeElement(screen, screen.findElementByName("page_" + lastPageIndex), new EndNotify() {
      public void perform() {
      }
    });
    lastPageIndex = pageIndex;
  }

  public class CreateButtonControl extends ControlAttributes {
    public CreateButtonControl(final String name, final String id) {
      setId(id);
      setName(name);
    }

    public void create(
        final Nifty nifty,
        final Screen screen,
        final Element parent) {
      nifty.addControl(screen, parent, getStandardControl());
    }
  }
}