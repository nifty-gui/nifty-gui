package de.lessvoid.nifty.examples.absoluteClipping;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class Clipping extends DefaultScreenController{
    private static int width;
    private static int height;

  public Clipping() {
  }

  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Clip the area")) {
      System.exit(0);
    }
    LwjglRenderDevice lwjglRenderDevice = new LwjglRenderDevice();
    height = lwjglRenderDevice.getHeight();
     width = lwjglRenderDevice.getWidth();
    // create nifty
    Nifty nifty = new Nifty(
        lwjglRenderDevice,
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.loadStyleFile("nifty-default-styles.xml");
    nifty.loadControlFile("nifty-default-controls.xml");
    nifty.fromXml("Clipping/clip.xml", "GScreen0");
    nifty.gotoScreen("GScreen0");
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }

  

  
    @NiftyEventSubscriber(id="decrease")
    public void redPanelVisible(final String id, final ButtonClickedEvent ev) {
      update(false);
    }
    @NiftyEventSubscriber(id="increase")
    public void greenPanelVisible(final String id, final ButtonClickedEvent ev) {
       update(true);
    }
    int amount = 0;
    private void update(boolean up) {
      amount += up ? 10 : -10; 
      this.nifty.setAbsoluteClip(-amount/2, -amount/2, width+amount/2,height+amount/2);
    }
  
}
