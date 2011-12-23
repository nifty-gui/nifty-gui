/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs;

import java.util.List;
import java.util.Properties;

import org.bushe.swing.event.EventTopicSubscriber;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.TabSelectedEvent;
import de.lessvoid.nifty.controls.Tabs;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementShowEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 *
 * @author ractoc
 */
public class TabsControl extends AbstractController implements Tabs, EventTopicSubscriber<ElementShowEvent> {

    private Nifty nifty;
    private static String activeTab;
    private Element elmnt;
    private String buttonWidth;
    private String buttonHeight;

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        super.bind(element);
        this.nifty = nifty;
        this.elmnt = element;
        if (controlDefinitionAttributes.isSet("buttonWidth")) {
            buttonWidth = controlDefinitionAttributes.get("buttonWidth");
        }
        if (controlDefinitionAttributes.isSet("buttonHeight")) {
            element.findElementByName("#tab-button-panel").setConstraintHeight(new SizeValue(controlDefinitionAttributes.get("buttonHeight")));
            int pixelHeight = 0;
            if (controlDefinitionAttributes.get("buttonHeight").endsWith("px")) {
            	pixelHeight = Integer.parseInt(controlDefinitionAttributes.get("buttonHeight").substring(0, controlDefinitionAttributes.get("buttonHeight").length() - 2));
            } else if (controlDefinitionAttributes.get("buttonHeight").endsWith("%")) {
            	int percentageHeight = Integer.parseInt(controlDefinitionAttributes.get("buttonHeight").substring(0, controlDefinitionAttributes.get("buttonHeight").length() - 1));
            	pixelHeight = element.getParent().getHeight() / 100 * percentageHeight;
            }
            element.findElementByName("#tab-button-panel").setHeight(pixelHeight);
            buttonHeight = controlDefinitionAttributes.get("buttonHeight");
        }
        nifty.subscribe(screen, getId(), ElementShowEvent.class, this);
        element.layoutElements();
    }

    @Override
    public void init(Properties prprts, Attributes atrbts) {
        Element tabContentPanel = elmnt.findElementByName("#tab-content-panel");
        List<Element> elements = tabContentPanel.getElements();
        for (final Element e : elements) {
            if (activeTab
                    == null || activeTab.length()
                    == 0) {
                activeTab = e.getId();
                createTabButton(e.getId(), e.getControl(TabControl.class).getCaption());
            } else {
                e.hideWithoutEffect();
                createTabButton(e.getId(), e.getControl(TabControl.class).getCaption());
            }
        }
    }

    private void createTabButton(final String tabId, final String buttonCaption) {
        Element tabButtonPanel = elmnt.findElementByName("#tab-button-panel");
        if (tabButtonPanel.getHeight() == 0) {
        	tabButtonPanel.setConstraintHeight(new SizeValue(buttonHeight));
        }
        if (tabButtonPanel.findElementByName(tabId + "-button") == null) {
            new ButtonBuilder(tabId + "-button") {

                {
                    style("nifty-button");
                    childLayout(ChildLayoutType.Horizontal);
                    interactOnClick("switchTab(" + tabId + ")");
                    if (buttonWidth != null) {
                        width(buttonWidth);
                    }
                    if (buttonHeight != null) {
                        height(percentage(100));
                    } else {
                        height("25px");
                    }
                    label(buttonCaption);
                }
            }.build(nifty, nifty.getCurrentScreen(), tabButtonPanel);
        }
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public boolean inputEvent(NiftyInputEvent inputEvent) {
        return true;
    }

    @Override
    public void addTab(Element tab) {
        if (tab.getControl(TabControl.class) != null) {
            elmnt.findElementByName("#tab-content-panel").add(tab);
            createTabButton(tab.getId(), tab.getControl(TabControl.class).getCaption());
            setSelectedTab(tab.getId());
            elmnt.layoutElements();
        } else {
            throw new IllegalArgumentException("Expected an Element with a TabControl.");
        }
    }

    @Override
    public void removeTab(int index) {
        removeTab(elmnt.findElementByName("#tab-button-panel").getElements().get(index).getId());
    }

    public void removeTab(String tabId) {
        // if we remove a tab the active tab is reset to the first tab, after removal of the indicated tab.
        if (tabId.equals(activeTab)) {
            if (elmnt.findElementByName("#tab-button-panel").getElements().size() > 1) {
                if (elmnt.findElementByName("#tab-button-panel").getElements().get(0).getId().equals(tabId)) {
                    setSelectedTab(elmnt.findElementByName("#tab-button-panel").getElements().get(1).getId());
                } else {
                    setSelectedTab(elmnt.findElementByName("#tab-button-panel").getElements().get(0).getId());
                }
            }
        }
        elmnt.findElementByName("#tab-button-panel").findElementByName(tabId + "-button").markForRemoval();
        elmnt.findElementByName("#tab-content-panel").findElementByName(tabId).markForRemoval();
        elmnt.layoutElements();

    }

    @Override
    public void setSelectedTab(int index) {
        setSelectedTab(elmnt.findElementByName("#tab-button-panel").getElements().get(index).getId());
    }

    @Override
    public void setSelectedTab(String tabId) {
        switchTab(tabId);
    }

    @Override
    public String getSelectedTab() {
        return activeTab;
    }

    @Override
    public int getSelectedTabIndex() {
        int tabIndex = 0;
        if (activeTab
                == null || activeTab.length()
                == 0) {
            return -1;
        } else {
            Element tabContentPanel = elmnt.findElementByName("#tab-content-panel");
            List<Element> elements = tabContentPanel.getElements();
            for (final Element e : elements) {
                if (e.getId().equals(activeTab)) {
                    break;
                }
                tabIndex++;
            }
        }
        return tabIndex;
    }

    /**
     * Method used to switch between tabs. This method is called when the tab caption button is clicked.
     * @param tabId The id of the tab this button is linked to.
     */
    public void switchTab(String tabId) {
        if (!tabId.equals(activeTab)) {
            if (activeTab != null) {
                elmnt.findElementByName("#tab-button-panel").findElementByName(activeTab + "-button").setStyle("tab-button");
            }
            elmnt.findElementByName("#tab-button-panel").findElementByName(tabId + "-button").setStyle("active-tab-button");
            if (activeTab != null) {
                elmnt.findElementByName("#tab-content-panel").findElementByName(activeTab).hideWithoutEffect();
            }
            elmnt.findElementByName("#tab-content-panel").findElementByName(tabId).showWithoutEffects();
            nifty.publishEvent(getId(), new TabSelectedEvent(this, tabId));
            activeTab = tabId;
            elmnt.layoutElements();
        }

    }

    @Override
    public void onEvent(final String topic, final ElementShowEvent data) {
        // we make sure that only the active tab is visible
        Element tabContentPanel = elmnt.findElementByName("#tab-content-panel");
        for (int i = 0; i < tabContentPanel.getElements().size(); i++) {
            tabContentPanel.getElements().get(i).hideWithoutEffect();
        }
        if (activeTab != null) {
            elmnt.findElementByName("#tab-content-panel").findElementByName(activeTab).showWithoutEffects();
        }
    }
}
