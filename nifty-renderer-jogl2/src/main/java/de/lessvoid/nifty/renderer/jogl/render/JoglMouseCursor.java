package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.spi.render.MouseCursor;

import java.awt.*;

public class JoglMouseCursor implements MouseCursor {

  private final Cursor cursor;

  public JoglMouseCursor(final Cursor cursor) {
    this.cursor = cursor;
  }

  @Override
  public void dispose() {
  }

  Cursor getCursor() {
    return cursor;
  }
}
