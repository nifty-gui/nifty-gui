package de.lessvoid.nifty.controls.scrollbar;


public class ScrollbarImpl {
  private ScrollbarView view;
  private float value;
  private float oldValue;
  private float min = 0.f;
  private float worldMax;
  private float viewMax;
  private float buttonStepSize;
  private float pageStepSize;
  private boolean moveTheHandle;
  private float moveTheHandleStartPos;

  public void bindToView(final ScrollbarView view, final float value, final float worldMax, final float viewMax, final float buttonStepSize, final float pageStepSize) {
    this.view = view;
    this.value = value;
    this.oldValue = -1;
    this.worldMax = worldMax;
    this.viewMax = viewMax;
    this.buttonStepSize = buttonStepSize;
    this.pageStepSize = pageStepSize;
    this.moveTheHandle = false;
    updateView();
    changeValue(this.value);
  }

  public void setup(final float value, final float worldMax, final float viewMax, final float buttonStepSize, final float pageStepSize) {
    this.value = value;
    this.worldMax = worldMax;
    this.viewMax = viewMax;
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

  public float getWorldMax() {
    return worldMax;
  }

  public void setWorldMax(final float max) {
    this.worldMax = max;
    changeValue(value);
    updateView();
  }

  public float getViewMax() {
    return viewMax;
  }

  public void setViewMax(final float viewMax) {
    this.viewMax = viewMax;
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
    int viewSize = view.getAreaSize();
    int handleSize = calcHandleSize(viewSize);
    int handlePosition = calcHandlePosition(viewSize, handleSize);
    if (hitsHandle(handlePosition, handleSize, viewValueClicked)) {
      moveTheHandle = true;
      moveTheHandleStartPos = viewToWorld(viewValueClicked, viewSize) - value;
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
    int viewSize = view.getAreaSize();
    float newPos = viewToWorld(viewValue, viewSize) - moveTheHandleStartPos;

    changeValue(newPos);
    updateView();
  }

  private boolean hitsHandle(final int handlePosition, final int handleSize, final int viewValueClicked) {
    return viewValueClicked > handlePosition && viewValueClicked < (handlePosition + handleSize);
  }

  private void changeValue(final float newValue) {
    int viewSize = view.getAreaSize();
    float handleSizeWorld = viewToWorld(calcHandleSize(viewSize), viewSize);

    value = newValue;
    if (value > (worldMax - handleSizeWorld)) {
      value = worldMax - handleSizeWorld;
    } else if (newValue < min) {
      value = min;
    }
    if (value != oldValue) {
      oldValue = value;
      view.valueChanged(value);
    }
  }

  private void updateView() {
    int viewSize = view.getAreaSize();
    int handleSize = calcHandleSize(viewSize);
    view.setHandle(calcHandlePosition(viewSize, handleSize), handleSize);
  }

  private int calcHandlePosition(final int viewSize, final int handleSize) {
    int viewMin = (int) Math.round(worldToView(value, viewSize));
    if (viewMin + handleSize > viewSize) {
      viewMin = viewSize - handleSize;
    }
    return viewMin;
  }

  private int calcHandleSize(final float viewSize) {
    int handleSize = (int) Math.round(viewSize / calcPageCount());
    int minHandleSize = view.getMinHandleSize(); 
    if (handleSize < minHandleSize) {
      return minHandleSize;
    }
    return handleSize;
  }

  private float calcPageCount() {
    float pages = worldMax / viewMax;
    if (pages < 1.0f) {
      pages = 1.0f;
    }
    return pages;
  }

  private float viewToWorld(final float value, final float viewSize) {
    return value / viewSize * worldMax;
  }

  private float worldToView(final float value, final float viewSize) {
    if (worldMax == 0.f) {
      return 0.f;
    }
    return value / worldMax * viewSize;
  }
}
