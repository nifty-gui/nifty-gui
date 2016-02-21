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

  @Test
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
    double cx = 100. + 50. * Math.cos(0);
    double cy = 75. + 50. * Math.sin(0);
    batchManager.addFirstLineVertex((float) cx, (float) cy, transform, lineParameters);
    expectArc(batchManager, 100., 75., 50., 0., 2 * Math.PI);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arc(100, 75, 50, 0, 2 * Math.PI);
    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  @Test
  public void testStrokeConnectedArcs() {
    double cx = 100. + 50. * Math.cos(0);
    double cy = 100. + 50. * Math.sin(0);
    batchManager.addFirstLineVertex((float) cx, (float) cy, transform, lineParameters);
    expectArc(batchManager, 100., 100., 50., 0., 2 * Math.PI);

    double cx2 = 412. + 50. * Math.cos(0);
    double cy2 = 284. + 50. * Math.sin(0);
    batchManager.addLineVertex((float) cx2, (float) cy2, transform, lineParameters);
    batchManager.addLineVertex((float) cx2, (float) cy2, transform, lineParameters);
    expectArc(batchManager, 412., 284., 50., 0., 2 * Math.PI);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arc(100., 100., 50., 0., 2 * Math.PI);
    pathRenderer.arc(412., 284., 50., 0., 2 * Math.PI);
    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  @Test
  public void testStrokeWithClosePath() {
    double cx = 100. + 50. * Math.cos(0);
    double cy = 75. + 50. * Math.sin(0);
    batchManager.addFirstLineVertex((float) cx, (float) cy, transform, lineParameters);
    expectArc(batchManager, 100., 75., 50., 0., Math.PI);
    batchManager.addLineVertex((float) cx, (float) cy, transform, lineParameters);
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
    for (int i=1; i<64; i++) {
      double t = i / (double) (64 - 1);

      double angle = startAngle + t * (endAngle - startAngle);
      double cx = x + r * Math.cos(angle);
      double cy = y + r * Math.sin(angle);
      batchManager.addLineVertex((float) cx, (float) cy, transform, lineParameters);
    }
  }
}
