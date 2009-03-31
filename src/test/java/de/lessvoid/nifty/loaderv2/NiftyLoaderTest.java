package de.lessvoid.nifty.loaderv2;

import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.TimeProvider;
import de.lessvoid.nifty.tools.resourceloader.ResourceLoader;

public class NiftyLoaderTest extends TestCase {
  private static String testXml =
    "<nifty>"
    +  "<registerSound id=\"gong\" filename=\"sound/19546__tobi123__Gong_mf2.wav\" />"
    +  "<style id=\"test\" base=\"else\">"
    +    "<attributes align=\"left\"/>"
    +    "<interact onMouseMove=\"doSomething2()\" />"
    +    "<effect stuff=\"a\">"
    +     "<onStartScreen name=\"start\" />"
    +     "<onHover name=\"test\">"
    +      "<hover name=\"anc\" />"
    +     "</onHover>"
    +     "<onStartScreen name=\"start-2\" />"
    +    "</effect>"
    +  "</style>"
    +  "<style id=\"test2\" base=\"else2\">"
    +    "<attributes align=\"right\"/>"
    +  "</style>"
    +  "<screen id=\"screen\" controller=\"de.lessvoid.nifty.loaderv2.NiftyLoaderTest#DummyScreenController\">"
    +   "<layer id=\"layer1\">"
    +    "<panel id=\"panel-1\">"
    +     "<image id=\"image-3\">"
    +      "<label id=\"label-1\">"
    +      "</label>"
    +     "</image>"
    +    "</panel>"
    +    "<panel id=\"panel-2\">"
    +    "</panel>"
    +    "<image id=\"image-1\">"
    +    "</image>"
    +    "<image id=\"image-2\">"
    +    "</image>"
    +   "</layer>"
    +  "</screen>"
    + "</nifty>";

  public class DummyScreenController implements ScreenController {
    public void bind(final Nifty nifty, final Screen screen) {
    }
    public void onStartScreen() {
    }
    public void onEndScreen() {
    }
  }

  public void setUp() {
    LoggerShortFormat.intialize();
  }

  public void testLoader() throws Exception {
    NiftyLoader niftyLoader = new NiftyLoader();
    niftyLoader.registerSchema("nifty.nxs", ResourceLoader.getResourceAsStream("nifty.nxs"));
    niftyLoader.registerSchema("nifty-styles.nxs", ResourceLoader.getResourceAsStream("nifty-styles.nxs"));

    Nifty nifty = createNiceMock(Nifty.class);
    replay(nifty);

    NiftyType niftyType = niftyLoader.loadNiftyXml(
        "nifty.nxs",
        new ByteArrayInputStream(testXml.getBytes("ISO-8859-1")),
        nifty,
        new TimeProvider());
    System.out.println(niftyType.output());
  }
}
