package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.math.Vec2;
import de.lessvoid.nifty.internal.render.batch.BatchManager;

public class CommandLine implements Command {
  private final double x0;
  private final double y0;
  private final double x1;
  private final double y1;

  public CommandLine(final double x0, final double y0, final double x1, final double y1) {
    this.x0 = x0;
    this.y0 = y0;
    this.x1 = x1;
    this.y1 = y1;
  }

  @Override
  public void execute(final BatchManager batchManager, final Context context) {
    Vec2 start = new Vec2((float)x0, (float)y0);
    Vec2 end = new Vec2((float)x1, (float)y1);

    Vec2 diff = Vec2.sub(end, start, null);
    Vec2 flip = new Vec2(-diff.y, diff.x).normalise(null).scale(context.getLineWidth() / 2.f);
    Vec2 flipNeg = new Vec2(flip).negate();

    Vec2 p0 = Vec2.add(start, flip, null);
    Vec2 p1 = Vec2.add(end, flip, null);
    Vec2 p2 = Vec2.add(end, flipNeg, null);
    Vec2 p3 = Vec2.add(start, flipNeg, null);
    
//    renderTarget.filledRect(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, context.getFillColor());
  }
}
