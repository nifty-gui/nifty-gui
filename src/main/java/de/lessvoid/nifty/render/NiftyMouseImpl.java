package de.lessvoid.nifty.render;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;

public class NiftyMouseImpl implements NiftyMouse {
  private Logger log = Logger.getLogger(NiftyMouseImpl.class.getName());
  private RenderDevice renderDevice;
  private InputSystem inputSystem;
  private Map < String, MouseCursor > registeredMouseCursors = new Hashtable < String, MouseCursor >();
  private String currentId;

  public NiftyMouseImpl(final RenderDevice renderDevice, final InputSystem inputSystem) {
    this.renderDevice = renderDevice;
    this.inputSystem = inputSystem;
  }

  @Override
  public void registerMouseCursor(final String id, final String filename, final int hotspotX, final int hotspotY) throws IOException {
    MouseCursor mouseCursor = renderDevice.createMouseCursor(filename, hotspotX, hotspotY);
    if (mouseCursor == null) {
      log.warning("Your RenderDevice does not support the createMouseCursor() method. Mouse cursors can't be changed.");
      return;
    }
    registeredMouseCursors.put(id, mouseCursor);
  }

  @Override
  public String getCurrentId() {
    return currentId;
  }

  @Override
  public void unregisterAll() {
    for (MouseCursor cursor : registeredMouseCursors.values()) {
      cursor.dispose();
    }
    registeredMouseCursors.clear();
  }

  @Override
  public void resetMouseCursor() {
    currentId = null;
    renderDevice.disableMouseCursor();
  }

  @Override
  public void enableMouseCursor(final String id) {
    if (id == null) {
      resetMouseCursor();
      return;
    }
    if (id.equals(currentId)) {
      return;
    }
    renderDevice.enableMouseCursor(registeredMouseCursors.get(id));
    currentId = id;
  }

  @Override
  public void setMousePosition(final int x, final int y) {
    inputSystem.setMousePosition(x, y);
  }
}
