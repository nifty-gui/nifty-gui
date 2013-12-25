package de.lessvoid.nifty.render;

import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class NiftyMouseImpl implements NiftyMouse {
  private static final Logger log = Logger.getLogger(NiftyMouseImpl.class.getName());
  private final RenderDevice renderDevice;
  private final InputSystem inputSystem;
  @Nonnull
  private final Map<String, MouseCursor> registeredMouseCursors = new HashMap<String, MouseCursor>();
  @Nullable
  private String currentId;
  private int mouseX;
  private int mouseY;
  @Nonnull
  private final TimeProvider timeProvider;
  private long lastMouseMoveEventTime;

  public NiftyMouseImpl(
      final RenderDevice renderDevice,
      final InputSystem inputSystem,
      @Nonnull final TimeProvider timeProvider) {
    this.renderDevice = renderDevice;
    this.inputSystem = inputSystem;
    this.timeProvider = timeProvider;
    lastMouseMoveEventTime = timeProvider.getMsTime();
  }

  @Override
  public void registerMouseCursor(
      final String id,
      @Nonnull final String filename,
      final int hotspotX,
      final int hotspotY) throws IOException {
    MouseCursor mouseCursor = renderDevice.createMouseCursor(filename, hotspotX, hotspotY);
    if (mouseCursor == null) {
      log.warning("Your RenderDevice does not support the createMouseCursor() method. Mouse cursors can't be changed.");
      return;
    }
    registeredMouseCursors.put(id, mouseCursor);
  }

  @Nullable
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
  public void enableMouseCursor(@Nullable final String id) {
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
