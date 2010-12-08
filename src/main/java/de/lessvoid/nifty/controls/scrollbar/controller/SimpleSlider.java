package de.lessvoid.nifty.controls.scrollbar.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
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
public class SimpleSlider extends AbstractController {
    private Screen screen;

    private Element element;

    private ControllerEventListener listener;

    private Element scrollPos;

    private float minValue;

    private float maxValue;

    private float currentValue;

    private boolean snap;

    private boolean fill;

    private boolean flip;

    public void bind(final Nifty niftyParam, final Screen screenParam, final Element newElement,
            final Properties properties, final ControllerEventListener newListener,
            final Attributes controlDefinitionAttributes) {
        this.element = newElement;
        this.screen = screenParam;
        this.listener = newListener;

        this.minValue = Float.parseFloat(properties.getProperty("minValue", "0.0"));
        this.maxValue = Float.parseFloat(properties.getProperty("maxValue", "1.0"));
        this.currentValue = Float.parseFloat(properties.getProperty("currentValue", "0.0"));
        this.snap = Boolean.parseBoolean(properties.getProperty("snap", "false"));
        this.fill = Boolean.parseBoolean(properties.getProperty("fill", "false"));
        this.flip = Boolean.parseBoolean(properties.getProperty("flip", "false"));
    }

    public void onStartScreen() {
        this.scrollPos = element.findElementByName("scrollposition");
        changeSliderPos(currentValue);
    }

    /**
     * click.
     * 
     * @param mouseX the mouse x position
     * @param mouseY the mouse y position
     */
    public void click(final int mouseX, final int mouseY) {
        changeSliderPosFromMouse(mouseY);
    }

    /**
     * mouseMove.
     * 
     * @param mouseX the mouse x position
     * @param mouseY the mouse y position
     */
    public void mouseMove(final int mouseX, final int mouseY) {
        changeSliderPosFromMouse(mouseY);
    }

    /**
     * @param y TODO
     */
    private void changeSliderPosFromMouse(final int y) {
        int mousePosY = y - element.getY();
        if (mousePosY < 0) {
            mousePosY = 1;
        }

        if (mousePosY >= element.getHeight()) {
            mousePosY = element.getHeight() - 1;
        }

        float newValue = (mousePosY) / (float) element.getHeight() * (maxValue - minValue);
        changeSliderPos(newValue);
    }

    /**
     * Set the slider position from the given scroll bar value.
     * 
     * @param newValue the current value of the scroll bar must be in range [minValue, maxValue]
     */
    private void changeSliderPos(float newValue) {
        if (scrollPos == null) {
            return;
        }

        if (flip) {
            newValue = (maxValue - minValue) - newValue;
        }

        this.currentValue = newValue;

        if (snap) {
            this.currentValue = (int) newValue;
        }

        float t = currentValue / (maxValue - minValue);
        float newPos = element.getHeight() * t;

        if (!fill) {
            if (!snap) {
                newPos -= scrollPos.getHeight() / 2;
            }
        }

        if (newPos < 0) {
            newPos = 0;
        }

        if (!fill) {
            if ((newPos + scrollPos.getHeight()) > element.getHeight()) {
                newPos = element.getHeight() - scrollPos.getHeight();
            }
        }

        if (!fill) {
            scrollPos.setConstraintX(new SizeValue(0 + "px"));
            scrollPos.setConstraintY(new SizeValue((int) newPos + "px"));
        }
        else {
            scrollPos.setConstraintX(new SizeValue("0px"));

            if (snap) {
                t = (currentValue + 1) / (maxValue - minValue);
                if (t > 1.0f) {
                    t = 1.0f;
                }
                newPos = element.getHeight() * t;
            }

            int y = (int) (element.getHeight() - newPos);
            scrollPos.setConstraintY(new SizeValue(y + "px"));
            int height = (int) (newPos);
            scrollPos.setConstraintHeight(new SizeValue(height + "px"));

            // ImageRenderer imageRenderer = scrollPos.getRenderer(ImageRenderer.class);
            // imageRenderer.getImage().setSubImage(
            // 0,
            // (int) (y / (float) element.getHeight() * imageRenderer.getImage().getHeight()),
            // scrollPos.getConstraintWidth().getValueAsInt(0),
            // (int) ((height) / (float) element.getHeight() * imageRenderer.getImage().getHeight())
            // );
        }

        listener.onChangeNotify();
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
    }

    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        return false;
    }
}
