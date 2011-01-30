package de.lessvoid.nifty.controls.scrollbar;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.NextPrevHelper;
import de.lessvoid.nifty.controls.Scrollbar;
import de.lessvoid.nifty.controls.ScrollbarChangedEvent;
import de.lessvoid.nifty.elements.ControllerEventListener;
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
  private float max;
  private float initial;
  private float pageStepSize;
  private float buttonStepSize;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    super.bind(element);

    this.nifty = nifty;
    this.element = element;
    this.elementBackground = element.findElementByName("nifty-internal-scrollbar-background");
    this.elementPosition = element.findElementByName("nifty-internal-scrollbar-position");
    this.nextPrevHelper = new NextPrevHelper(element, screen.getFocusHandler());

    if ("verticalScrollbar".equals(parameter.getProperty("name"))) {
      scrollbarView = new ScrollbarViewVertical();
    } else if ("horizontalScrollbar".equals(parameter.getProperty("name"))) {
      scrollbarView = new ScrollbarViewHorizontal();
    }

    max = Float.valueOf(parameter.getProperty("max", "100.0"));
    initial = Float.valueOf(parameter.getProperty("initial", "0.0"));
    buttonStepSize = Float.valueOf(parameter.getProperty("buttonStepSize", "1.0"));
    pageStepSize = Float.valueOf(parameter.getProperty("pageStepSize", "25.0"));
  }

  @Override
  public void onStartScreen() {
    scrollbarImpl.bindToView(scrollbarView, initial, max, buttonStepSize, pageStepSize);
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

  public void mouseClick(final int mouseX, final int mouseY) {
  }

  // Scrollbar implementation

  @Override
  public void setup(final float value, final float max, final float buttonStepSize, final float pageStepSize) {
    scrollbarImpl.setup(value, max, buttonStepSize, pageStepSize);
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
  public void setMax(final float max) {
    scrollbarImpl.setMax(max);
  }

  @Override
  public float getMax() {
    return scrollbarImpl.getMax();
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
    @Override
    public int getSize() {
      return elementBackground.getHeight();
    }

    @Override
    public void setHandle(final int pos, final int size) {
      elementPosition.setConstraintY(new SizeValue(pos + "px"));
      elementPosition.setConstraintHeight(new SizeValue(size + "px"));
      elementBackground.layoutElements();
    }

    @Override
    public void valueChanged(final float value) {
      nifty.publishEvent(element.getId(), new ScrollbarChangedEvent(value));
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelY;
    }
  }

  private class ScrollbarViewHorizontal implements ScrollbarView {
    @Override
    public int getSize() {
      return elementBackground.getWidth();
    }

    @Override
    public void setHandle(final int pos, final int size) {
      elementPosition.setConstraintX(new SizeValue(pos + "px"));
      elementPosition.setConstraintWidth(new SizeValue(size + "px"));
      elementBackground.layoutElements();
    }

    @Override
    public void valueChanged(final float value) {
      nifty.publishEvent(element.getId(), new ScrollbarChangedEvent(value));
    }

    @Override
    public int filter(final int pixelX, final int pixelY) {
      return pixelX;
    }
  }
}
