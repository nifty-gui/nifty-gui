package de.lessvoid.nifty.examples.tutorial;

import java.io.File;
import java.net.URL;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ClasspathResourceLocator;
import com.jme.util.resource.MultiFormatResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.jme.input.JmeInputSystem;
import de.lessvoid.nifty.jme.render.JmeRenderDevice;
import de.lessvoid.nifty.lwjglslick.sound.SlickSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

public class TutorialMainJME2 extends SimpleGame {
  private Quaternion rotQuat = new Quaternion();
  private float angle = 0;
  private Sphere sphere;
  private Node niftyNode;
  private Nifty nifty;

  public static void main(final String[] args) {
    TutorialMainJME2 app = new TutorialMainJME2();
    //app.setConfigShowMode(ConfigShowMode.AlwaysShow);
    app.start();
  }

  protected void simpleUpdate() {
    nifty.render(true);

    if (tpf < 1) {
      angle = angle + (tpf * 1);
      if (angle > 360) {
        angle = 0;
      }
    }
    rotQuat.fromAngleAxis(angle, new Vector3f(1, 1, 0));
    sphere.setLocalRotation(rotQuat);
  }

  protected void simpleInitGame() {
    display.setTitle("jME - Sphere");

    sphere = new Sphere("Sphere", 63, 50, 25);
    sphere.setLocalTranslation(new Vector3f(0,0,-40));
    sphere.setModelBound(new BoundingBox());
    sphere.updateModelBound();
    sphere.setCullHint(CullHint.Dynamic);

    try {
      MultiFormatResourceLocator loc2 = new MultiFormatResourceLocator(new File("c:/").toURI(), ".jpg", ".png", ".tga");
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, loc2);
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, new ClasspathResourceLocator());
    } catch (Exception e) {
        e.printStackTrace();
    }

    URL u = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "/model/grass.gif");
    System.err.println("FOUND URL: "+u);

    TextureState ts = display.getRenderer().createTextureState();
    ts.setEnabled(true);
    ts.setTexture(TextureManager.loadTexture(u, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
    sphere.setRenderState(ts);

    BlendState blend = display.getRenderer().createBlendState();
    blend.setBlendEnabled(true);
    blend.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
    blend.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
    blend.setTestEnabled(true);
    blend.setTestFunction(BlendState.TestFunction.GreaterThan);
    blend.setEnabled(true);
    sphere.setRenderState(blend);

    niftyNode = new Node("hudNode");
    niftyNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
    niftyNode.setLocalTranslation(DisplaySystem.getDisplaySystem().getWidth()/2.0f, DisplaySystem.getDisplaySystem().getHeight()/2.0f, 0.0f);
    niftyNode.setLightCombineMode(Spatial.LightCombineMode.Off);
    niftyNode.updateRenderState();

    rootNode.setCullHint(CullHint.Never);
    rootNode.attachChild(sphere);
    rootNode.attachChild(niftyNode);

    nifty = new Nifty(
        new JmeRenderDevice(),
        new SlickSoundDevice(),
        new JmeInputSystem(),
        new TimeProvider());
    nifty.fromXml("tutorial/tutorial.xml", "start");
  }

}
