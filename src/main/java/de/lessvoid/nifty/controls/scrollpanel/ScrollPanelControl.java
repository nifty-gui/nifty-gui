package de.lessvoid.nifty.controls.scrollpanel;

import java.util.List;
import java.util.Properties;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.controls.ScrollPanelChangedEvent;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.controls.ScrollbarChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ScrollPanelControl extends AbstractController implements ScrollPanel {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private boolean verticalScrollbar;
  private boolean horizontalScrollbar;
  private Element childRootElement;
  private float stepSizeX;
  private float stepSizeY;
  private float pageSizeX;
  private float pageSizeY;
  private AutoScroll autoScroll = AutoScroll.OFF;
  private EventTopicSubscriber<ScrollbarChangedEvent> horizontalScrollbarSubscriber = new EventTopicSubscriber<ScrollbarChangedEvent>() {
    @Override
    public void onEvent(final String id, final ScrollbarChangedEvent event) {
      final Element scrollElement = childRootElement.getElements().get(0);
      if (scrollElement != null) {
        scrollElement.setConstraintX(new SizeValue(-(int) event.getValue() + "px"));
        updateWorldH();
        scrollElement.getParent().layoutElements();

        float yPos = 0.f;
        Scrollbar verticalS = element.findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
        if (verticalS != null && verticalScrollbar) {
          yPos = verticalS.getValue();
        }
        nifty.publishEvent(element.getId(), new ScrollPanelChangedEvent(event.getValue(), yPos));
      }
    }
  };
  private EventTopicSubscriber<ScrollbarChangedEvent> verticalScrollbarSubscriber = new EventTopicSubscriber<ScrollbarChangedEvent>() {
    @Override
    public void onEvent(final String id, final ScrollbarChangedEvent event) {
      final Element scrollElement = childRootElement.getElements().get(0);
      if (scrollElement != null) {
        scrollElement.setConstraintY(new SizeValue(-(int) event.getValue() + "px"));
        updateWorldV();
        scrollElement.getParent().layoutElements();

        float xPos = 0.f;
        Scrollbar horizontalS = element.findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
        if (horizontalS != null && horizontalScrollbar) {
          xPos = horizontalS.getValue();
        }
        nifty.publishEvent(element.getId(), new ScrollPanelChangedEvent(xPos, event.getValue()));
      }
    }
  };

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(elementParam);
    nifty = niftyParam;
    screen = screenParam;
    element = elementParam;
    verticalScrollbar = new Boolean(parameter.getProperty("vertical", "true"));
    horizontalScrollbar = new Boolean(parameter.getProperty("horizontal", "true"));
    childRootElement = element.findElementByName(controlDefinitionAttributes.get("childRootId"));
    stepSizeX = new Float(parameter.getProperty("stepSizeX", "1.0"));
    stepSizeY = new Float(parameter.getProperty("stepSizeY", "1.0"));
    pageSizeX = new Float(parameter.getProperty("pageSizeX", "1.0"));
    pageSizeY = new Float(parameter.getProperty("pageSizeY", "1.0"));
    autoScroll = AutoScroll.parse(parameter.getProperty("autoScroll", "off"));
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    initializeScrollPanel();
    initializeScrollbars();
    subscribeHorizontalScrollbar();
    subscribeVerticalScrollbar();
  }

  public void onStartScreen() {
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void setHorizontalPos(final float xPos) {
    Scrollbar verticalS = element.findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (verticalS != null && verticalScrollbar) {
      verticalS.setValue(xPos);
    }
  }

  @Override
  public float getHorizontalPos() {
    Scrollbar verticalS = element.findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (verticalS != null && verticalScrollbar) {
      return verticalS.getValue();
    }
    return 0.f;
  }

  @Override
  public void setVerticalPos(final float yPos) {
    Scrollbar verticalS = element.findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
    if (verticalS != null && verticalScrollbar) {
      verticalS.setValue(yPos);
    }
  }

  @Override
  public float getVerticalPos() {
    Scrollbar verticalS = element.findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
    if (verticalS != null && verticalScrollbar) {
      return verticalS.getValue();
    }
    return 0.f;
  }

  @Override
  public void showElementVertical(final int elemCount) {
    showElementVertical(elemCount, VerticalAlign.center);
  }

  @Override
  public void setUp(final float stepSizeX, final float stepSizeY, final float pageSizeX, final float pageSizeY, final AutoScroll auto) {
    this.stepSizeX = stepSizeX;
    this.stepSizeY = stepSizeY;
    this.pageSizeX = pageSizeX;
    this.pageSizeY = pageSizeY;
    this.autoScroll = auto;

    initializeScrollbars();
  }

  @Override
  public void setAutoScroll(final AutoScroll auto) {
    this.autoScroll = auto;

    updateWorldH();
    updateWorldV();
  }

  @Override
  public AutoScroll getAutoScroll() {
    return autoScroll;
  }

  @Override
  public void setStepSizeX(final float stepSizeX) {
    this.stepSizeX = stepSizeX;
    Scrollbar horizontalS = element.findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (horizontalS != null) {
      horizontalS.setButtonStepSize(stepSizeX);
    }
  }

  @Override
  public void setStepSizeY(final float stepSizeY) {
    this.stepSizeY = stepSizeY;
    Scrollbar verticalS = element.findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
    if (verticalS != null) {
      verticalS.setButtonStepSize(stepSizeY);
    }
  }

  @Override
  public void setPageSizeX(final float pageSizeX) {
    this.pageSizeX = pageSizeX;
    Scrollbar horizontalS = element.findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (horizontalS != null) {
      horizontalS.setPageStepSize(pageSizeX);
    }
  }

  @Override
  public void setPageSizeY(final float pageSizeY) {
    this.pageSizeY = pageSizeY;
    Scrollbar verticalS = element.findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
    if (verticalS != null) {
      verticalS.setPageStepSize(pageSizeY);
    }
  }

  private void subscribeVerticalScrollbar() {
    Element scrollbar = getElement().findElementByName("#nifty-internal-vertical-scrollbar");
    if (scrollbar != null) {
      nifty.subscribe(scrollbar.getId(), ScrollbarChangedEvent.class, verticalScrollbarSubscriber);
    }
  }

  private void subscribeHorizontalScrollbar() {
    Element scrollbar = getElement().findElementByName("#nifty-internal-horizontal-scrollbar");
    if (scrollbar != null) {
      nifty.subscribe(scrollbar.getId(), ScrollbarChangedEvent.class, horizontalScrollbarSubscriber);
    }
  }

  private void unsubscribeVerticalScrollbar() {
    Element scrollbar = getElement().findElementByName("#nifty-internal-vertical-scrollbar");
    if (scrollbar != null) {
      nifty.unsubscribe(scrollbar.getId(), verticalScrollbarSubscriber);
    }
  }

  private void unsubscribeHorizontalScrollbar() {
    Element scrollbar = getElement().findElementByName("#nifty-internal-horizontal-scrollbar");
    if (scrollbar != null) {
      nifty.unsubscribe(scrollbar.getId(), horizontalScrollbarSubscriber);
    }
  }

  private void initializeScrollPanel() {
    if (!verticalScrollbar) {
      Element vertical = element.findElementByName("#nifty-internal-vertical-scrollbar");
      if (vertical != null) {
        nifty.removeElement(screen, vertical);
      }
    }
    if (!horizontalScrollbar) {
      Element horizontal = element.findElementByName("#nifty-internal-horizonal-panel");
      if (horizontal != null) {
        nifty.removeElement(screen, horizontal);
      }
    }
    nifty.executeEndOfFrameElementActions();
    screen.layoutLayers();
  }

  private void initializeScrollbars() {
    if (childRootElement != null) {
      List<Element> elements = childRootElement.getElements();
      if (elements.isEmpty()) {
        return;
      }
      final Element scrollElement = elements.get(0);
      if (scrollElement != null) {
        Scrollbar horizontalS = element.findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
        if (horizontalS != null) {
          horizontalS.setWorldMax(scrollElement.getWidth());
          updateWorldH();
          horizontalS.setViewMax(horizontalS.getWidth());
          horizontalS.setValue(0.0f);
          horizontalS.setButtonStepSize(stepSizeX);
          horizontalS.setPageStepSize(pageSizeX);
        }

        Scrollbar verticalS = element.findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
        if (verticalS != null) {
          verticalS.setWorldMax(scrollElement.getHeight());
          updateWorldV();
          verticalS.setViewMax(verticalS.getHeight());
          verticalS.setValue(0.0f);
          verticalS.setButtonStepSize(stepSizeY);
          verticalS.setPageStepSize(pageSizeY);
        }
        scrollElement.setConstraintX(new SizeValue("0px"));
        scrollElement.setConstraintY(new SizeValue("0px"));
      }
    }
    screen.layoutLayers();
  }

  private void updateWorldH() {
    Scrollbar horizontalS = element.findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (horizontalS != null) {
      if (autoScroll == AutoScroll.RIGHT || autoScroll == AutoScroll.BOTTOM_RIGHT || autoScroll == AutoScroll.TOP_RIGHT) {
        horizontalS.setValue(horizontalS.getWorldMax());
      } else if (autoScroll == AutoScroll.LEFT || autoScroll == AutoScroll.BOTTOM_LEFT || autoScroll == AutoScroll.TOP_LEFT) {
        horizontalS.setValue(0);
      }
    }
  }

  private void updateWorldV() {
    Scrollbar verticalS = element.findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
    if (verticalS != null) {
      if (autoScroll == AutoScroll.BOTTOM || autoScroll == AutoScroll.BOTTOM_LEFT || autoScroll == AutoScroll.BOTTOM_RIGHT) {
        verticalS.setValue(verticalS.getWorldMax());
      } else if (autoScroll == AutoScroll.TOP || autoScroll == AutoScroll.TOP_LEFT || autoScroll == AutoScroll.TOP_RIGHT) {
        verticalS.setValue(0);
      }
    }
  }

  private void showElementVertical(final int elemCount, final VerticalAlign valign) {
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
}
