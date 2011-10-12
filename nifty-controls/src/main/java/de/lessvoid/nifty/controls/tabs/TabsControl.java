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
import de.lessvoid.xml.xpp3.Attributes;

/**
 *
 * @author ractoc
 */
public class TabsControl extends AbstractController implements Tabs, EventTopicSubscriber<ElementShowEvent> {

    private Nifty nifty;
    private static String activeTab;
    private Element elmnt;

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        super.bind(element);
        this.nifty = nifty;
        this.elmnt = element;
        nifty.subscribe(screen, getId(), ElementShowEvent.class, this);
    }

    @Override
    public void init(Properties prprts, Attributes atrbts) {
        System.out.println("init");
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
        System.out.println("looking for button " + tabId + "-button");
        if (tabButtonPanel.findElementByName(tabId + "-button") == null) {
            System.out.println("adding button " + tabId + "-button");
            new ButtonBuilder(tabId + "-button") {

                {
                    style("nifty-button");
                    childLayout(ChildLayoutType.Horizontal);
                    interactOnClick("switchTab(" + tabId + ")");
                    width(percentage(25));
                    height(percentage(100));
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
            System.out.println("adding tab " + tab.getId());
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
                tabIndex ++;
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
            //TODO: add these two styles to the style.
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
        System.out.println("onShow: " + data.getElement());

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
