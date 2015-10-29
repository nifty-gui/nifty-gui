package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public abstract class AbstractController implements Controller, NiftyControl {
  private static final Logger log = Logger.getLogger(AbstractController.class.getName());
  @Nullable
  private Element element;
  private boolean bound = false;

  protected void bind(@Nonnull final Element element) {
    this.element = element;
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    this.bound = true;
  }

  @Nullable
  @Override
  public Element getElement() {
    if (element == null) {
      log.warning("Requested element from controller before binding was performed.");
    }
    return element;
  }

  @Override
  public void enable() {
    setEnabled(true);
  }

  @Override
  public void disable() {
    setEnabled(false);
  }

  @Override
  public void setEnabled(final boolean enabled) {
    final Element element = getElement();
    if (element != null) {
      if (enabled) {
        element.enable();
      } else {
        element.disable();
      }
    }
  }

  @Override
  public boolean isEnabled() {
    final Element element = getElement();
    return element != null && element.isEnabled();
  }

  @Nullable
  @Override
  public String getId() {
    final Element element = getElement();
    return element != null ? element.getId() : null;
  }

  @Override
  public void setId(@Nullable final String id) {
    final Element element = getElement();
    if (element != null) {
      element.setId(id);
    }
  }

  @Override
  public int getWidth() {
    final Element element = getElement();
    return element != null ? element.getWidth() : 0;
  }

  @Override
  public void setWidth(@Nonnull final SizeValue width) {
    final Element element = getElement();
    if (element != null) {
      element.setConstraintWidth(width);
    }
  }

  @Override
  public int getHeight() {
    final Element element = getElement();
    return element != null ? element.getHeight() : 0;
  }

  @Override
  public void setHeight(@Nonnull final SizeValue height) {
    final Element element = getElement();
    if (element != null) {
      element.setConstraintHeight(height);
    }
  }

  @Override
  public String getStyle() {
    final Element element = getElement();
    return element != null ? element.getStyle() : null;
  }

  @Override
  public void setStyle(@Nonnull final String style) {
    final Element element = getElement();
    if (element != null) {
      element.setStyle(element.getNifty().specialValuesReplace(style));
    }
  }

  @Override
  public void setFocus() {
    final Element element = getElement();
    if (element != null) {
      element.setFocus();
    }
  }

  @Override
  public void setFocusable(final boolean focusable) {
    final Element element = getElement();
    if (element != null) {
      element.setFocusable(focusable);
    }
  }

  @Override
  public void onFocus(final boolean getFocus) {
    final Element element = getElement();
    if (element != null) {
      String id = element.getId();
      if (id != null) {
        if (getFocus) {
          element.getNifty().publishEvent(id, new FocusGainedEvent(this, this));
        } else {
          element.getNifty().publishEvent(id, new FocusLostEvent(this, this));
        }
      }
    }
  }

  @Override
  public boolean hasFocus() {
    final Element element = getElement();
    if (element == null) {
      return false;
    }
    return element == element.getFocusHandler().getKeyboardFocusElement();
  }

  @Override
  public void layoutCallback() {
  }

  @Override
  public boolean isBound() {
    return bound;
  }

  @Override
  public void onEndScreen() {
  }

}
