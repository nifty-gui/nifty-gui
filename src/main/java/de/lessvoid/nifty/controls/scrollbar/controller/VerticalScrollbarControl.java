package de.lessvoid.nifty.controls.scrollbar.controller;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.scrollbar.controller.impl.ScrollBarImplVertical;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class VerticalScrollbarControl extends AbstractController {
    private GeneralScrollbar scrollbar;

    public void bind(final Nifty newNifty, final Screen screenParam, final Element newElement,
            final Properties properties, final ControllerEventListener newListener,
            final Attributes controlDefinitionAttributes) {
        scrollbar = new GeneralScrollbar(new ScrollBarImplVertical());
        scrollbar.bind(newNifty, screenParam, newElement, properties, newListener,
                controlDefinitionAttributes);
    }

    public void onStartScreen() {
        scrollbar.onStartScreen();
    }

    public void click(final int mouseX, final int mouseY) {
        scrollbar.click(mouseX, mouseY);
    }

    public void mouseMoveStart(final int mouseX, final int mouseY) {
        scrollbar.mouseMoveStart(mouseX, mouseY);
    }

    public void mouseMove(final int mouseX, final int mouseY) {
        scrollbar.mouseMove(mouseX, mouseY);
    }

    public void upClick(final int mouseX, final int mouseY) {
        scrollbar.upClick(mouseX, mouseY);
    }

    public void downClick(final int mouseX, final int mouseY) {
        scrollbar.downClick(mouseX, mouseY);
    }

    @Override
    public void onFocus(final boolean getFocus) {
        super.onFocus(getFocus);
        scrollbar.onFocus(getFocus);
    }

    public boolean inputEvent(final NiftyInputEvent inputEvent) {
        return scrollbar.inputEvent(inputEvent);
    }

    public void setScrollBarControlNotify(final ScrollbarControlNotify scrollBarControlNotifyParam) {
        scrollbar.setScrollBarControlNotify(scrollBarControlNotifyParam);
    }

    public void setWorldMaxValue(final float worldMaxValue) {
        scrollbar.setWorldMaxValue(worldMaxValue);
    }

    public void setViewMaxValue(final float viewMaxValue) {
        scrollbar.setViewMaxValue(viewMaxValue);
    }

    public float getCurrentValue() {
        return scrollbar.getCurrentValue();
    }

    public void setCurrentValue(final float newValue) {
        scrollbar.changeSliderPos(newValue);
    }

    public float getWorldMinValue() {
        return scrollbar.getWorldMinValue();
    }

    public float getWorldMaxValue() {
        return scrollbar.getWorldMaxValue();
    }

    public float getViewMinValue() {
        return scrollbar.getViewMinValue();
    }

    public float getViewMaxValue() {
        return scrollbar.getViewMaxValue();
    }

    public void setPerClickChange(final float perClickChange) {
        scrollbar.setPerClickChange(perClickChange);
    }
}
