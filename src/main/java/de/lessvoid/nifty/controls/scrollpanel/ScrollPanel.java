package de.lessvoid.nifty.controls.scrollpanel;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.scrollbar.controller.HorizontalScrollbarControl;
import de.lessvoid.nifty.controls.scrollbar.controller.ScrollbarControlNotify;
import de.lessvoid.nifty.controls.scrollbar.controller.VerticalScrollbarControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ScrollPanel implements Controller {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private boolean verticalScrollbar;
  private boolean horizontalScrollbar;
  private String childRootId;
  private Element childRootElement;
  private float stepSizeX;
  private float stepSizeY;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    screen = screenParam;
    element = elementParam;
    verticalScrollbar = new Boolean(parameter.getProperty("vertical", "true"));
    horizontalScrollbar = new Boolean(parameter.getProperty("horizontal", "true"));
    childRootId = controlDefinitionAttributes.get("childRootId");
    childRootElement = element.findElementByName(childRootId);
    stepSizeX = new Float(parameter.getProperty("stepSizeX", "1.0"));
    stepSizeY = new Float(parameter.getProperty("stepSizeY", "1.0"));
  }

  public void inputEvent(NiftyInputEvent inputEvent) {
  }
  
  public void onFocus(boolean getFocus) {
  }

  public void onStartScreen() {
    initializeScrollPanel(screen, stepSizeX, stepSizeY);
  }

  public void initializeScrollPanel(final Screen screen, final float stepSizeX, final float stepSizeY) {
    this.stepSizeX = stepSizeX;
    this.stepSizeY = stepSizeY;

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
        HorizontalScrollbarControl horizontalS = element.findControl("nifty-internal-horizontal-scrollbar", HorizontalScrollbarControl.class);
        if (horizontalS != null) {
          horizontalS.setWorldMaxValue(scrollElement.getWidth());
          horizontalS.setViewMaxValue(childRootElement.getWidth());
          horizontalS.setPerClickChange(stepSizeX);
          horizontalS.setCurrentValue(0.0f);
          horizontalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
            public void positionChanged(final float currentValue) {
              scrollElement.setConstraintX(new SizeValue(-(int)currentValue + "px"));
              scrollElement.getParent().layoutElements();
            }
          });
        }
  
        VerticalScrollbarControl verticalS = element.findControl("nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
        if (verticalS != null) {
          verticalS.setWorldMaxValue(scrollElement.getHeight());
          verticalS.setViewMaxValue(childRootElement.getHeight());
          verticalS.setPerClickChange(stepSizeY);
          verticalS.setCurrentValue(0.0f);
          verticalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
            public void positionChanged(final float currentValue) {
              scrollElement.setConstraintY(new SizeValue(-(int)currentValue + "px"));
              scrollElement.getParent().layoutElements();
            }
          });
        }
  
        scrollElement.setConstraintX(new SizeValue("0px"));
        scrollElement.setConstraintY(new SizeValue("0px"));
      }
    }

    screen.layoutLayers();
  }
}
