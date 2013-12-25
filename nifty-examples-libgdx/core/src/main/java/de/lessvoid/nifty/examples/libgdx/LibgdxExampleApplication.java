package de.lessvoid.nifty.examples.libgdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.batch.BatchRenderDevice;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.gdx.input.GdxInputSystem;
import de.lessvoid.nifty.gdx.input.GdxKeyRepeatSystem;
import de.lessvoid.nifty.gdx.render.batch.GdxBatchRenderBackend;
import de.lessvoid.nifty.gdx.sound.GdxSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

import java.util.logging.LogManager;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 *         <p/>
 *         {@inheritDoc}
 */
public class LibgdxExampleApplication implements ApplicationListener {
  private Nifty nifty;
  private final NiftyExample niftyExample;
  private final int atlasWidth;
  private final int atlasHeight;
  private AssetManager assetManager;
  private GdxKeyRepeatSystem keyRepeat;

  public LibgdxExampleApplication(final NiftyExample niftyExample, final int atlasWidth, final int atlasHeight) {
    this.niftyExample = niftyExample;
    this.atlasWidth = atlasWidth;
    this.atlasHeight = atlasHeight;
  }

  /**
   * Called when the {@link com.badlogic.gdx.Application} is first created.
   */
  @Override
  public void create() {
    configureLogging();

    assetManager = new AssetManager();

    BatchRenderDevice batchRenderDevice = new BatchRenderDevice(new GdxBatchRenderBackend(), atlasWidth, atlasHeight);
    batchRenderDevice.enableLogFPS();

    GdxInputSystem gdxInputSystem = new GdxInputSystem(Gdx.input);

    // Set up key repeat for arrows and backspace (useful when navigating Nifty textfields, for example)
    // LibGDX will not repeat these non-printable keys without using a GdxKeyRepeatSystem.
    keyRepeat = new GdxKeyRepeatSystem(gdxInputSystem);
    keyRepeat.setKeyRepeat(Input.Keys.LEFT, true);
    keyRepeat.setKeyRepeat(Input.Keys.RIGHT, true);
    keyRepeat.setKeyRepeat(Input.Keys.UP, true);
    keyRepeat.setKeyRepeat(Input.Keys.DOWN, true);
    keyRepeat.setKeyRepeat(Input.Keys.BACKSPACE, true);
    keyRepeat.setKeyRepeat(Input.Keys.FORWARD_DEL, true);

    // Initialize Nifty
    nifty = new Nifty(batchRenderDevice, new GdxSoundDevice(assetManager), gdxInputSystem, new AccurateTimeProvider());

    // Initialize the Nifty example.
    niftyExample.prepareStart(nifty);

    // Run the Nifty example.
    if (niftyExample.getMainXML() != null) {
      nifty.fromXml(niftyExample.getMainXML(), niftyExample.getStartScreen());
    } else {
      nifty.gotoScreen(niftyExample.getStartScreen());
    }
  }

  private void configureLogging() {
    try {
      LogManager.getLogManager().readConfiguration(ClassLoader.getSystemResourceAsStream("logging.properties"));
    } catch (Exception e) {
      Gdx.app.error("LibgdxExampleApplication", "Could not read logging configuration file: assets/logging" +
          ".properties", e);
    }
  }

  /**
   * Called when the {@link com.badlogic.gdx.Application} is resized. This can happen at any point during a non-paused
   * state but will never happen before a call to {@link #create()}.
   * <p/>
   * Note: This will be called once automatically during initialization, directly after create().
   *
   * @param width  the new width in pixels
   * @param height the new height in pixels
   */
  @Override
  public void resize(int width, int height) {
  }

  /**
   * Called when the {@link com.badlogic.gdx.Application} should render itself.
   * Since LibGDX doesn't have an explicit main loop, anything that must be called continuously, i.e., once per
   * frame, must go here. See https://code.google.com/p/libgdx/wiki/ApplicationLifeCycle for more information.
   */
  @Override
  public void render() {
    assetManager.update(); // Asynchronously load custom, non-Nifty resources if needed. Must be called once per frame.
    keyRepeat.update(); // Process any repeating input keys being held down
    nifty.update(); // Nifty's processing of non-rendering tasks (input, sound, etc). Must be called once per frame.
    nifty.render(true); // Nifty draws your GUI here. true = Nifty will clear the screen. Must be called once per frame.
  }

  /**
   * Called when the {@link com.badlogic.gdx.Application} is paused. An Application is paused before it is destroyed,
   * when a user pressed the Home button on Android or an incoming call happened. On the desktop this will only be
   * called immediately before {@link #dispose()} is called.
   */
  @Override
  public void pause() {
  }

  /**
   * Called when the {@link com.badlogic.gdx.Application} is resumed from a paused state. On Android this happens when
   * the activity gets focus again. On the desktop this method will never be called.
   */
  @Override
  public void resume() {
  }

  /**
   * Called when the {@link com.badlogic.gdx.Application} is destroyed. Preceded by a call to {@link #pause()}.
   */
  @Override
  public void dispose() {
    nifty.exit(); // Quit Nifty and dispose of all Nifty's resources.
    assetManager.dispose(); // Dispose all assets (resources) in the AssetManager and stop all asynchronous loading.
    Gdx.app.exit(); // Schedule an exit from the LibGDX application.
  }
}
