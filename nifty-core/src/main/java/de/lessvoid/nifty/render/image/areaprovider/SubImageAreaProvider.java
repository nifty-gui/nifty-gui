package de.lessvoid.nifty.render.image.areaprovider;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Logger;

public class SubImageAreaProvider implements AreaProvider {
  private static final Logger log = Logger.getLogger(SubImageAreaProvider.class.getName());

  private static final int SUBIMAGE_ARGS_COUNT = 4;

  private Box m_subImageArea;

  @Override
  public void setParameters(String parameters) {
    String[] args = getArguments(parameters);

    int x = Integer.valueOf(args[0]);
    int y = Integer.valueOf(args[1]);
    int width = Integer.valueOf(args[2]);
    int height = Integer.valueOf(args[3]);

    m_subImageArea = new Box(x, y, width, height);
  }

  @Nonnull
  private String[] getArguments(@Nullable String parameters) {
    String[] args = null;
    if (parameters != null) {
      args = parameters.split(",");
    }

    if ((args == null) || (args.length != SUBIMAGE_ARGS_COUNT)) {
      int argCount = (args == null) ? 0 : args.length;
      throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
          + "] : wrong parameter count (" + argCount + "). Expected [x,y,width,height], found ["
          + parameters + "].");
    }
    return args;
  }

  @Override
  public Box getSourceArea(@Nonnull RenderImage renderImage) {
    int imageWidth = renderImage.getWidth();
    int imageHeight = renderImage.getHeight();

    if (((m_subImageArea.getX() + m_subImageArea.getWidth()) > imageWidth)
        || ((m_subImageArea.getY() + m_subImageArea.getHeight()) > imageHeight)) {
      log.warning("subImage's area exceeds image's bounds.");
    }

    return m_subImageArea;
  }

  @Nonnull
  @Override
  public Size getNativeSize(@Nonnull NiftyImage image) {
    return new Size(image.getWidth(), image.getHeight());
  }
}
