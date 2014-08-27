package de.lessvoid.nifty.examples.multiclick;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Properties;

/**
 * @author cris
 */
public class MultiClickExample implements ScreenController, NiftyExample {
   private Nifty nifty;

   public void bind(@Nonnull final Nifty nifty, @Nonnull final Screen screen) {
     this.nifty = nifty;
     nifty.setGlobalProperties(new Properties());
     nifty.getGlobalProperties().setProperty("MULTI_CLICK_TIME", "200");
     Element findElementById = screen.findElementById("GPanel3");
     new LabelBuilder("GLabel2") {{
       text("This is generated using builder");
       wrap(true);
       height(SizeValue.wildcard());
       width(SizeValue.wildcard());
       interactOnMultiClick("changeTime()");
     }}.build(nifty, screen, findElementById);
   }

  @Override
  public void onStartScreen() {
  }

  @Override
  public void onEndScreen() {
  }

  public void log(int x,int y,int count) {
    System.out.println("clicked: "+count);
    Label findNiftyControl = nifty.getCurrentScreen().findNiftyControl("GLabel0", Label.class);
    findNiftyControl.setText("Click count: "+count);
  }

  @NiftyEventSubscriber(id="GLabel0")
  public void logSingle(String id,NiftyMousePrimaryClickedEvent event) {
    System.out.println("Click one time");
  }

  public void changeTime(){
    Properties globalProperties = this.nifty.getGlobalProperties();
    String property = globalProperties.getProperty("MULTI_CLICK_TIME");
    int time = Integer.parseInt(property);
    time+=100;
    if(time>1500) time=200;
    globalProperties.setProperty("MULTI_CLICK_TIME", ""+time);
    System.out.println("New time : "+time);
  }

  @Override
  public String getStartScreen() {
    return "start";
  }

  @Nullable
  @Override
  public String getMainXML() {
    return "multiclick/mainscreen.xml";
  }

  @Nonnull
  @Override
  public String getTitle() {
    return "Multiclick Example";
  }

  @Override
  public void prepareStart(Nifty nifty) {
    // nothing to do
  }
}
