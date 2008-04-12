package de.lessvoid.nifty.render;

import java.util.Set;

/**
 * The RenderDevice.
 * @author void
 */
public interface RenderDevice {

  public int getWidth();
  public int getHeight();

  /**
   * Render Image.
   * @param image the image to render
   * @param x the x position on the screen
   * @param y the y position on the screen
   * @param width the width
   * @param height the height
   */
  void renderImage(RenderImage image, int x, int y, int width, int height);

  /**
   * Render a Part of an Image.
   * @param image the image to render
   * @param x the x position on the screen
   * @param y the y position on the screen
   * @param w the width
   * @param h the height
   * @param srcX x position in image to copy from
   * @param srcY y position in image to copy from
   * @param srcW width in image to copy from
   * @param srcH heightin image to coopy from
   */
  void renderImagePart(RenderImage image, int x, int y, int w, int h, int srcX, int srcY, int srcW, int srcH);
  public void setColor( float r, float g, float b, float a );
  public void renderQuad( int x, int y, int width, int height );

  /**
   * Create a RenderImage.
   * @param name the filename
   * @param filter use linear filter (true) or nearest filter (false)
   * @return a new RenderImage instance
   */
  RenderImage createImage(String name, boolean filter);


  public RenderFont createFont( String name );

  /**
   * Save the given Set of States on a Stack.
   * @param stateToSave set of SaveState elements to identify state to save
   */
  void saveState(Set < RenderState > stateToSave);

  /**
   * restore the last state from stack.
   */
  void restoreState();

  public void moveTo( float x, float y );
  public float getMoveToX();
  public float getMoveToY();
  
  public void clear();
  
  public void setFontColor( float r, float g, float b, float a );
  public void renderText( RenderFont font, String text, int x, int y );
  public void setRenderTextSize( float size );
  
  public void enableBlend();
  public void disableTexture();
  public void setImageScale(float f);

  public void setGlobalPosition(float x, float y);

  void disableClip();
  void enableClip(int x0, int y0, int x1, int y1);
}
