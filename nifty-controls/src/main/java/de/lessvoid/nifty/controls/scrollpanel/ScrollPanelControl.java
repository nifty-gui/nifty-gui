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
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * @deprecated Please use {@link de.lessvoid.nifty.controls.ScrollPanel} when accessing NiftyControls.
 */
@Deprecated
public class ScrollPanelControl extends AbstractController implements ScrollPanel {
  private Nifty nifty;
  private Screen screen;
  private boolean verticalScrollbar;
  private boolean horizontalScrollbar;
  private Element childRootElement;
  private float stepSizeX;
  private float stepSizeY;
  private float pageSizeX;
  private float pageSizeY;
  private AutoScroll autoScroll = AutoScroll.OFF;
  private EventTopicSubscriber<ScrollbarChangedEvent> horizontalScrollbarSubscriber;
  private EventTopicSubscriber<ScrollbarChangedEvent> verticalScrollbarSubscriber;

  @Override
  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(elementParam);
    nifty = niftyParam;
    screen = screenParam;
    verticalScrollbar = new Boolean(parameter.getProperty("vertical", "true"));
    horizontalScrollbar = new Boolean(parameter.getProperty("horizontal", "true"));
    childRootElement = getElement().findElementByName(controlDefinitionAttributes.get("childRootId"));
    stepSizeX = new Float(parameter.getProperty("stepSizeX", "1.0"));
    stepSizeY = new Float(parameter.getProperty("stepSizeY", "1.0"));
    pageSizeX = new Float(parameter.getProperty("pageSizeX", "1.0"));
    pageSizeY = new Float(parameter.getProperty("pageSizeY", "1.0"));
    autoScroll = AutoScroll.parse(parameter.getProperty("autoScroll", "off"));
    horizontalScrollbarSubscriber = new HorizontalEventTopicSubscriber(this);
    verticalScrollbarSubscriber = new VerticalEventTopicSubscriber(this);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    initializeScrollPanel();
    initializeScrollbars();
    subscribeHorizontalScrollbar();
    subscribeVerticalScrollbar();
    super.init(parameter, controlDefinitionAttributes);
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void layoutCallback() {
    if (childRootElement != null) {
      List<Element> elements = childRootElement.getElements();
      if (elements.isEmpty()) {
        return;
      }
      final Element scrollElement = elements.get(0);
      if (scrollElement != null) {
        Scrollbar horizontalS = getElement().findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
        if (horizontalS != null) {
          horizontalS.setWorldMax(scrollElement.getWidth());
          horizontalS.setWorldPageSize(horizontalS.getWidth());
          updateWorldH();
        }

        Scrollbar verticalS = getElement().findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
        if (verticalS != null) {
          verticalS.setWorldMax(scrollElement.getHeight());
          verticalS.setWorldPageSize(verticalS.getHeight());
          updateWorldV();
        }
      }
    }
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void setHorizontalPos(final float xPos) {
    Scrollbar horizontalS = getElement().findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (horizontalS != null && verticalScrollbar) {
      horizontalS.setValue(xPos);
    }
  }

  @Override
  public float getHorizontalPos() {
    Scrollbar horizontalS = getElement().findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (horizontalS != null && verticalScrollbar) {
      return horizontalS.getValue();
    }
    return 0.f;
  }

  @Override
  public void setVerticalPos(final float yPos) {
    Scrollbar verticalS = getElement().findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
    if (verticalS != null && verticalScrollbar) {
      verticalS.setValue(yPos);
    }
  }

  @Override
  public float getVerticalPos() {
    Scrollbar verticalS = getElement().findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
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
    Scrollbar horizontalS = getElement().findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (horizontalS != null) {
      horizontalS.setButtonStepSize(stepSizeX);
    }
  }

  @Override
  public void setStepSizeY(final float stepSizeY) {
    this.stepSizeY = stepSizeY;
    Scrollbar verticalS = getElement().findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
    if (verticalS != null) {
      verticalS.setButtonStepSize(stepSizeY);
    }
  }

  @Override
  public void setPageSizeX(final float pageSizeX) {
    this.pageSizeX = pageSizeX;
    Scrollbar horizontalS = getElement().findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (horizontalS != null) {
      horizontalS.setPageStepSize(pageSizeX);
    }
  }

  @Override
  public void setPageSizeY(final float pageSizeY) {
    this.pageSizeY = pageSizeY;
    Scrollbar verticalS = getElement().findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
    if (verticalS != null) {
      verticalS.setPageStepSize(pageSizeY);
    }
  }

  public void mouseWheel(final Element e, final NiftyMouseInputEvent inputEvent) {
    int mouseWheel = inputEvent.getMouseWheel();
    Scrollbar verticalS = getElement().findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
    if (verticalS != null) {
      float currentValue = verticalS.getValue();
      if (mouseWheel < 0) {
        verticalS.setValue(currentValue - verticalS.getButtonStepSize() * mouseWheel);
      } else if (mouseWheel > 0) {
        verticalS.setValue(currentValue - verticalS.getButtonStepSize() * mouseWheel);
      }
    }
  }

  private void subscribeVerticalScrollbar() {
    Element scrollbar = getElement().findElementByName("#nifty-internal-vertical-scrollbar");
    if (scrollbar != null) {
      nifty.subscribe(screen, scrollbar.getId(), ScrollbarChangedEvent.class, verticalScrollbarSubscriber);
    }
  }

  private void subscribeHorizontalScrollbar() {
    Element scrollbar = getElement().findElementByName("#nifty-internal-horizontal-scrollbar");
    if (scrollbar != null) {
      nifty.subscribe(screen, scrollbar.getId(), ScrollbarChangedEvent.class, horizontalScrollbarSubscriber);
    }
  }

  private void initializeScrollPanel() {
    if (!verticalScrollbar) {
      Element vertical = getElement().findElementByName("#nifty-internal-vertical-scrollbar");
      if (vertical != null) {
        nifty.removeElement(screen, vertical);
      }
    }
    if (!horizontalScrollbar) {
      Element horizontal = getElement().findElementByName("#nifty-internal-horizonal-panel");
      if (horizontal != null) {
        nifty.removeElement(screen, horizontal);
      }
    }
    nifty.executeEndOfFrameElementActions();
    getElement().getParent().layoutElements();
  }

  private void initializeScrollbars() {
    if (childRootElement != null) {
      List<Element> elements = childRootElement.getElements();
      if (elements.isEmpty()) {
        return;
      }
      final Element scrollElement = elements.get(0);
      if (scrollElement != null) {
        Scrollbar horizontalS = getElement().findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
        if (horizontalS != null) {
          horizontalS.setWorldMax(scrollElement.getWidth());
          updateWorldH();
          horizontalS.setWorldPageSize(horizontalS.getWidth());
          horizontalS.setValue(0.0f);
          horizontalS.setButtonStepSize(stepSizeX);
          horizontalS.setPageStepSize(pageSizeX);
        }

        Scrollbar verticalS = getElement().findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
        if (verticalS != null) {
          verticalS.setWorldMax(scrollElement.getHeight());
          updateWorldV();
          verticalS.setWorldPageSize(verticalS.getHeight());
          verticalS.setValue(0.0f);
          verticalS.setButtonStepSize(stepSizeY);
          verticalS.setPageStepSize(pageSizeY);
        }
        scrollElement.setConstraintX(new SizeValue("0px"));
        scrollElement.setConstraintY(new SizeValue("0px"));
      }
      scrollElement.getParent().layoutElements();
    }
  }

  private void updateWorldH() {
    Scrollbar horizontalS = getElement().findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
    if (horizontalS != null) {
      if (autoScroll == AutoScroll.RIGHT || autoScroll == AutoScroll.BOTTOM_RIGHT || autoScroll == AutoScroll.TOP_RIGHT) {
        horizontalS.setValue(horizontalS.getWorldMax());
      } else if (autoScroll == AutoScroll.LEFT || autoScroll == AutoScroll.BOTTOM_LEFT || autoScroll == AutoScroll.TOP_LEFT) {
        horizontalS.setValue(0);
      }
    }
  }

  private void updateWorldV() {
    Scrollbar verticalS = getElement().findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
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
      newPos = stepSizeY * elemCount - getElement().getHeight() / 2;
      break;
    case bottom:
      newPos = stepSizeY * elemCount - getElement().getHeight();
      break;
    default:
      newPos = 0;
    }
    setVerticalPos(newPos);
  }

  private class VerticalEventTopicSubscriber implements EventTopicSubscriber<ScrollbarChangedEvent> {
    private ScrollPanel scrollPanel;

    public VerticalEventTopicSubscriber(final ScrollPanel scrollPanel) {
      this.scrollPanel = scrollPanel;
    }

    @Override
    public void onEvent(final String id, final ScrollbarChangedEvent event) {
      if (childRootElement == null) {
        return;
      }
      if (childRootElement.getElements().isEmpty()) {
        return;
      }
      final Element scrollElement = childRootElement.getElements().get(0);
      if (scrollElement != null) {
        scrollElement.setConstraintY(new SizeValue(-(int) event.getValue() + "px"));
        updateWorldV();
        scrollElement.getParent().layoutElements();

        float xPos = 0.f;
        Scrollbar horizontalS = getElement().findNiftyControl("#nifty-internal-horizontal-scrollbar", Scrollbar.class);
        if (horizontalS != null && horizontalScrollbar) {
          xPos = horizontalS.getValue();
        }
        nifty.publishEvent(getElement().getId(), new ScrollPanelChangedEvent(scrollPanel, xPos, event.getValue()));
      }
    }
  }

  private class HorizontalEventTopicSubscriber implements EventTopicSubscriber<ScrollbarChangedEvent> {
    private ScrollPanel scrollPanel;

    public HorizontalEventTopicSubscriber(final ScrollPanel scrollPanel) {
      this.scrollPanel = scrollPanel;
    }

    @Override
    public void onEvent(final String id, final ScrollbarChangedEvent event) {
      if (childRootElement == null) {
        return;
      }
      if (childRootElement.getElements().isEmpty()) {
        return;
      }
      final Element scrollElement = childRootElement.getElements().get(0);
      if (scrollElement != null) {
        scrollElement.setConstraintX(new SizeValue(-(int) event.getValue() + "px"));
        updateWorldH();
        scrollElement.getParent().layoutElements();

        float yPos = 0.f;
        Scrollbar verticalS = getElement().findNiftyControl("#nifty-internal-vertical-scrollbar", Scrollbar.class);
        if (verticalS != null && verticalScrollbar) {
          yPos = verticalS.getValue();
        }
        nifty.publishEvent(getElement().getId(), new ScrollPanelChangedEvent(scrollPanel, event.getValue(), yPos));
      }
    }
  }
}
