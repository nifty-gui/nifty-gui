package de.lessvoid.nifty.examples.defaultcontrols.scrollpanel;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.ScrollPanel.AutoScroll;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The ScrollPanelDialogController.
 *
 * @author void
 */
public class ScrollPanelDialogController implements Controller {
  private Screen screen;

  @Override
  public void bind(
      @Nonnull final Nifty nifty,
      @Nonnull final Screen screen,
      @Nonnull final Element element,
      @Nonnull final Parameters parameter) {
    this.screen = screen;
  }

  @Override
  public void init(@Nonnull final Parameters parameter) {
    ScrollPanel scrollPanel = getScrollPanel();
    scrollPanel.setUp(10.f, 10.f, 100.f, 100.f, AutoScroll.OFF);
    getScrollPanelXPosTextField().setText(String.valueOf((int) scrollPanel.getHorizontalPos()));
    getScrollPanelYPosTextField().setText(String.valueOf((int) scrollPanel.getVerticalPos()));
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onFocus(final boolean getFocus) {
  }

  @Override
  public boolean inputEvent(@Nonnull final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void onEndScreen() {

  }

  @NiftyEventSubscriber(id = "scrollPanel")
  public void onScrollPanelChange(final String id, @Nonnull final ScrollPanelChangedEvent event) {
    getScrollPanelXPosTextField().setText(String.valueOf((int) event.getX()));
    getScrollPanelYPosTextField().setText(String.valueOf((int) event.getY()));
  }

  @NiftyEventSubscriber(id = "scrollpanelXPos")
  public void onScrollpanelXPosChanged(final String id, @Nonnull final TextFieldChangedEvent event) {
    try {
      float f = Float.valueOf(event.getText());
      ScrollPanel scrollPanel = getScrollPanel();
      scrollPanel.setHorizontalPos(f);
    } catch (NumberFormatException e) {
    }
  }

  @NiftyEventSubscriber(id = "scrollpanelYPos")
  public void onScrollpanelYPosChanged(final String id, @Nonnull final TextFieldChangedEvent event) {
    try {
      float f = Float.valueOf(event.getText());
      ScrollPanel scrollPanel = getScrollPanel();
      scrollPanel.setVerticalPos(f);
    } catch (NumberFormatException e) {
    }
  }

  @Nullable
  private ScrollPanel getScrollPanel() {
    return screen.findNiftyControl("scrollPanel", ScrollPanel.class);
  }

  @Nullable
  private TextField getScrollPanelYPosTextField() {
    return screen.findNiftyControl("scrollpanelYPos", TextField.class);
  }

  @Nullable
  private TextField getScrollPanelXPosTextField() {
    return screen.findNiftyControl("scrollpanelXPos", TextField.class);
  }
}
