package de.lessvoid.nifty.renderer.jogl.render;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jogamp.newt.Window;

import de.lessvoid.nifty.render.batch.spi.MouseCursorFactory;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class JoglMouseCursorFactory implements MouseCursorFactory {
	
	private final Window newtWindow;
	
	public JoglMouseCursorFactory(@Nonnull final Window newtWindow) {
		this.newtWindow = newtWindow;
	}
	
  @Nullable
  @Override
  public MouseCursor create(
          @Nonnull String filename,
          int hotspotX,
          int hotspotY,
          @Nonnull NiftyResourceLoader resourceLoader) throws IOException {
    return new JoglMouseCursor(filename, hotspotX, hotspotY, newtWindow, resourceLoader);
  }
}
