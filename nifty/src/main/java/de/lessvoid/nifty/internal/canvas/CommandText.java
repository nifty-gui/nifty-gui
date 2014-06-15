package de.lessvoid.nifty.internal.canvas;

import org.jglfont.JGLFont;

import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyFont;
import de.lessvoid.nifty.internal.accessor.NiftyFontAccessor;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandText implements Command {
  private final NiftyFont font;
  private final int x;
  private final int y;
  private final String text;

  public CommandText(final NiftyFont font, final int x, final int y, final String text) {
    this.font = font;
    this.x = x;
    this.y = y;
    this.text = text;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    JGLFont jglFont = NiftyFontAccessor.getDefault().getJGLFont(font);
    jglFont.setCustomRenderState(batchManager);

    NiftyColor textColor = context.getTextColor();
    jglFont.renderText(
        x, y, text, context.getTextSize(), context.getTextSize(),
        (float) textColor.getRed(),
        (float) textColor.getGreen(),
        (float) textColor.getBlue(),
        (float) textColor.getAlpha());
  }
}
