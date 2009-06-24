package de.lessvoid.nifty.controls.listbox;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.scrollbar.controller.HorizontalScrollbarControl;
import de.lessvoid.nifty.controls.scrollbar.controller.ScrollbarControlNotify;
import de.lessvoid.nifty.controls.scrollbar.controller.VerticalScrollbarControl;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ListBox implements Controller {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private boolean verticalScrollbar;
  private boolean horizontalScrollbar;
  private String childRootId;
  private Element childRootElement;
  private float stepSizeX;
  private float stepSizeY;
  private FocusHandler focusHandler;
  private int selectedItem;
  private int maxSelectedItems;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameter,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    screen = screenParam;
    element = elementParam;
    verticalScrollbar = new Boolean(parameter.getProperty("vertical", "true"));
    horizontalScrollbar = new Boolean(parameter.getProperty("horizontal", "true"));
    childRootId = controlDefinitionAttributes.get("childRootId");
    childRootElement = element.findElementByName(childRootId);
    stepSizeX = new Float(parameter.getProperty("stepSizeX", "1.0"));
    stepSizeY = new Float(parameter.getProperty("stepSizeY", "1.0"));
    selectedItem = 0;
    maxSelectedItems = 0;
  }

  public void inputEvent(NiftyInputEvent inputEvent) {
	  if (inputEvent == NiftyInputEvent.NextInputElement) {
	        if (focusHandler != null) {
	          Element nextElement = focusHandler.getNext(element);
	          nextElement.setFocus();
	        }
	      } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
	        if (focusHandler != null) {
	          Element prevElement = focusHandler.getPrev(element);
	          prevElement.setFocus();
	        }
	      } else if (inputEvent == NiftyInputEvent.MoveCursorDown) {
	    	  if (selectedItem < maxSelectedItems - 1) {
	    		  changeSelection(selectedItem + 1);
	    	  }
	      } else if (inputEvent == NiftyInputEvent.MoveCursorUp) {
	    	  if (selectedItem > 0) {
	    		  changeSelection(selectedItem - 1);
	    	  }
	      }
  }
  
  public void onFocus(boolean getFocus) {
  }

  public void onStartScreen() {
    initializeScrollPanel(screen, stepSizeX, stepSizeY);
    focusHandler = screen.getFocusHandler();
  }

  public void initializeScrollPanel(final Screen screen, final float stepSizeX, final float stepSizeY) {
    this.stepSizeX = stepSizeX;
    this.stepSizeY = stepSizeY;

    if (!verticalScrollbar) {
      Element vertical = element.findElementByName("nifty-internal-vertical-scrollbar");
      if (vertical != null) {
        nifty.removeElement(screen, vertical);
      }
    }
    if (!horizontalScrollbar) {
      Element horizontal = element.findElementByName("nifty-internal-horizonal-panel");
      if (horizontal != null) {
        nifty.removeElement(screen, horizontal);
      }
    }

    nifty.removeElements();
    screen.layoutLayers();

    if (childRootElement != null) {
      final Element scrollElement = childRootElement.getElements().get(0);
      if (scrollElement != null) {
        HorizontalScrollbarControl horizontalS = element.findControl("nifty-internal-horizontal-scrollbar", HorizontalScrollbarControl.class);
        if (horizontalS != null) {
          horizontalS.setWorldMaxValue(scrollElement.getWidth());
          horizontalS.setViewMaxValue(childRootElement.getWidth());
          horizontalS.setPerClickChange(stepSizeX);
          horizontalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
            public void positionChanged(final float currentValue) {
              scrollElement.setConstraintX(new SizeValue(-(int)currentValue + "px"));
              screen.layoutLayers();
            }
          });
        }
  
        VerticalScrollbarControl verticalS = element.findControl("nifty-internal-vertical-scrollbar", VerticalScrollbarControl.class);
        if (verticalS != null) {
          verticalS.setWorldMaxValue(scrollElement.getHeight());
          verticalS.setViewMaxValue(childRootElement.getHeight());
          verticalS.setPerClickChange(stepSizeY);
          verticalS.setScrollBarControlNotify(new ScrollbarControlNotify() {
            public void positionChanged(final float currentValue) {
              scrollElement.setConstraintY(new SizeValue(-(int)currentValue + "px"));
              screen.layoutLayers();
            }
          });
        }
  
        scrollElement.setConstraintX(new SizeValue("0px"));
        scrollElement.setConstraintY(new SizeValue("0px"));

        selectedItem = 0;
        maxSelectedItems = scrollElement.getElements().size();
        updateSelection(selectedItem);
      }
    }
  }

  private Element getScrollElement() {
	  if (childRootElement != null) {
		  return childRootElement.getElements().get(0);
	  }
	  return null;
  }

  private void updateSelection(final int selectedItem) {
	  Element scrollElement = getScrollElement();
	  if (scrollElement != null) {
		  scrollElement.getElements().get(selectedItem).startEffect(EffectEventId.onCustom, null);
		  this.selectedItem = selectedItem;
	  }
  }

  public void changeSelection(final int newSelectedItemIndex) {
	  Element scrollElement = getScrollElement();
	  if (scrollElement != null) {
		  scrollElement.getElements().get(selectedItem).stopEffect(EffectEventId.onCustom);
		  updateSelection(newSelectedItemIndex);
	  }
  }
}
