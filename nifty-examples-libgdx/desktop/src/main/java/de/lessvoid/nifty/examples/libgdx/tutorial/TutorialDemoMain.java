package de.lessvoid.nifty.examples.libgdx.tutorial;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.lessvoid.nifty.examples.libgdx.LibgdxExampleApplication;
import de.lessvoid.nifty.examples.tutorial.TutorialExample;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class TutorialDemoMain {
  public static void main(String[] args) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.useGL30 = false;
    config.vSyncEnabled = true;
    config.width = 1024;
    config.height = 768;
    config.title = "Nifty LibGDX Desktop Examples: Nifty 1.2 Tutorial";
    config.resizable = true;
    final int atlasWidth = 2048;
    final int atlasHeight = 2048;
    new LwjglApplication(new LibgdxExampleApplication(new TutorialExample(), atlasWidth, atlasHeight), config);
  }
}
