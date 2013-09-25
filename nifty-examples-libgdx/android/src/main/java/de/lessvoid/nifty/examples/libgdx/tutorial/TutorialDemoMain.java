package de.lessvoid.nifty.examples.libgdx.tutorial;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.lessvoid.nifty.examples.libgdx.LibgdxExampleApplication;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class TutorialDemoMain extends AndroidApplication {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
    config.useGL20 = false;
    final int atlasWidth = 2048;
    final int atlasHeight = 2048;
    initialize(new LibgdxExampleApplication(new TutorialExample(), atlasWidth, atlasHeight), config);
  }
}
