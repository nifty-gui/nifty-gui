package de.lessvoid.nifty.controls.scrollpanel;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import org.bushe.swing.event.EventTopicSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.logging.Logger;

/**
 * @deprecated Please use {@link de.lessvoid.nifty.controls.ScrollPanel} when accessing NiftyControls.
 */
@Deprecated
public class ScrollPanelControl extends AbstractController implements ScrollPanel {
  @Nonnull
  private static final Logger log = Logger.getLogger(ScrollPanelControl.class.getName());
  @Nullable
  private Nifty nifty;
  @Nullable
  private Screen screen;
  private boolean verticalScrollbar;
  private boolean horizontalScrollbar;
  @Nullable
  private Element childRootElement;
  private float stepSizeX;
  private float stepSizeY;
  private float pageSizeX;
  private float pageSizeY;
  @Nonnull
  private AutoScroll autoScroll = AutoScroll.OFF;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    bind(element);
    this.nifty = nifty;
    this.screen = screen;
    verticalScrollbar = parameter.getAsBoolean("vertical", true);
    horizontalScrollbar = parameter.getAsBoolean("horizontal", true);

    String childRootId = parameter.get("childRootId");
    if (childRootId == null) {
      log.severe("Missing children root id. This scroll panel will not work.");
    } else {
      childRootElement = element.findElementById(childRootId);
    }
    stepSizeX = parameter.getAsFloat("stepSizeX", 1.f);
    stepSizeY = parameter.getAsFloat("stepSizeY", 1.f);
    pageSizeX = parameter.getAsFloat("pageSizeX", 1.f);
    pageSizeY = parameter.getAsFloat("pageSizeY", 1.f);

    autoScroll = AutoScroll.OFF;
    String autoScrollParam = parameter.get("autoScroll");
    if (autoScrollParam != null) {
      for (AutoScroll value : AutoScroll.values()) {
        if (autoScrollParam.equals(value.getParam())) {
          autoScroll = value;
          break;
        }
      }
    }
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    super.init(parameter);
    if (nifty == null || screen == null) {
      log.severe("Binding of control is not done. Can't initialize.");
      return;
    }
    initializeScrollPanel(nifty, screen);
    initializeScrollbars();

    Element scrollbarV = getVerticalScrollbar();
    if (scrollbarV != null) {
      String id = scrollbarV.getId();
      if (id != null) {
        nifty.subscribe(screen, id, ScrollbarChangedEvent.class, new VerticalEventTopicSubscriber(this));
      }
    }
    Element scrollbarH = getHorizontalScrollbar();
    if (scrollbarH != null) {
      String id = scrollbarH.getId();
      if (id != null) {
        nifty.subscribe(screen, id, ScrollbarChangedEvent.class, new HorizontalEventTopicSubscriber(this));
      }
    }
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void layoutCallback() {
    if (childRootElement != null) {
      List<Element> elements = childRootElement.getChildren();
      if (elements.isEmpty()) {
        return;
      }
      final Element scrollElement = elements.get(0);
      if (scrollElement != null) {
        Scrollbar horizontalS = getHorizontalScrollbarControl();
        if (horizontalS != null) {
          horizontalS.setWorldMax(scrollElement.getWidth());
          horizontalS.setWorldPageSize(horizontalS.getWidth());
          updateWorldH();
        }

        Scrollbar verticalS = getVerticalScrollbarControl();
        if (verticalS != null) {
          verticalS.setWorldMax(scrollElement.getHeight());
          verticalS.setWorldPageSize(verticalS.getHeight());
          updateWorldV();
        }
      }
    }
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void setHorizontalPos(final float xPos) {
    Scrollbar horizontalS = getHorizontalScrollbarControl();
    if (horizontalS != null && verticalScrollbar) {
      horizontalS.setValue(xPos);
    }
  }

  @Override
  public float getHorizontalPos() {
    Scrollbar horizontalS = getHorizontalScrollbarControl();
    if (horizontalS != null && verticalScrollbar) {
      return horizontalS.getValue();
    }
    return 0.f;
  }

  @Override
  public void setVerticalPos(final float yPos) {
    Scrollbar verticalS = getVerticalScrollbarControl();
    if (verticalS != null && verticalScrollbar) {
      verticalS.setValue(yPos);
    }
  }

  @Override
  public float getVerticalPos() {
    Scrollbar verticalS = getVerticalScrollbarControl();
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
  public void setUp(
      final float stepSizeX,
      final float stepSizeY,
      final float pageSizeX,
      final float pageSizeY,
      @Nonnull final AutoScroll auto) {
    this.stepSizeX = stepSizeX;
    this.stepSizeY = stepSizeY;
    this.pageSizeX = pageSizeX;
    this.pageSizeY = pageSizeY;
    this.autoScroll = auto;

    initializeScrollbars();
  }

  @Override
  public void setAutoScroll(@Nonnull final AutoScroll auto) {
    this.autoScroll = auto;

    updateWorldH();
    updateWorldV();
  }

  @Nonnull
  @Override
  public AutoScroll getAutoScroll() {
    return autoScroll;
  }

  @Override
  public void setStepSizeX(final float stepSizeX) {
    this.stepSizeX = stepSizeX;
    Scrollbar horizontalS = getHorizontalScrollbarControl();
    if (horizontalS != null) {
      horizontalS.setButtonStepSize(stepSizeX);
    }
  }

  @Override
  public void setStepSizeY(final float stepSizeY) {
    this.stepSizeY = stepSizeY;
    Scrollbar verticalS = getHorizontalScrollbarControl();
    if (verticalS != null) {
      verticalS.setButtonStepSize(stepSizeY);
    }
  }

  @Override
  public void setPageSizeX(final float pageSizeX) {
    this.pageSizeX = pageSizeX;
    Scrollbar horizontalS = getHorizontalScrollbarControl();
    if (horizontalS != null) {
      horizontalS.setPageStepSize(pageSizeX);
    }
  }

  @Override
  public void setPageSizeY(final float pageSizeY) {
    this.pageSizeY = pageSizeY;
    Scrollbar verticalS = getVerticalScrollbarControl();
    if (verticalS != null) {
      verticalS.setPageStepSize(pageSizeY);
    }
  }

  public void mouseWheel(final Element e, @Nonnull final NiftyMouseInputEvent inputEvent) {
    int mouseWheel = inputEvent.getMouseWheel();
    Scrollbar verticalS = getVerticalScrollbarControl();
    if (verticalS != null) {
      float currentValue = verticalS.getValue();
      if (mouseWheel < 0) {
        verticalS.setValue(currentValue - verticalS.getButtonStepSize() * mouseWheel);
      } else if (mouseWheel > 0) {
        verticalS.setValue(currentValue - verticalS.getButtonStepSize() * mouseWheel);
      }
    }
  }

  @Nullable
  private Element getChildById(@Nonnull final String id) {
    Element element = getElement();
    if (element == null) {
      return null;
    }
    return element.findElementById(id);
  }

  @Nullable
  private Element getVerticalScrollbar() {
    if (verticalScrollbar) {
      return getChildById("#nifty-internal-vertical-scrollbar");
    }
    return null;
  }

  @Nullable
  private Scrollbar getVerticalScrollbarControl() {
    Element element = getVerticalScrollbar();
    if (element == null) {
      return null;
    }
    return element.getNiftyControl(Scrollbar.class);
  }

  @Nullable
  private Element getHorizontalScrollbar() {
    if (horizontalScrollbar) {
      return getChildById("#nifty-internal-horizontal-scrollbar");
    }
    return null;
  }

  @Nullable
  private Scrollbar getHorizontalScrollbarControl() {
    Element element = getHorizontalScrollbar();
    if (element == null) {
      return null;
    }
    return element.getNiftyControl(Scrollbar.class);
  }

  private void initializeScrollPanel(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
    if (!verticalScrollbar) {
      Element vertical = getChildById("#nifty-internal-vertical-scrollbar");
      if (vertical != null) {
        nifty.removeElement(screen, vertical);
      }
    }
    if (!horizontalScrollbar) {
      Element horizontal = getChildById("#nifty-internal-horizontal-panel");
      if (horizontal != null) {
        nifty.removeElement(screen, horizontal);
      }
    }
  }

  private void initializeScrollbars() {
    if (childRootElement != null) {
      List<Element> elements = childRootElement.getChildren();
      if (elements.isEmpty()) {
        return;
      }
      final Element scrollElement = elements.get(0);
      if (scrollElement != null) {
        Scrollbar horizontalS = getHorizontalScrollbarControl();
        if (horizontalS != null) {
          horizontalS.setWorldMax(scrollElement.getWidth());
          updateWorldH();
          horizontalS.setWorldPageSize(horizontalS.getWidth());
          horizontalS.setValue(0.0f);
          horizontalS.setButtonStepSize(stepSizeX);
          horizontalS.setPageStepSize(pageSizeX);
        }

        Scrollbar verticalS = getVerticalScrollbarControl();
        if (verticalS != null) {
          verticalS.setWorldMax(scrollElement.getHeight());
          updateWorldV();
          verticalS.setWorldPageSize(verticalS.getHeight());
          verticalS.setValue(0.0f);
          verticalS.setButtonStepSize(stepSizeY);
          verticalS.setPageStepSize(pageSizeY);
        }
        scrollElement.setConstraintX(SizeValue.px(0));
        scrollElement.setConstraintY(SizeValue.px(0));
        childRootElement.layoutElements();
      }
    }
  }

  private void updateWorldH() {
    Scrollbar horizontalS = getHorizontalScrollbarControl();
    if (horizontalS != null) {
      if (autoScroll == AutoScroll.RIGHT || autoScroll == AutoScroll.BOTTOM_RIGHT || autoScroll == AutoScroll
          .TOP_RIGHT) {
        horizontalS.setValue(horizontalS.getWorldMax());
      } else if (autoScroll == AutoScroll.LEFT || autoScroll == AutoScroll.BOTTOM_LEFT || autoScroll == AutoScroll
          .TOP_LEFT) {
        horizontalS.setValue(0);
      }
    }
  }

  private void updateWorldV() {
    Scrollbar verticalS = getVerticalScrollbarControl();
    if (verticalS != null) {
      if (autoScroll == AutoScroll.BOTTOM || autoScroll == AutoScroll.BOTTOM_LEFT || autoScroll == AutoScroll
          .BOTTOM_RIGHT) {
        verticalS.setValue(verticalS.getWorldMax());
      } else if (autoScroll == AutoScroll.TOP || autoScroll == AutoScroll.TOP_LEFT || autoScroll == AutoScroll
          .TOP_RIGHT) {
        verticalS.setValue(0);
      }
    }
  }

  private void showElementVertical(final int elemCount, @Nonnull final VerticalAlign valign) {
    float newPos;

    switch (valign) {
      case top:
        newPos = stepSizeY * elemCount;
        break;
      case center:
        newPos = stepSizeY * elemCount - getHeight() / 2;
        break;
      case bottom:
        newPos = stepSizeY * elemCount - getHeight();
        break;
      default:
        newPos = 0;
    }
    setVerticalPos(newPos);
  }

  private class VerticalEventTopicSubscriber implements EventTopicSubscriber<ScrollbarChangedEvent> {
    @Nonnull
    private final ScrollPanel scrollPanel;

    public VerticalEventTopicSubscriber(@Nonnull final ScrollPanel scrollPanel) {
      this.scrollPanel = scrollPanel;
    }

    @Override
    public void onEvent(final String id, @Nonnull final ScrollbarChangedEvent event) {
      if (childRootElement == null) {
        return;
      }
      if (childRootElement.getChildren().isEmpty()) {
        return;
      }
      final Element scrollElement = childRootElement.getChildren().get(0);
      if (scrollElement != null) {
        scrollElement.setConstraintY(SizeValue.px(-(int) event.getValue()));
        updateWorldV();
        childRootElement.layoutElements();

        float xPos = 0.f;
        Scrollbar horizontalS = getHorizontalScrollbarControl();
        if (horizontalS != null && horizontalScrollbar) {
          xPos = horizontalS.getValue();
        }
        if (nifty != null) {
          String panelId = getId();
          if (panelId != null) {
            nifty.publishEvent(panelId, new ScrollPanelChangedEvent(scrollPanel, xPos, event.getValue()));
          }
        }
      }
    }
  }

  private class HorizontalEventTopicSubscriber implements EventTopicSubscriber<ScrollbarChangedEvent> {
    @Nonnull
    private final ScrollPanel scrollPanel;

    public HorizontalEventTopicSubscriber(@Nonnull final ScrollPanel scrollPanel) {
      this.scrollPanel = scrollPanel;
    }

    @Override
    public void onEvent(final String id, @Nonnull final ScrollbarChangedEvent event) {
      if (childRootElement == null) {
        return;
      }
      if (childRootElement.getChildren().isEmpty()) {
        return;
      }
      final Element scrollElement = childRootElement.getChildren().get(0);
      if (scrollElement != null) {
        scrollElement.setConstraintX(SizeValue.px(-(int) event.getValue()));
        updateWorldH();
        childRootElement.layoutElements();

        float yPos = 0.f;
        Scrollbar verticalS = getVerticalScrollbarControl();
        if (verticalS != null && verticalScrollbar) {
          yPos = verticalS.getValue();
        }
        if (nifty != null) {
          String panelId = getId();
          if (panelId != null) {
            nifty.publishEvent(panelId, new ScrollPanelChangedEvent(scrollPanel, event.getValue(), yPos));
          }
        }
      }
    }
  }
}
