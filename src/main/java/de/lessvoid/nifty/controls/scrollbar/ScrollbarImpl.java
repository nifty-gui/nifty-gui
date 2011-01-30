package de.lessvoid.nifty.controls.scrollbar;


public class ScrollbarImpl {
  private ScrollbarView view;
  private float value;
  private float oldValue;
  private float min = 0.f;
  private float max;
  private float buttonStepSize;
  private float pageStepSize;
  private boolean moveTheHandle;
  private int moveTheHandleStartPos;

  public void bindToView(final ScrollbarView view, final float value, final float max, final float buttonStepSize, final float pageStepSize) {
    this.view = view;
    this.value = value;
    this.oldValue = -1;
    this.max = max;
    this.buttonStepSize = buttonStepSize;
    this.pageStepSize = pageStepSize;
    this.moveTheHandle = false;
    updateView();
    changeValue(this.value);
  }

  public void setup(final float value, final float max, final float buttonStepSize, final float pageStepSize) {
    this.value = value;
    this.max = max;
    this.buttonStepSize = buttonStepSize;
    this.pageStepSize = pageStepSize;
    changeValue(value);
    updateView();
  }

  public void setValue(final float value) {
    changeValue(value);
    updateView();
  }

  public float getValue() {
    return value;
  }

  public void stepUp() {
    changeValue(value + buttonStepSize);
    updateView();
  }

  public void stepDown() {
    changeValue(value - buttonStepSize);
    updateView();
  }

  public void stepPageUp() {
    changeValue(value + pageStepSize);
    updateView();
  }

  public void stepPageDown() {
    changeValue(value - pageStepSize);
    updateView();
  }

  public float getMax() {
    return max;
  }

  public void setMax(final float max) {
    this.max = max;
    changeValue(value);
    updateView();
  }

  public float getButtonStepSize() {
    return buttonStepSize;
  }

  public void setButtonStepSize(final float buttonStepSize) {
    this.buttonStepSize = buttonStepSize;
    changeValue(value);
    updateView();
  }

  public float getPageStepSize() {
    return pageStepSize;
  }

  public void setPageStepSize(final float pageStepSize) {
    this.pageStepSize = pageStepSize;
  }

  public void interactionClick(final int viewValueClicked) {
    int viewSize = view.getSize();
    int handleSize = calcHandleSize(viewSize);
    int handlePosition = calcHandlePosition(viewSize, handleSize);

    if (hitsHandle(handlePosition, handleSize, viewValueClicked)) {
      moveTheHandle = true;
      moveTheHandleStartPos = viewValueClicked;
    } else {
      moveTheHandle = false;
      if (viewValueClicked < handlePosition && viewValueClicked > 0) {
        stepPageDown();
      } else if (viewValueClicked > (handlePosition + handleSize) && viewValueClicked < viewSize) {
        stepPageUp();
      }
    }
  }

  public void interactionMove(final int viewValue) {
    if (!moveTheHandle) {
      return;
    }
    int viewSize = view.getSize();
    float startPos = viewToWorld(moveTheHandleStartPos, viewSize);
    float newPos = viewToWorld(viewValue, viewSize);
    float delta = newPos - startPos;
    changeValue(value + delta);
    updateView();
  }

  private boolean hitsHandle(final int handlePosition, final int handleSize, final int viewValueClicked) {
    return viewValueClicked > handlePosition && viewValueClicked < handlePosition + handleSize;
  }

  private void changeValue(final float newValue) {
    value = newValue;    
    if (value > max) {
      value = max;
    } else if (newValue < min) {
      value = min;
    }
    if (value != oldValue) {
      oldValue = value;
      view.valueChanged(value);
    }
  }

  private void updateView() {
    int viewSize = view.getSize();
    int handleSize = calcHandleSize(viewSize);
    view.setHandle(calcHandlePosition(viewSize, handleSize), handleSize);
  }

  private int calcHandlePosition(final int viewSize, final int handleSize) {
    int viewMin = (int) Math.floor(worldToView(value, viewSize));
    if (viewMin + handleSize > viewSize) {
      viewMin = viewSize - handleSize;
    }
    return viewMin;
  }

  private int calcHandleSize(final float viewSize) {
    return (int) Math.floor(viewSize / calcPageCount(viewSize));
  }

  private float calcPageCount(final float viewSize) {
    float pages = max / viewSize;
    if (pages < 1.0f) {
      pages = 1.0f;
    }
    return pages;
  }

  private float viewToWorld(final float value, final float viewSize) {
    return value / viewSize * max;
  }

  private float worldToView(final float value, final float viewSize) {
    if (max == 0.f) {
      return 0.f;
    }
    return value / max * viewSize;
  }
}
