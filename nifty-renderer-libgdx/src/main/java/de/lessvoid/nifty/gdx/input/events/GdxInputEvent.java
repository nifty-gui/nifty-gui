package de.lessvoid.nifty.gdx.input.events;

import com.badlogic.gdx.InputProcessor;

import de.lessvoid.nifty.NiftyInputConsumer;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface GdxInputEvent {
  public boolean sendToNifty(NiftyInputConsumer consumer);

  public void sendToGdx(final InputProcessor processor);

  public void freeEvent();
}
