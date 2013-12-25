package example;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.html.NiftyHtmlGenerator;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import org.easymock.EasyMock;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HtmlMain implements ScreenController {
  private Nifty nifty;
  private Screen screen;
  private NiftyHtmlGenerator generator;

  public static void main(final String[] args) throws Exception {
    if (!LwjglInitHelper.initSubSystems("Nifty HTML Test")) {
      System.exit(0);
    }

    // create nifty
    SoundDevice soundDeviceMock = EasyMock.createNiceMock(SoundDevice.class);
    Nifty nifty = new Nifty(new LwjglRenderDevice(), soundDeviceMock, LwjglInitHelper.getInputSystem(),
        new AccurateTimeProvider());
    nifty.fromXml("src/test/resources/test.xml", "start");

    // that's the standard render loop for LWJGL as used in every standard nifty example
    LwjglInitHelper.renderLoop(nifty, null);
    LwjglInitHelper.destroy();
  }

  private static String readHTMLFile(final String filename) throws IOException {
    InputStreamReader reader = new InputStreamReader(new FileInputStream(filename), "ISO-8859-1");
    StringBuffer result = new StringBuffer();
    char[] buffer = new char[1024];
    int read = -1;
    while ((read = reader.read(buffer)) > 0) {
      result.append(buffer, 0, read);
    }
    return result.toString();
  }

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    generator = new NiftyHtmlGenerator(nifty);
    generator.setDefaultFont("aurulent-sans-16.fnt");
    generator.setDefaultBoldFont("aurulent-sans-16-bold.fnt");
  }

  @Override
  public void onStartScreen() {
    List<String> items = new ArrayList<String>();
    for (int i = 1; i < 51; i++) {
      items.add("src/test/resources/html/test-" + String.format("%02d", i) + ".html");
    }

    DropDown<String> htmlSelectDropDown = screen.findNiftyControl("html-select", DropDown.class);
    htmlSelectDropDown.addAllItems(items);
  }

  @Override
  public void onEndScreen() {
  }

  @NiftyEventSubscriber(id = "html-select")
  public void onHtmlSelectChanged(final String id, final DropDownSelectionChangedEvent<String> event) {
    try {
      generator.generate(readHTMLFile(event.getSelection()), screen, screen.findElementById("parent"));

      // for debugging purpose we could output the screen as a text structure
      //      System.out.println(screen.debugOutput());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}