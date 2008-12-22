package de.lessvoid.console;

import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import de.lessvoid.font.Font;
import de.lessvoid.nifty.render.spi.RenderDevice;

public class Console
{
  private Font font;
  private int x;
  private int y;
  private LinkedList<String> data = new LinkedList<String>();
  private boolean left;
  private int maxLines;
  
  private RenderDevice device;
  
  /**
   * create console
   * @param newRenderDevice 
   */
  public Console(RenderDevice device, int maxLines, boolean left)
  {
    this.device = device;

    font= new Font(device);
    font.init( "console.fnt" );
    
    this.left= left;
    this.maxLines= maxLines;
  }

  public void clear()
  {
    data.clear();
  }

  /**
   * Output something to the console.
   * @param newLine line to add
   */
  public void output(final String newLine) {
    String[] lines = newLine.split("\n");
    for (String line : lines) {
      addSingleLine(line);
    }
  }

  private void addSingleLine(final String newLine) {
    data.add(newLine);
    if (maxLinesReached()) {
      data.removeFirst();
    }
  }

  /**
   * returns true when the max lines limit has been reached.
   * @return more than max lines
   */
  boolean maxLinesReached() {
    return data.size() > maxLines;
  }

  /**
   * update console
   */
  public void update()
  {
    // first step: calc max width and height
    int maxWidth= 0;
    int maxHeight= 0;
    for( int i=0; i<data.size(); i++ )
    {
      maxHeight += font.getHeight();
      
      int width= font.getStringWidth( data.get( i ));
      if( width > maxWidth )
        maxWidth= width; 
    }
    
    // second step: do actual render
    beginRender( maxWidth, maxHeight );
    for( int i=0; i<data.size(); i++ )
    {
      outputString( data.get( i ));
    }
    endRender();
  }
  
  private void beginRender( int maxWidth, int maxHeight )
  {
    GL11.glPushAttrib( GL11.GL_CURRENT_BIT );
    
    GL11.glEnable( GL11.GL_BLEND );
    GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
    
    if( left )
    {
      x= 10;
      y= device.getHeight() - maxHeight - 10;
    }
    else
    {
      x= device.getWidth() - maxWidth - 10;
      y= device.getHeight() - maxHeight - 10;
    }
    GL11.glColor3f(1.0f, 1.0f, 1.0f);
  }
  
  private void endRender()
  {
    GL11.glPopAttrib();
  }

  private void outputString(final String text) {
    font.drawString(x, y, text);
    y += font.getHeight();
  }
}
