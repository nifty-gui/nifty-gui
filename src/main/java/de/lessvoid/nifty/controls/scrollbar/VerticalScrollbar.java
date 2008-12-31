package de.lessvoid.nifty.controls.scrollbar;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.scrollbar.impl.ScrollBarImpl;
import de.lessvoid.nifty.controls.scrollbar.impl.ScrollBarImplVertical;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.loader.xpp3.Attributes;
import de.lessvoid.nifty.screen.Screen;

public class VerticalScrollbar implements Controller {
  private GeneralScrollbar scrollbar;

  public void bind(
      final Nifty newNifty,
      final Element newElement,
      final Properties properties,
      final ControllerEventListener newListener,
      final Attributes controlDefinitionAttributes) {
    scrollbar = new GeneralScrollbar(new ScrollBarImplVertical());
    scrollbar.bind(newNifty, newElement, properties, newListener, controlDefinitionAttributes);
  }

  /**
   * On start screen event.
   */
  public void onStartScreen(final Screen screenParam) {
    scrollbar.onStartScreen(screenParam);
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

  public void onFocus(final boolean getFocus) {
    scrollbar.onFocus(getFocus);
  }

  public void inputEvent(final NiftyInputEvent inputEvent) {
    scrollbar.inputEvent(inputEvent);
  }

  public void setScrollBarControlNotify(final ScrollbarControlNotify scrollBarControlNotifyParam) {
    scrollbar.setScrollBarControlNotify(scrollBarControlNotifyParam);
  }

  public void setWorldMaxValue(float worldMaxValue) {
    scrollbar.setWorldMaxValue(worldMaxValue);
  }

  public void setViewMaxValue(float viewMaxValue) {
    scrollbar.setViewMaxValue(viewMaxValue);
  }
}
