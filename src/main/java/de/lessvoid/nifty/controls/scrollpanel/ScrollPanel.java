package de.lessvoid.nifty.controls.scrollpanel;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.scrollbar.HorizontalScrollbar;
import de.lessvoid.nifty.controls.scrollbar.ScrollbarControlNotify;
import de.lessvoid.nifty.controls.scrollbar.VerticalScrollbar;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ScrollPanel implements Controller {
  private Nifty nifty;
  private Element element;
  private boolean verticalScrollbar;
  private boolean horizontalScrollbar;
  private String childRootId;
  private Element childRootElement;

  public void bind(Nifty niftyParam, Element elementParam, Properties parameter, ControllerEventListener listener, Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    element = elementParam;
    verticalScrollbar = new Boolean(parameter.getProperty("vertical", "true"));
    horizontalScrollbar = new Boolean(parameter.getProperty("horizontal", "true"));
    childRootId = controlDefinitionAttributes.get("childRootId");
    childRootElement = element.findElementByName(childRootId);
  }

  public void inputEvent(NiftyInputEvent inputEvent) {
  }
  
  public void onFocus(boolean getFocus) {
  }

  public void onStartScreen(final Screen screen) {
    initializeScrollPanel(screen);
  }

  public void initializeScrollPanel(final Screen screen) {
    if (!verticalScrollbar) {
      Element vertical = element.findElementByName("nifty-internal-vertical-scrollbar");
      if (vertical != null) {
        nifty.removeElement(screen, vertical);
      }
    }
    if (!horizontalScrollbar) {
      Element horizontal = element.findElementByName("nifty-internal-horizonal-panel");
      if (horizontal != null) {
        nifty.removeElement(screen, horizontal);
      }
    }

    nifty.removeElements();
    screen.layoutLayers();

    if (childRootElement != null) {
      final Element scrollElement = childRootElement.getElements().get(0);
      if (scrollElement != null) {
        HorizontalScrollbar horizontalS = element.findControl("nifty-internal-horizontal-scrollbar", HorizontalScrollbar.class);
        if (horizontalS != null) {
          horizontalS.setWorldMaxValue(scrollElement.getWidth());
          horizontalS.setViewMaxValue(childRootElement.getWidth());
          horizontalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
            public void positionChanged(final float currentValue) {
              scrollElement.setConstraintX(new SizeValue(-(int)currentValue + "px"));
              screen.layoutLayers();
            }
          });
        }
  
        VerticalScrollbar verticalS = element.findControl("nifty-internal-vertical-scrollbar", VerticalScrollbar.class);
        if (verticalS != null) {
          verticalS.setWorldMaxValue(scrollElement.getHeight());
          verticalS.setViewMaxValue(childRootElement.getHeight());
          verticalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
            public void positionChanged(final float currentValue) {
              scrollElement.setConstraintY(new SizeValue(-(int)currentValue + "px"));
              screen.layoutLayers();
            }
          });
        }
  
        scrollElement.setConstraintX(new SizeValue("0px"));
        scrollElement.setConstraintY(new SizeValue("0px"));
      }
    }
  }
}
