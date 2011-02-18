package de.lessvoid.nifty.controls;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

public abstract class AbstractController implements Controller, NiftyControl {

    private List<FocusNotify> notifies = new CopyOnWriteArrayList<FocusNotify>();

    @Override
    public void onFocus(boolean getFocus) {
        if (getFocus) {
            notifyObserversFocusGained();
        }
        else {
            notifyObserversFocusLost();
        }
    }

    public void addNotify(FocusNotify notify) {
        notifies.add(notify);
    }

    public void removeNotify(FocusNotify notify) {
        notifies.remove(notify);
    }

    public void removeAllNotifies() {
        notifies.clear();
    }

    private void notifyObserversFocusGained() {
        for (FocusNotify notify : notifies) {
            notify.focusGained(this);
        }
    }

    private void notifyObserversFocusLost() {
        for (FocusNotify notify : notifies) {
            notify.focusLost(this);
        }
    }

    // NEW
    private Element element;

    protected void bind(final Element element) {
      this.element = element;
    }

    protected Element getElement() {
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
}
