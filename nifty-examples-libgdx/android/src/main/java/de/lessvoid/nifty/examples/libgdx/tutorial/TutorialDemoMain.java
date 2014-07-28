package de.lessvoid.nifty.examples.libgdx.tutorial;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import de.lessvoid.nifty.examples.libgdx.LibgdxExampleApplication;
import de.lessvoid.nifty.examples.tutorial.TutorialExampleMain;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class TutorialDemoMain extends AndroidApplication {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
    final int atlasWidth = 1024;
    final int atlasHeight = 1024;
    initialize(new LibgdxExampleApplication(new TutorialExampleMain(), atlasWidth, atlasHeight), config);
  }
}
