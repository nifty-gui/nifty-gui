package de.lessvoid.nifty.controls;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.lessvoid.nifty.elements.Element;

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
    public boolean isEnabled() {
      return element.isEnabled();
    }

}
