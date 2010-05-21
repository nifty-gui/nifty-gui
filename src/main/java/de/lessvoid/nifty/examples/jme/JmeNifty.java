package de.lessvoid.nifty.examples.jme;

import java.net.URL;

import org.lwjgl.input.Mouse;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.resource.ClasspathResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;

import de.lessvoid.nifty.jme.render.NiftyNode;


public class JmeNifty extends SimpleGame {
  private Quaternion rotQuat = new Quaternion();
  private float angle = 0;
  private Sphere sphere;
  private NiftyNode niftyNode;

  public static void main(final String[] args) {
    JmeNifty app = new JmeNifty();
    app.setConfigShowMode(ConfigShowMode.AlwaysShow);
    app.start();
  }
  
  protected void simpleUpdate() 
  {
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
    display.setTitle("Nifty render test for JME2");
    
    // Allow mouse to leave the game window (very useful on debugging :)
    Mouse.setGrabbed(false);        

    
    // Create a spinning monkey ball
    sphere = new Sphere("Sphere", 63, 50, 25);
    sphere.setLocalTranslation(new Vector3f(0,0,-40));
    sphere.setModelBound(new BoundingBox());
    sphere.updateModelBound();
    sphere.setCullHint(CullHint.Dynamic);

    try {
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, new ClasspathResourceLocator());
    } catch (Exception e) {
        e.printStackTrace();
    }

    URL u = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "/jmetest/data/images/Monkey.png");
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
    rootNode.attachChild(sphere);

    
    // Create the NiftyGUI
    niftyNode = new NiftyNode("GUI:NIFTYNODE", "console/console.xml", "start");
    rootNode.attachChild(niftyNode);
    
    rootNode.setCullHint(CullHint.Never);
  }
}