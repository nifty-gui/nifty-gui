package example;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.html.NiftyHtmlGenerator;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TimeProvider;

public class HtmlMain {

  public static void main(final String[] args) throws Exception {
    if (!LwjglInitHelper.initSubSystems("Nifty HTML Test")) {
      System.exit(0);
    }

    // create nifty
    Nifty nifty = new Nifty(new LwjglRenderDevice(), new NullSoundDevice(), LwjglInitHelper.getInputSystem(), new TimeProvider());
    nifty.fromXml("src/test/resources/test.xml", "start");

    // get the current screen from nifty
    Screen screen = nifty.getCurrentScreen();

    // create the NiftyHtmlGenerator that needs the parent element (a panel) where the generated nifty elements will be attached as child elements
    NiftyHtmlGenerator generator = new NiftyHtmlGenerator(nifty);
    generator.generate(readHTMLFile("src/test/resources/html/test-20.html"), screen, screen.findElementByName("parent"));

    // just debug output the nifty screen (in case we want to check some things)
    System.out.println(nifty.getCurrentScreen().debugOutput());

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
}