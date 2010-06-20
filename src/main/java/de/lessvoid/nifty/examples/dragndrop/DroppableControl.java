package de.lessvoid.nifty.examples.dragndrop;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class DroppableControl implements Controller {
  @Override
  public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, ControllerEventListener listener, Attributes controlDefinitionAttributes) {
  }
  
  @Override
  public void inputEvent(NiftyInputEvent inputEvent) {
  }
  
  @Override
  public void onFocus(boolean getFocus) {
  }
  
  @Override
  public void onStartScreen() {
  }
}