package de.lessvoid.nifty.render;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;

public class NiftyMouseImpl implements NiftyMouse {
  private static Logger log = Logger.getLogger(NiftyMouseImpl.class.getName());
  private RenderDevice renderDevice;
  private InputSystem inputSystem;
  private Map < String, MouseCursor > registeredMouseCursors = new Hashtable < String, MouseCursor >();
  private String currentId;
  private int mouseX;
  private int mouseY;
  private TimeProvider timeProvider;
  private long lastMouseMoveEventTime;

  public NiftyMouseImpl(final RenderDevice renderDevice, final InputSystem inputSystem, final TimeProvider timeProvider) {
    this.renderDevice = renderDevice;
    this.inputSystem = inputSystem;
    this.timeProvider = timeProvider;
    lastMouseMoveEventTime = timeProvider.getMsTime();
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
    updateMousePosition(x, y);
  }

  @Override
  public int getX() {
    return mouseX;
  }

  @Override
  public int getY() {
    return mouseY;
  }

  @Override
  public long getNoMouseMovementTime() {
    long now = timeProvider.getMsTime();
    return now - lastMouseMoveEventTime;
  }

  public void updateMousePosition(final int x, final int y) {
    if (positionChanged(x, y)) {
      lastMouseMoveEventTime = timeProvider.getMsTime();
    }
    mouseX = x;
    mouseY = y;
  }

  private boolean positionChanged(final int x, final int y) {
    return x != mouseX || y != mouseY;
  }
}
