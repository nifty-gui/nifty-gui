package de.lessvoid.nifty.examples.defaultcontrolsxml;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

public class DefaultControlsXmlMain {
  public static void main(final String[] args) {
    if (!LwjglInitHelper.initSubSystems("Nifty Button Example")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(
        new LwjglRenderDevice(),
        new OpenALSoundDevice(),
        LwjglInitHelper.getInputSystem(),
        new TimeProvider());
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/01-button.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/02-chat.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/03-checkbox.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/04-console.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/05-dropdown.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/06-imageselect.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/07-label.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/08-dropdown.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/09-radiobutton.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/10-scrollbar.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/11-scrollpanel.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/12-slider.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/13-textfield.xml", "start");
//    nifty.fromXml("src/main/resources/defaultcontrolsxml/14-window.xml", "start");

    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }
}
