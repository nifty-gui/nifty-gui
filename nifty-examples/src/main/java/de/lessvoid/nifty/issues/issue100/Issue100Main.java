/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.issues.issue100;

import de.lessvoid.nifty.issues.issue225.*;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author cris
 */
public class Issue100Main implements ScreenController {
    
    public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("New ImageMode")) {
      System.exit(0);
    }
    LwjglRenderDevice lwjglRenderDevice = new LwjglRenderDevice();
  
    // create nifty
    Nifty nifty = new Nifty(
        lwjglRenderDevice,
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");
    nifty.fromXml("issues/issue100/example.xml", "GScreen0");
    nifty.gotoScreen("GScreen0");
    
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
    private Nifty nifty;

    public void bind(Nifty nifty, Screen screen) {
        DropDown dropdown = screen.findNiftyControl("GDropDown0", DropDown.class);
        this.nifty = nifty;
        dropdown.addItem(Locale.ITALIAN);
        dropdown.addItem(Locale.GERMAN);
        dropdown.addItem(Locale.ENGLISH);
        dropdown.addItem(Locale.getDefault());
        dropdown.selectItemByIndex(3);
        
    }

    public void onStartScreen() {
       
    }

    public void onEndScreen() {
        
    }
    @NiftyEventSubscriber(id="GDropDown0")
    public void changeLocale(String id, DropDownSelectionChangedEvent e){
        nifty.setLocale((Locale)e.getSelection());
    }
}
