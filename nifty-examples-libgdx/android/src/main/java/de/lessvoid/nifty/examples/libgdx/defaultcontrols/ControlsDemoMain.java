package de.lessvoid.nifty.examples.libgdx.defaultcontrols;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.lessvoid.nifty.examples.libgdx.LibgdxExampleApplication;
import de.lessvoid.nifty.examples.defaultcontrols.ControlsDemo;
import de.lessvoid.nifty.examples.libgdx.resolution.GdxResolutionControl;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class ControlsDemoMain extends AndroidApplication {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
    config.useGL20 = false;
    final int atlasWidth = 2048;
    final int atlasHeight = 2048;
    initialize(new LibgdxExampleApplication(new ControlsDemo(new GdxResolutionControl()), atlasWidth, atlasHeight), config);
  }
}
