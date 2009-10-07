package de.lessvoid.nifty.controls.listbox.controller;

import java.util.List;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.dynamic.LabelCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.scrollbar.controller.HorizontalScrollbarControl;
import de.lessvoid.nifty.controls.scrollbar.controller.ScrollbarControlNotify;
import de.lessvoid.nifty.controls.scrollbar.controller.VerticalScrollbarControl;
import de.lessvoid.nifty.elements.ControllerEventListener;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class ListBoxControl implements Controller {
  private Nifty nifty;
  private Screen screen;
  private Element element;
  private boolean verticalScrollbar;
  private boolean horizontalScrollbar;
  private String childRootId;
  private Element childRootElement;
  private Properties parameter;
  private float stepSizeX;
  private float stepSizeY;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element elementParam,
      final Properties parameterParam,
      final ControllerEventListener listener,
      final Attributes controlDefinitionAttributes) {
    nifty = niftyParam;
    screen = screenParam;
    element = elementParam;
    parameter = parameterParam;
    verticalScrollbar = new Boolean(parameter.getProperty("vertical", "true"));
    horizontalScrollbar = new Boolean(parameter.getProperty("horizontal", "true"));
    childRootId = controlDefinitionAttributes.get("childRootId");
    childRootElement = element.findElementByName(childRootId);

    ListBoxPanel listBoxPanel = getListBoxPanel();
    listBoxPanel.linkChildsToListBoxControl(this);
  }

  public void inputEvent(NiftyInputEvent inputEvent) {
  }

  public void onFocus(boolean getFocus) {
  }

  public void onStartScreen() {
    stepSizeX = new Float(parameter.getProperty("stepSizeX", "1.0"));

    ListBoxPanel listBoxPanel = getListBoxPanel();
    List < Element > listBoxElements = listBoxPanel.getListBoxElements();
    if (listBoxElements.isEmpty()) {
      stepSizeY = new Float(parameter.getProperty("stepSizeY", "1.0"));      
    } else {
      // that's just a hack for the moment to get the step size
      // for the scrollbar we'll simply use the height of the very
      // first element.
      stepSizeY = new Float(parameter.getProperty("stepSizeY", String.valueOf(listBoxElements.get(0).getHeight())));      
    }

    initializeScrollPanel(screen, stepSizeX, stepSizeY);
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
      if (!childRootElement.getElements().isEmpty()) {
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
                scrollElement.getParent().layoutElements();
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
                scrollElement.getParent().layoutElements();
              }
            });
          }
          scrollElement.setConstraintX(new SizeValue("0px"));
          scrollElement.setConstraintY(new SizeValue("0px"));
        }
      }
    }
    screen.layoutLayers();
  }

  public void changeSelection(final int newSelectedItemIndex) {
    ListBoxPanel listBoxPanel = getListBoxPanel();
    listBoxPanel.changeSelection(newSelectedItemIndex);
  }

  public void changeSelection(final Element element) {
    ListBoxPanel listBoxPanel = getListBoxPanel();
    listBoxPanel.changeSelection(element);
  }

  public int getSelectedItemIndex() {
    ListBoxPanel listBoxPanel = getListBoxPanel();
    return listBoxPanel.getSelectedItemIndex();
  }

  public Element getSelectedElement() {
    ListBoxPanel listBoxPanel = getListBoxPanel();
    return listBoxPanel.getSelectedElement();
  }

  public void setFocus() {
    childRootElement.setFocus();
  }

  private ListBoxPanel getListBoxPanel() {
    ListBoxPanel listBoxPanel = childRootElement.getControl(ListBoxPanel.class);
    return listBoxPanel;
  }

  public void addItem(final String text) {
    LabelCreator createLabel = new LabelCreator(text);
    createLabel.setStyle("nifty-listbox-item");

    ControlEffectAttributes effectParam = new ControlEffectAttributes();
    effectParam.setName("updateScrollpanelPositionToDisplayElement");
    effectParam.setAttribute("target", element.getId());
    effectParam.setAttribute("oneShot", "true");
    createLabel.addEffectsOnCustom(effectParam);

    Element listBoxDataParent = screen.findElementByName(element.getId() + "Data");
    Element newLabel = createLabel.create(nifty, screen, listBoxDataParent);

    // connect the new item with the parent ListBox because this is not happening automatically yet
    connectElement(newLabel);
  }

  public void addElement(final Element element) {
    connectElement(element);
  }

  private void connectElement(Element newLabel) {
    ListBoxControl listBox = screen.findControl(element.getId(), ListBoxControl.class);
    ListBoxItemController newLabelController = newLabel.getControl(ListBoxItemController.class);
    newLabelController.setListBox(listBox);
  }
}
