package de.lessvoid.nifty.controls.scrollbar;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.NextPrevHelper;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.controls.ScrollbarChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ScrollbarControl extends AbstractController implements Scrollbar {
  private ScrollbarImpl scrollbarImpl = new ScrollbarImpl();
  private ScrollbarView scrollbarView;
  private Nifty nifty;
  private Element element;
  private Element elementBackground;
  private Element elementPosition;
  private NextPrevHelper nextPrevHelper;
  private float worldMax;
  private float viewMax;
  private float initial;
  private float pageStepSize;
  private float buttonStepSize;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);

    this.nifty = nifty;
    this.element = element;
    this.elementBackground = element.findElementByName("#background");
    this.elementPosition = element.findElementByName("#position");
    this.nextPrevHelper = new NextPrevHelper(element, screen.getFocusHandler());

    if ("verticalScrollbar".equals(parameter.getProperty("name"))) {
      this.scrollbarView = new ScrollbarViewVertical(elementPosition.getHeight());
    } else if ("horizontalScrollbar".equals(parameter.getProperty("name"))) {
      this.scrollbarView = new ScrollbarViewHorizontal(elementPosition.getWidth());
    }

    worldMax = Float.valueOf(parameter.getProperty("worldMax", "100.0"));
    viewMax = Float.valueOf(parameter.getProperty("viewMax", "100.0"));
    initial = Float.valueOf(parameter.getProperty("initial", "0.0"));
    buttonStepSize = Float.valueOf(parameter.getProperty("buttonStepSize", "1.0"));
    pageStepSize = Float.valueOf(parameter.getProperty("pageStepSize", "25.0"));
    scrollbarImpl.bindToView(scrollbarView, initial, worldMax, viewMax, buttonStepSize, pageStepSize);
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (nextPrevHelper.handleNextPrev(inputEvent)) {
      return true;
    }
    if (inputEvent == NiftyInputEvent.MoveCursorUp || inputEvent == NiftyInputEvent.MoveCursorLeft) {
      scrollbarImpl.stepDown();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorDown || inputEvent == NiftyInputEvent.MoveCursorRight) {
      scrollbarImpl.stepUp();
      return true;
    }
    return false;
  }

  public void upClick() {
    scrollbarImpl.stepDown();
  }

  public void downClick() {
    scrollbarImpl.stepUp();
  }

  public void click(final int mouseX, final int mouseY) {
    scrollbarImpl.interactionClick(scrollbarView.filter(mouseX, mouseY));
  }

  public void mouseMoveStart(final int mouseX, final int mouseY) {
    scrollbarImpl.interactionClick(scrollbarView.filter(mouseX, mouseY));
  }

  public void mouseMove(final int mouseX, final int mouseY) {
    scrollbarImpl.interactionMove(scrollbarView.filter(mouseX, mouseY));
  }

  // Scrollbar implementation

  @Override
  public void setup(final float value, final float worldMax, final float viewMax, final float buttonStepSize, final float pageStepSize) {
    scrollbarImpl.setup(value, worldMax, viewMax, buttonStepSize, pageStepSize);
  }

  @Override
  public void setValue(final float value) {
    scrollbarImpl.setValue(value);
  }

  @Override
  public float getValue() {
    return scrollbarImpl.getValue();
  }

  @Override
  public void setWorldMax(final float worldMax) {
    scrollbarImpl.setWorldMax(worldMax);
  }

  @Override
  public float getWorldMax() {
    return scrollbarImpl.getWorldMax();
  }

  @Override
  public void setViewMax(final float viewMax) {
    scrollbarImpl.setViewMax(viewMax);
  }

  @Override
  public float getViewMax() {
    return scrollbarImpl.getViewMax();
  }

  @Override
  public void setButtonStepSize(final float stepSize) {
    scrollbarImpl.setButtonStepSize(stepSize);
  }

  @Override
  public float getButtonStepSize() {
    return scrollbarImpl.getButtonStepSize();
  }

  @Override
  public void setPageStepSize(final float stepSize) {
    scrollbarImpl.setPageStepSize(stepSize);
  }

  @Override
  public float getPageStepSize() {
    return scrollbarImpl.getPageStepSize();
  }

  // ScrollbarView implementations

  private class ScrollbarViewVertical implements ScrollbarView {
    private int minHandleSize;

    public ScrollbarViewVertical(final int minHandleSize) {
      this.minHandleSize = minHandleSize;
    }

    @Override
    public int getAreaSize() {
      return elementBackground.getHeight();
    }

    @Override
    public int getMinHandleSize() {
      return minHandleSize;
    }

    @Override
    public void setHandle(final int pos, final int size) {
      if (size == 0) {
        elementPosition.hide();
      } else {
        elementPosition.show();
        elementPosition.setConstraintY(new SizeValue(pos + "px"));
        elementPosition.setConstraintHeight(new SizeValue(size + "px"));
        elementBackground.layoutElements();
      }
    }

    @Override
    public void valueChanged(final float value) {
      if (element.getId() != null) {
        nifty.publishEvent(element.getId(), new ScrollbarChangedEvent(value));
      }
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelY - elementBackground.getY();
    }
  }

  private class ScrollbarViewHorizontal implements ScrollbarView {
    private int minHandleSize;

    public ScrollbarViewHorizontal(final int minHandleSize) {
      this.minHandleSize = minHandleSize;
    }

    @Override
    public int getAreaSize() {
      return elementBackground.getWidth();
    }

    @Override
    public int getMinHandleSize() {
      return minHandleSize;
    }

    @Override
    public void setHandle(final int pos, final int size) {
      elementPosition.setConstraintX(new SizeValue(pos + "px"));
      elementPosition.setConstraintWidth(new SizeValue(size + "px"));
      elementBackground.layoutElements();
    }

    @Override
    public void valueChanged(final float value) {
      if (element.getId() != null) {
        nifty.publishEvent(element.getId(), new ScrollbarChangedEvent(value));
      }
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelX - elementBackground.getX();
    }
  }
}
