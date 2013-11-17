package de.lessvoid.nifty.examples.defaultcontrols.scrollpanel;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.controls.ScrollPanel.AutoScroll;
import de.lessvoid.nifty.controls.ScrollPanelChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

/**
 * The ScrollPanelDialogController.
 * @author void
 */
public class ScrollPanelDialogController implements Controller {
  private Screen screen;

  @Override
  public void bind(
      final Nifty nifty,
      final Screen screen,
      final Element element,
      final Parameters parameter) {
    this.screen = screen;
  }

  @Override
  public void init(final Parameters parameter) {
    ScrollPanel scrollPanel = getScrollPanel();
    scrollPanel.setUp(10.f, 10.f, 100.f, 100.f, AutoScroll.OFF);
    getScrollPanelXPosTextField().setText(String.valueOf((int)scrollPanel.getHorizontalPos()));
    getScrollPanelYPosTextField().setText(String.valueOf((int)scrollPanel.getVerticalPos()));
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @NiftyEventSubscriber(id="scrollPanel")
  public void onScrollPanelChange(final String id, final ScrollPanelChangedEvent event) {
    getScrollPanelXPosTextField().setText(String.valueOf((int)event.getX()));
    getScrollPanelYPosTextField().setText(String.valueOf((int)event.getY()));
  }

  @NiftyEventSubscriber(id="scrollpanelXPos")
  public void onScrollpanelXPosChanged(final String id, final TextFieldChangedEvent event) {
    try {
      float f = Float.valueOf(event.getText());
      ScrollPanel scrollPanel = getScrollPanel();
      scrollPanel.setHorizontalPos(f);
    } catch (NumberFormatException e) {
    }
  }

  @NiftyEventSubscriber(id="scrollpanelYPos")
  public void onScrollpanelYPosChanged(final String id, final TextFieldChangedEvent event) {
    try {
      float f = Float.valueOf(event.getText());
      ScrollPanel scrollPanel = getScrollPanel();
      scrollPanel.setVerticalPos(f);
    } catch (NumberFormatException e) {
    }
  }

  private ScrollPanel getScrollPanel() {
    return screen.findNiftyControl("scrollPanel", ScrollPanel.class);
  }

  private TextField getScrollPanelYPosTextField() {
    return screen.findNiftyControl("scrollpanelYPos", TextField.class);
  }

  private TextField getScrollPanelXPosTextField() {
    return screen.findNiftyControl("scrollpanelXPos", TextField.class);
  }
}
