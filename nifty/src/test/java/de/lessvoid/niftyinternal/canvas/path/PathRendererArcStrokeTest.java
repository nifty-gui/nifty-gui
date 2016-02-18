package de.lessvoid.niftyinternal.canvas.path;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

import de.lessvoid.niftyinternal.canvas.LineParameters;
import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.NiftyRuntimeException;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.render.batch.ArcParameters;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

public class PathRendererArcStrokeTest {
  private PathRenderer pathRenderer;
  private LineParameters lineParameters;
  private Mat4 transform;
  private BatchManager batchManager;

  @Before
  public void before() {
    pathRenderer = new PathRenderer();
    lineParameters = new LineParameters();
    transform = new Mat4();
    batchManager = createMock(BatchManager.class);
  }

  @After
  public void after() {
    verify(batchManager);
  }

  @Test(expected = NiftyRuntimeException.class)
  public void testWithoutBeginPath() {
    replay(batchManager);

    pathRenderer.arc(100, 75, 50, 0, 2 * Math.PI);
  }

  @Test
  public void testWithBeginPathNoStroke() {
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arc(100, 75, 50, 0, 2 * Math.PI);
  }

  @Test
  public void testStrokeSingleArc() {
    expectArc(batchManager, 100., 75., 50., 0., 2 * Math.PI);

    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arc(100, 75, 50, 0, 2 * Math.PI);
    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  @Test
  public void testStrokeConnectedArcs() {
    expectArc(batchManager, 100., 100., 50., 0., 2 * Math.PI);
    expectArc(batchManager, 412., 284., 50., 0., 2 * Math.PI);
    batchManager.addFirstLineVertex(eq(150.f), eq(100.f), eq(transform), eq(lineParameters));
    batchManager.addLineVertex(eq(462.f), eq(284.f), eq(transform), eq(lineParameters));
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arc(100., 100., 50., 0., 2 * Math.PI);
    pathRenderer.arc(412., 284., 50., 0., 2 * Math.PI);
    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  @Test
  public void testStrokeWithClosePath() {
    expectArc(batchManager, 100., 75., 50., 0., Math.PI);
    batchManager.addFirstLineVertex(50.f, 75.f, transform, lineParameters);
    batchManager.addLineVertex(150.f, 75.f, transform, lineParameters);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arc(100, 75, 50, 0, Math.PI);
    pathRenderer.closePath();
    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  private void expectArc(
      final BatchManager batchManager,
      final double x,
      final double y,
      final double r,
      final double startAngle,
      final double endAngle) {
    for (int i=0; i<64; i++) {
      double t = i / (double) (64 - 1);

      double angle = startAngle + t * (endAngle - startAngle);
      double cx = x + r * Math.cos(angle);
      double cy = y + r * Math.sin(angle);
      batchManager.addLineVertex((float) cx, (float) cy, transform, lineParameters);
    }
  }
}
