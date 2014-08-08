package de.lessvoid.nifty.gdx.input.events;

import com.badlogic.gdx.InputProcessor;
import de.lessvoid.nifty.NiftyInputConsumer;

import javax.annotation.Nonnull;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface GdxInputEvent {
  public boolean sendToNifty(@Nonnull NiftyInputConsumer consumer);
  public boolean sendToGdx(@Nonnull InputProcessor processor);
  public void freeEvent();
}
