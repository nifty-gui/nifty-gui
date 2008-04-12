package de.lessvoid.nifty.render.opengl;

import de.lessvoid.font.Font;
import de.lessvoid.nifty.render.RenderFont;

public class RenderFontLwjgl implements RenderFont {
  private Font font;
  
  public RenderFontLwjgl( String name ) {
    font= new Font();
    font.init( name );
  }
  
  public void render( String text, int x, int y ) {
    font.drawString( x, y, text );
  }

  public void renderWithSize( String text, int x, int y, float size ) {
    font.drawStringWithSize( x, y, text, size );
  }

  public void renderWithSizeAndColor( String text, int x, int y, float size, float r, float g, float b, float a ) {
    font.renderWithSizeAndColor( x, y, text, size, r, g, b, a );
  }

  public int getHeight() {
    return font.getHeight();
  }

  public int getWidth( String text ) {
    return font.getStringWidth( text );
  }

  public int getFittingOffset(final String text, final int width) {
    return font.getLengthFittingPixelSize(text, width, 1.0f);
  }

  public int getFittingOffsetBackward(final String text, final int width) {
    return font.getLengthFittingPixelSizeBackwards(text, width, 1.0f);
  }

  public int getIndexFromPixel(final String text, final int pixel, final float size) {
    return font.getIndexFromPixel(text, pixel, size);
  }

  /**
   * set selection.
   * @param selectionStart selection start
   * @param selectionEnd selection end
   */
  public void setSelection(final int selectionStart, final int selectionEnd) {
    font.setSelectionStart(selectionStart);
    font.setSelectionEnd(selectionEnd);
  }

}
