/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Tab;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

/**
 *
 * @author ractoc
 */
public class TabControl extends AbstractController implements Tab {

    private String caption;

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        super.bind(element);
        if (caption == null || caption.length() == 0) {
            if (controlDefinitionAttributes.get("caption") == null || controlDefinitionAttributes.get("caption").length() == 0) {
                setCaption(getId());
            } else {
                setCaption(controlDefinitionAttributes.get("caption"));
            }
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
    public void setCaption(String caption) {
        if (caption == null || caption.length() == 0) {
            throw new NullPointerException("Missing attribute: caption for tab " + this.getId());
        }
        this.caption = caption;
    }

    @Override
    public String getCaption() {
        return caption;
    }
}
