package de.lessvoid.nifty.controls.dragndrop.controller;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class DroppableControl extends AbstractController {

    private Nifty nifty;

    private Screen screen;

    private List<DropNotify> notifies = new CopyOnWriteArrayList<DropNotify>();

    private List<DropFilter> filters = new CopyOnWriteArrayList<DropFilter>();

    private Element droppable;

    private DraggableControl draggable;

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter,
            ControllerEventListener listener, Attributes controlDefinitionAttributes) {
        this.nifty = nifty;
        this.screen = screen;
        droppable = element;

        addOnDropMethodNotify(controlDefinitionAttributes.get("onDrop"));
    }

    private void addOnDropMethodNotify(String methodName) {
        if (methodName != null) {
            addNotify(new OnDropMethodNotify(methodName));
        }
    }

    @Override
    public boolean inputEvent(NiftyInputEvent inputEvent) {
        return false;
    }

    @Override
    public void onFocus(boolean getFocus) {
        super.onFocus(getFocus);
    }

    @Override
    public void onStartScreen() {
        draggable = findDraggableChild(droppable);
        if (draggable != null) {
            drop(draggable, reactivate(draggable.getElement()), false);
        }
    }

    private EndNotify reactivate(final Element element) {
        return new EndNotify() {

            @Override
            public void perform() {
                element.reactivate();
            }
        };
    }

    private DraggableControl findDraggableChild(Element element) {
        for (Element child : element.getElements()) {
            if (isDraggable(child)) {
                return child.getControl(DraggableControl.class);
            }
            DraggableControl draggable = findDraggableChild(child);
            if (draggable != null) {
                return draggable;
            }
        }
        return null;
    }

    private boolean isDraggable(Element element) {
        NiftyInputControl control = element.getAttachedInputControl();
        if (control != null) {
            return control.getController() instanceof DraggableControl;
        }
        return false;
    }

    protected void drop(DraggableControl droppedDraggable, EndNotify endNotify) {
        drop(droppedDraggable, endNotify, true);
    }

    private void drop(DraggableControl droppedDraggable, EndNotify endNotify,
            boolean notifyObservers) {
        draggable = droppedDraggable;
        draggable.getElement().setConstraintX(new SizeValue("0px"));
        draggable.getElement().setConstraintY(new SizeValue("0px"));
        draggable.getElement().markForMove(droppable, endNotify);

        DroppableControl source = droppedDraggable.getDroppable();
        droppedDraggable.setDroppable(this);

        if (notifyObservers) {
            notifyObservers(source, droppedDraggable);
        }
    }

    public DraggableControl getDraggable() {
        return draggable;
    }

    public void addNotify(DropNotify notify) {
        notifies.add(notify);
    }

    public void removeNotify(DropNotify notify) {
        notifies.remove(notify);
    }

    @Override
    public void removeAllNotifies() {
        notifies.clear();
    }

    private void notifyObservers(DroppableControl source, DraggableControl droppedDraggable) {
        for (DropNotify notify : notifies) {
            notify.dropped(source, droppedDraggable, this);
        }
    }

    public void addFilter(DropFilter filter) {
        filters.add(filter);
    }

    public void removeFilter(DropFilter filter) {
        filters.remove(filter);
    }

    public void removeAllFilters() {
        filters.clear();
    }

    protected boolean accept(DroppableControl source, DraggableControl draggable) {
        for (DropFilter filter : filters) {
            if (!filter.accept(source, draggable, this)) {
                return false;
            }
        }
        return true;
    }

    public Element getElement() {
        return droppable;
    }

    private class OnDropMethodNotify implements DropNotify {

        private String methodName;

        public OnDropMethodNotify(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public void dropped(DroppableControl source, DraggableControl draggable,
                DroppableControl target) {
            NiftyMethodInvoker methodInvoker = new NiftyMethodInvoker(nifty, methodName,
                    screen.getScreenController());
            methodInvoker.invoke(source, draggable, target);
        }
    }
}
