package de.lessvoid.nifty.controls.scrollbar.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.NextPrevHelper;
import de.lessvoid.nifty.controls.scrollbar.controller.impl.ScrollBarImpl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A slider control.
 * 
 * @author void
 */
public class GeneralScrollbar extends AbstractController {
    private Screen screen;

    private Element element;

    private ControllerEventListener listener;

    private Element scrollPos;

    private float viewMinValue;

    private float viewMaxValue;

    private float currentValue;

    private float worldMinValue;

    private float worldMaxValue;

    private float perClickChange = 1.0f;

    private int startMouse;

    private float pageSize;

    private ScrollBarImpl scrollBar;

    private NextPrevHelper nextPrevHelper;

    public void setScrollBarControlNotify(final ScrollbarControlNotify scrollBarControlNotifyParam) {
        listener = new ControllerEventListener() {
            public void onChangeNotify() {
                scrollBarControlNotifyParam.positionChanged(currentValue);
            }
        };
    }

    public GeneralScrollbar(final ScrollBarImpl scrollBarImplParam) {
        scrollBar = scrollBarImplParam;
    }

    public void bind(final Nifty newNifty, final Screen screenParam, final Element newElement,
            final Properties properties, final ControllerEventListener newListener,
            final Attributes controlDefinitionAttributes) {
        element = newElement;
        screen = screenParam;
        listener = newListener;

        viewMinValue = Float.parseFloat(properties.getProperty("viewMinValue", "0.0"));
        viewMaxValue = Float.parseFloat(properties.getProperty("viewMaxValue", "1.0"));
        worldMinValue = Float.parseFloat(properties.getProperty("worldMinValue", "0.0"));
        worldMaxValue = Float.parseFloat(properties.getProperty("worldMaxValue", "1.0"));
        currentValue = Float.parseFloat(properties.getProperty("currentValue", "0.0"));

        scrollPos = element.findElementByName("nifty-internal-scrollbar-position");
        element = element.findElementByName("nifty-internal-scrollbar-background");
        nextPrevHelper = new NextPrevHelper(element, screen.getFocusHandler());
    }

    public void onStartScreen() {
        calcPageSize();
        changeSliderPos(currentValue);
    }

    private void calcPageSize() {
        float worldSize = (worldMaxValue - worldMinValue);
        float pages = worldSize / (viewMaxValue - viewMinValue);

        if (pages < 1.0f) {
            pages = 1.0f;
        }

        pageSize = worldSize / pages;
        scrollBar.resizeHandle(
                scrollPos,
                new SizeValue((int) (scrollBar.translateValue(element.getWidth(),
                        element.getHeight()) / pages)
                        + "px"));
        Element parent = scrollPos.getParent();
        if (parent != null) {
            parent.layoutElements();
        }
    }

    /**
     * click.
     * 
     * @param mouseX the mouse x position
     * @param mouseY the mouse y position
     */
    public void click(final int mouseX, final int mouseY) {
        int mouseValue = scrollBar.translateValue(mouseX, mouseY);
        int scrollValue = scrollBar.translateValue(scrollPos.getX(), scrollPos.getY());
        if (mouseValue < scrollValue) {
            changeSliderPos(currentValue - pageSize);
        }
        int scrollSize = scrollBar.translateValue(scrollPos.getWidth(), scrollPos.getHeight());
        if (mouseValue > scrollValue + scrollSize) {
            changeSliderPos(currentValue + pageSize);
        }
    }

    public void mouseMoveStart(final int mouseX, final int mouseY) {
        startMouse = scrollBar.translateValue(mouseX, mouseY)
                - scrollBar.translateValue(scrollPos.getX(), scrollPos.getY());
    }

    /**
     * mouseMove.
     * 
     * @param mouseX the mouse x position
     * @param mouseY the mouse y position
     */
    public void mouseMove(final int mouseX, final int mouseY) {
        changeSliderPosFromMouse(mouseX, mouseY);
    }

    public void upClick(final int mouseX, final int mouseY) {
        currentValue -= perClickChange;
        changeSliderPos(currentValue);
    }

    public void downClick(final int mouseX, final int mouseY) {
        currentValue += perClickChange;
        changeSliderPos(currentValue);
    }

    /**
     * @param y TODO
     * @param mouseY
     */
    private void changeSliderPosFromMouse(final int mouseX, int mouseY) {
        // System.out.println("changeSliderPosFromMouse(" + mouseX + ", " + mouseY + ")");
        int newPos = scrollBar.translateValue(mouseX, mouseY)
                - scrollBar.translateValue(element.getX(), element.getY()) - startMouse;

        if (newPos < 0) {
            newPos = 0;
        }

        if (newPos > scrollBar.translateValue(element.getWidth(), element.getHeight())
                - scrollBar.translateValue(scrollPos.getWidth(), scrollPos.getHeight())) {
            newPos = scrollBar.translateValue(element.getWidth(), element.getHeight())
                    - scrollBar.translateValue(scrollPos.getWidth(), scrollPos.getHeight());
        }

        scrollBar.setPosition(scrollPos, newPos);

        currentValue = viewToWorld(newPos);
        if (listener != null) {
            listener.onChangeNotify();
        }
        element.layoutElements();
    }

    private float viewToWorld(float viewValue) {
        return ((viewValue - 0) / (scrollBar
                .translateValue(element.getWidth(), element.getHeight()) - 0))
                * (worldMaxValue - worldMinValue) + worldMinValue;
    }

    private float worldToView(float worldValue) {
        return ((worldValue - worldMinValue) / (worldMaxValue - worldMinValue))
                * (scrollBar.translateValue(element.getWidth(), element.getHeight()) - 0) + 0;
    }

    /**
     * Set the slider position from the given scroll bar value.
     * 
     * @param worldValue the current value of the scroll bar must be in range [minValue, maxValue]
     */
    public void changeSliderPos(final float worldValue) {
        if (scrollPos == null) {
            return;
        }

        this.currentValue = worldValue;
        if (currentValue < 0) {
            currentValue = 0;
        }

        int s = scrollBar.translateValue(element.getWidth(), element.getHeight())
                - scrollBar.translateValue(scrollPos.getWidth(), scrollPos.getHeight());
        float a = viewToWorld(s);
        if (currentValue > a) {
            currentValue = a;
        }

        float newPos = worldToView(currentValue);

        scrollBar.setPosition(scrollPos, (int) newPos);

        if (listener != null) {
            listener.onChangeNotify();
        }
        screen.layoutLayers();
    }

    /**
     * Get the current value of this scroll bar.
     * 
     * @return current value of scroll bar
     */
    public float getCurrentValue() {
        return currentValue;
    }

    @Override
    public void onFocus(final boolean getFocus) {
        super.onFocus(getFocus);
    }

    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        if (nextPrevHelper.handleNextPrev(inputEvent)) {
            return true;
        }
        if (inputEvent == NiftyInputEvent.MoveCursorUp
                || inputEvent == NiftyInputEvent.MoveCursorLeft) {
            changeSliderPos(currentValue - pageSize);
            return true;
        }
        else if (inputEvent == NiftyInputEvent.MoveCursorDown
                || inputEvent == NiftyInputEvent.MoveCursorRight) {
            changeSliderPos(currentValue + pageSize);
            return true;
        }
        return false;
    }

    public void setWorldMaxValue(float worldMaxValue) {
        this.worldMaxValue = worldMaxValue;
        calcPageSize();
    }

    public void setViewMaxValue(float viewMaxValue) {
        this.viewMaxValue = viewMaxValue;
        calcPageSize();
    }

    public float getWorldMinValue() {
        return worldMinValue;
    }

    public float getWorldMaxValue() {
        return worldMaxValue;
    }

    public float getViewMinValue() {
        return viewMinValue;
    }

    public float getViewMaxValue() {
        return viewMaxValue;
    }

    public void setPerClickChange(final float perClickChange) {
        this.perClickChange = perClickChange;
    }
}
