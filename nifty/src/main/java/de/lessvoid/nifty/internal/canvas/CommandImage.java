package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyImage;
import de.lessvoid.nifty.internal.InternalNiftyImage;
import de.lessvoid.nifty.internal.accessor.NiftyImageAccessor;
import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandImage implements Command {
  private final int x;
  private final int y;
  private final NiftyImage image;

  public CommandImage(final int x, final int y, final NiftyImage image) {
    this.x = x;
    this.y = y;
    this.image = image;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    InternalNiftyImage internalImage = NiftyImageAccessor.getDefault().getInternalNiftyImage(image);
    Mat4 local = Mat4.mul(context.getTransform(), Mat4.createTranslate(x, y, 0.0f));
    batchManager.addTextureQuad(internalImage.getTexture(), local, NiftyColor.WHITE());
  }
}
