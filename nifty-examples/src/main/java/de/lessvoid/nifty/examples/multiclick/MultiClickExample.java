/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.examples.multiclick;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import de.lessvoid.nifty.tools.SizeValue;
import java.util.Properties;

/**
 *
 * @author cris
 */
public class MultiClickExample implements ScreenController{
    
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
    nifty.fromXml("multiclick/mainscreen.xml", "GScreen0");
    nifty.gotoScreen("GScreen0");
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
    private Nifty nifty;

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        nifty.setGlobalProperties(new Properties());
        nifty.getGlobalProperties().setProperty("MULTI_CLICK_TIME", "200");
        Element findElementById = screen.findElementById("GPanel3");
        Element builder = new LabelBuilder("GLabel2"){{
            text("This is generated using builder");
            wrap(true);
            height(SizeValue.wildcard());
            width(SizeValue.wildcard());
            interactOnMultiClick("changeTime()");
        }}.build(nifty, screen, findElementById);
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
       
    }
    
    public void log(int x,int y,int count){
       System.out.println("clicked: "+count);
        Label findNiftyControl = nifty.getCurrentScreen().findNiftyControl("GLabel0", Label.class);
        findNiftyControl.setText("Click count: "+count);
    }
    @NiftyEventSubscriber(id="GLabel0")
    public void logSingle(String id,NiftyMousePrimaryClickedEvent event){
        System.out.println("Click one time");
    }
    
    public void changeTime(){
        Properties globalProperties = this.nifty.getGlobalProperties();
        String property = globalProperties.getProperty("MULTI_CLICK_TIME");
        int time = Integer.parseInt(property);
        time+=100;
        if(time>1500)
            time=200;
       globalProperties.setProperty("MULTI_CLICK_TIME", ""+time);
       System.out.println("New time : "+time);
    }
}
