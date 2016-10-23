package de.lessvoid.nifty.examples.libgdx.defaultcontrols;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.lessvoid.nifty.examples.defaultcontrols.ControlsDemo;
import de.lessvoid.nifty.examples.libgdx.LibgdxExampleApplication;
import de.lessvoid.nifty.examples.libgdx.resolution.GdxResolutionControl;
import de.lessvoid.nifty.examples.libgdx.resolution.GdxResolutionControl.Resolution;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class ControlsDemoMain {
  public static void main(String[] args) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.useGL30 = false;
    config.vSyncEnabled = true;
    config.width = 1024;
    config.height = 768;
    config.title = "Nifty LibGDX Desktop Examples: Default Controls";
    config.resizable = true;
    final int atlasWidth = 2048;
    final int atlasHeight = 2048;
    new LwjglApplication(new LibgdxExampleApplication(new ControlsDemo<Resolution>(new GdxResolutionControl()),
            atlasWidth, atlasHeight), config);
  }
}
