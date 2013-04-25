package de.lessvoid.nifty.internal.canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyRenderTarget;

public class InternalNiftyCanvas {
  private static final Logger log = Logger.getLogger(InternalNiftyCanvas.class.getName());
  private final List<Command> commands = new ArrayList<Command>();

  private int width;
  private int height;
  private Mat4 matrix;

  private final Context context = new Context();
  private NiftyRenderTarget renderTarget;

  public InternalNiftyCanvas() {
  }

  public void setFillColor(final NiftyColor color) {
    commands.add(new CommandFillColor(color));
  }

  public void setStrokeColor(final NiftyColor color) {
    commands.add(new CommandStrokeColor(color));
  }

  public void line(final double x0, final double y0, final double x1, final double y1) {
    commands.add(new CommandLine(x0, y0, x1, y1));
  }

  public void rect(final double x0, final double y0, final double x1, final double y1) {
    commands.add(new CommandRect(x0, y0, x1, y1));
  }

  public void filledRect(final double x0, final double y0, final double x1, final double y1) {
    commands.add(new CommandFilledRect(x0, y0, x1, y1));
  }

  // Other

  public void setSize(final int width, final int height) {
    this.width = width;
    this.height = height;
  }

  public void setMatrix(final Mat4 mat) {
    //renderTarget.setMatrix(mat);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void reset() {
    commands.clear();
  }

  public void updateContent(final NiftyRenderDevice renderDevice) {
    if (commands.isEmpty()) {
      return;
    }

    if (renderTarget == null) {
      renderTarget = renderDevice.createRenderTargets(width, height, 1, 1);
    }

    log.fine("Updating Canvas Content (" + commands.size() + ")");
    for (int i=0; i<commands.size(); i++) {
      Command command = commands.get(i);
      command.execute(renderTarget, context);
    }
    reset();
  }

  public void render(final NiftyRenderDevice renderDevice, final int width, final int height, final Mat4 mat) {
    if (renderTarget == null) {
      return;
    }
    renderDevice.render(renderTarget, 0, 0, width, height, mat);
  }
}
