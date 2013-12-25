package de.lessvoid.nifty.renderer.lwjgl.render;

import de.lessvoid.nifty.spi.render.MouseCursor;
import org.lwjgl.input.Cursor;

public class LwjglMouseCursor implements MouseCursor {
  private final Cursor cursor;

  public LwjglMouseCursor(final Cursor cursor) {
    this.cursor = cursor;
  }

  @Override
  public void dispose() {
    cursor.destroy();
  }

  public Cursor getCursor() {
    return cursor;
  }
}
