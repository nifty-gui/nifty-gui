package de.lessvoid.nifty.controls;

import java.util.Properties;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public abstract class AbstractController implements Controller, NiftyControl {
    private Element element;
    private boolean bound = false;

    protected void bind(final Element element) {
      this.element = element;
    }

    @Override
    public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
      this.bound = true;
    }

    @Override
    public Element getElement() {
      return element;
    }

    @Override
    public void enable() {
      element.enable();
    }

    @Override
    public void disable() {
      element.disable();
    }

    @Override
    public void setEnabled(final boolean enabled) {
      if (enabled) {
        element.enable();
      } else {
        element.disable();
      }
    }

    @Override
    public boolean isEnabled() {
      return element.isEnabled();
    }

    @Override
    public String getId() {
      return element.getId();
    }

    @Override
    public void setId(final String id) {
      element.setId(id);
    }

    @Override
    public int getWidth() {
      return element.getWidth();
    }

    @Override
    public void setWidth(final SizeValue width) {
      element.setConstraintWidth(width);
    }

    @Override
    public int getHeight() {
      return element.getHeight();
    }

    @Override
    public void setHeight(final SizeValue height) {
      element.setConstraintHeight(height);
    }

    @Override
    public String getStyle() {
      return element.getStyle();
    }

    @Override
    public void setStyle(final String style) {
     element.setStyle(style); 
    }

    @Override
    public void setFocus() {
      element.setFocus();
    }

    @Override
    public void setFocusable(final boolean focusable) {
      element.setFocusable(focusable);
    }

    @Override
    public void onFocus(final boolean getFocus) {
      if (element == null) {
        return;
      }
      if (getFocus) {
        element.getNifty().publishEvent(element.getId(), new FocusGainedEvent(this, this));
      } else {
        element.getNifty().publishEvent(element.getId(), new FocusLostEvent(this, this));
      }
    }

    @Override
    public boolean hasFocus() {
      if (getElement() == null) {
        return false;
      }
      return getElement() == getElement().getFocusHandler().getKeyboardFocusElement();
    }

    @Override
    public void layoutCallback() {
    }

    @Override
    public boolean isBound() {
      return bound;
    }
}
