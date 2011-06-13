package de.lessvoid.nifty.controls.radiobutton;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.RadioButton;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * RadioButtonControl implementation.
 * @deprecated Please use {@link de.lessvoid.nifty.controls.RadioButton} when accessing NiftyControls.
 */
@Deprecated
public class RadioButtonControl extends AbstractController implements RadioButton {

  @Override
  public void bind(final Nifty nifty, final Screen screen, final Element element, final Properties parameter, final Attributes controlDefinitionAttributes) {
  }

  @Override
  public void onStartScreen() {
  }

  @Override
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    return false;
  }

  @Override
  public void setGroup(final String groupId) {
  }

  @Override
  public String getGroup() {
    return null;
  }

  @Override
  public void activate() {
  }

  @Override
  public boolean isActivated() {
    return false;
  }

  @Override
  public String getGroupActive() {
    return null;
  }
}
