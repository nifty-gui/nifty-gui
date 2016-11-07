package de.lessvoid.nifty.examples.libgdx.all;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.lessvoid.nifty.examples.all.AllExamples;
import de.lessvoid.nifty.examples.libgdx.LibgdxExampleApplication;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class AllDemoMain {
  public static void main(String[] args) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.useGL30 = false;
    config.vSyncEnabled = true;
    config.width = 1024;
    config.height = 768;
    config.title = "Nifty LibGDX Desktop Examples";
    config.resizable = true;
    final int atlasWidth = 2048;
    final int atlasHeight = 2048;
    new LwjglApplication(new LibgdxExampleApplication(new AllExamples(), atlasWidth, atlasHeight), config);
  }
}
