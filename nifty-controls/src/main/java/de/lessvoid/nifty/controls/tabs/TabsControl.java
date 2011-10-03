/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder.ChildLayoutType;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Tabs;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author ractoc
 */
public class TabsControl extends AbstractController implements Tabs {

    private Nifty nifty;
    private static String activeTab;
    private Element elmnt;

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        super.bind(element);
        this.nifty = nifty;
        this.elmnt = element;
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
        System.out.println("looking for button " + tabId + "-button");
        if (tabButtonPanel.findElementByName(tabId + "-button") == null) {
            System.out.println("adding button " + tabId + "-button");
            new ButtonBuilder(tabId + "-button") {{
                style("nifty-button");
                childLayout(ChildLayoutType.Horizontal);
                interactOnClick("switchTab(" + tabId + ")");
                width(percentage(25));
                height(percentage(100));
                label(buttonCaption);
            }}.build(nifty, nifty.getCurrentScreen(), tabButtonPanel);
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
            setActiveTab(tab.getId());
            createTabButton(tab.getId(), tab.getControl(TabControl.class).getCaption());
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
                    setActiveTab(elmnt.findElementByName("#tab-button-panel").getElements().get(1).getId());
                } else {
                    setActiveTab(elmnt.findElementByName("#tab-button-panel").getElements().get(0).getId());
                }
            }
        }
        elmnt.findElementByName("#tab-button-panel").findElementByName(tabId + "-button").markForRemoval();
        elmnt.findElementByName("#tab-content-panel").findElementByName(tabId).markForRemoval();
        elmnt.layoutElements();
        
    }

    @Override
    public void setActiveTab(int index) {
        setActiveTab(elmnt.findElementByName("#tab-button-panel").getElements().get(index).getId());
    }

    @Override
    public void setActiveTab(String tabId) {
        switchTab(tabId);
    }

    /**
     * Method used to switch between tabs. This method is called when the tab caption button is clicked.
     * @param tabId The id of the tab this button is linked to.
     */
    public void switchTab(String tabId) {
        if (!tabId.equals(activeTab)) {
            //TODO: add these two styles to the grey style.
//            elmnt.findElementByName("#tab-button-panel").findElementByName(activeTab).setStyle("tab-button");
//            elmnt.findElementByName("#tab-button-panel").findElementByName(tabId).setStyle("active-tab-button");
System.out.println("setting active tab to " + tabId);
            if (activeTab != null) {
                elmnt.findElementByName("#tab-content-panel").findElementByName(activeTab).hideWithoutEffect();
            }
            elmnt.findElementByName("#tab-content-panel").findElementByName(tabId).showWithoutEffects();

            activeTab = tabId;
            elmnt.layoutElements();
        }

    }
}
