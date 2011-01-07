package de.lessvoid.nifty.controls.scrollpanel;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.scrollbar.controller.HorizontalScrollbarControl;
import de.lessvoid.nifty.controls.scrollbar.controller.ScrollbarControlNotify;
import de.lessvoid.nifty.controls.scrollbar.controller.VerticalScrollbarControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.ElementChangeListener;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ScrollPanel extends AbstractController {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private boolean verticalScrollbar;
  private boolean horizontalScrollbar;
  private String childRootId;
  private Element childRootElement;
  private float stepSizeX;
  private float stepSizeY;
  private HorizontalScrollbarControl horizontalS;
  private VerticalScrollbarControl verticalS;

  public enum VerticalAlign {
    top, center, bottom
  }

  private AutoScroll autoScroll = AutoScroll.OFF;

  public enum AutoScroll {
    TOP("top"),
    BOTTOM("bottom"),
    LEFT("left"),
    RIGHT("right"),
    TOP_LEFT("topLeft"),
    TOP_RIGHT("topRight"),
    BOTTOM_LEFT("bottomLeft"),
    BOTTOM_RIGHT("bottomRight"),
    OFF("off");

    private String param;

    private AutoScroll(final String param) {
      this.param = param;
    }

    public String getParam() {
      return param;
    }

    public static AutoScroll parse(final String param) {
      for (AutoScroll auto : values()) {
        if (auto.param.compareTo(param) == 0) {
          return auto;
        }
      }
      return AutoScroll.OFF;
    }
  }

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
    autoScroll = AutoScroll.parse(parameter.getProperty("autoScroll", "off"));
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onFocus(final boolean getFocus) {
    super.onFocus(getFocus);
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

    nifty.executeEndOfFrameElementActions();
    screen.layoutLayers();

    if (childRootElement != null) {
      final Element scrollElement = childRootElement.getElements().get(0);
      if (scrollElement != null) {
        horizontalS = element.findControl("nifty-internal-horizontal-scrollbar", HorizontalScrollbarControl.class);
        if (horizontalS != null) {
          horizontalS.setWorldMaxValue(scrollElement.getWidth());
          horizontalS.setViewMaxValue(childRootElement.getWidth());
          horizontalS.setPerClickChange(stepSizeX);
          horizontalS.setCurrentValue(0.0f);
          horizontalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
            public void positionChanged(final float currentValue) {
              scrollElement.setConstraintX(new SizeValue(-(int) currentValue + "px"));
              scrollElement.getParent().layoutElements();
            }
          });
        }

        verticalS = element.findControl("nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
        if (verticalS != null) {
          verticalS.setWorldMaxValue(scrollElement.getHeight());
          verticalS.setViewMaxValue(childRootElement.getHeight());
          verticalS.setPerClickChange(stepSizeY);
          verticalS.setCurrentValue(0.0f);
          verticalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
            public void positionChanged(final float currentValue) {
              scrollElement.setConstraintY(new SizeValue(-(int) currentValue + "px"));
              scrollElement.getParent().layoutElements();
            }
          });
        }

        scrollElement.setConstraintX(new SizeValue("0px"));
        scrollElement.setConstraintY(new SizeValue("0px"));

        // Set this scroll panel to listen to changes to the scroll element
        scrollElement.addElementChangeListener(new ElementChangeListener() {
          @Override
          public void elementChanged(final Element element) {
            horizontalS = ScrollPanel.this.element.findControl("nifty-internal-horizontal-scrollbar",
                HorizontalScrollbarControl.class);
            verticalS = ScrollPanel.this.element.findControl("nifty-internal-vertical-scrollbar",
                VerticalScrollbarControl.class);
            if (horizontalS != null && horizontalS.getWorldMaxValue() != element.getWidth()) {
              updateWorldH();
            }
            if (verticalS != null && verticalS.getWorldMaxValue() != element.getHeight()) {
              updateWorldV();
            }
          }
        });
      }
    }

    screen.layoutLayers();
  }

  private void updateWorldH() {
    if (childRootElement != null) {
      final Element scrollElement = childRootElement.getElements().get(0);
      if (scrollElement != null) {
        horizontalS = element.findControl("nifty-internal-horizontal-scrollbar", HorizontalScrollbarControl.class);
        if (horizontalS != null) {
          horizontalS.setWorldMaxValue(scrollElement.getWidth());
          if (autoScroll == AutoScroll.RIGHT || autoScroll == AutoScroll.BOTTOM_RIGHT
              || autoScroll == AutoScroll.TOP_RIGHT) {
            horizontalS.setCurrentValue(horizontalS.getWorldMaxValue());
          } else if (autoScroll == AutoScroll.LEFT || autoScroll == AutoScroll.BOTTOM_LEFT
              || autoScroll == AutoScroll.TOP_LEFT) {
            horizontalS.setCurrentValue(horizontalS.getWorldMinValue());
          }
        }
      }
    }
  }

  private void updateWorldV() {
    if (childRootElement != null) {
      final Element scrollElement = childRootElement.getElements().get(0);
      if (scrollElement != null) {
        verticalS = element.findControl("nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
        if (verticalS != null) {
          verticalS.setWorldMaxValue(scrollElement.getHeight());
          if (autoScroll == AutoScroll.BOTTOM || autoScroll == AutoScroll.BOTTOM_LEFT
              || autoScroll == AutoScroll.BOTTOM_RIGHT) {
            verticalS.setCurrentValue(verticalS.getWorldMaxValue());
          } else if (autoScroll == AutoScroll.TOP || autoScroll == AutoScroll.TOP_LEFT
              || autoScroll == AutoScroll.TOP_RIGHT) {
            verticalS.setCurrentValue(verticalS.getWorldMinValue());
          }
        }
      }
    }
  }

  public Element getVerticalScrollBar() {
    return element.findElementByName("nifty-internal-vertical-scrollbar");
  }

  public Element getHorizontalScrollBar() {
    return element.findElementByName("nifty-internal-horizontal-scrollbar");
  }

  /**
   * sets the scrollPanel to the specified vertical position
   * 
   * @param yPos
   */
  public void setVerticalPos(final float yPos) {
    if (verticalS != null && verticalScrollbar) {
      verticalS.setCurrentValue(yPos);
    }
  }

  /**
   * sets the vertical position so you see the specified element (elemCount) For
   * that stepSizeY have to be exactly the height of one element
   * 
   * @param elemCount
   */
  public void showElementVertical(final int elemCount) {
    showElementVertical(elemCount, VerticalAlign.center);
  }

  /**
   * sets the vertical position so you see the specified element (elemCount) you
   * can choose between three different vertical alignments (top,middle,bottom)
   * For that function to work stepSizeY have to be exactly the height of one
   * element
   * 
   * @param elemCount
   * @param valign
   *          : top,middle,bottom
   */
  public void showElementVertical(final int elemCount, final VerticalAlign valign) {
    float newPos;
    switch (valign) {
    case top:
      newPos = stepSizeY * elemCount;
      break;
    case center:
      newPos = stepSizeY * elemCount - element.getHeight() / 2;
      break;
    case bottom:
      newPos = stepSizeY * elemCount - element.getHeight();
      break;
    default:
      newPos = 0;
    }
    setVerticalPos(newPos);
  }

  /**
   * Sets the automatic scrolling of the scroll panel when the underlying
   * element changes size
   * 
   * @param auto
   *          the new AutoScroll setting
   */
  public void setAutoScroll(final AutoScroll auto) {
    autoScroll = auto;
  }

  /**
   * @return the AutoScroll setting
   */
  public AutoScroll getAutoScroll() {
    return autoScroll;
  }
}
