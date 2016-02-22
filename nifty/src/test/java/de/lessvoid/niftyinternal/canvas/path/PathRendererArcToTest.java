package de.lessvoid.niftyinternal.canvas.path;

import de.lessvoid.nifty.NiftyRuntimeException;
import de.lessvoid.niftyinternal.canvas.LineParameters;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.render.batch.BatchManager;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class PathRendererArcToTest {
  private PathRenderer pathRenderer;
  private LineParameters lineParameters = new LineParameters();;
  private Mat4 transform = new Mat4();
  private BatchManager batchManager;

  @Before
  public void before() {
    pathRenderer = new PathRenderer();
    batchManager = createMock(BatchManager.class);
  }

  @After
  public void after() {
    verify(batchManager);
  }

  @Test
  public void testWithoutBeginPath() {
    replay(batchManager);

    pathRenderer.arcTo(300, 25, 500, 225, 75);
  }

  @Test(expected = NiftyRuntimeException.class)
  public void testNegativeRadiusNotAllowed() {
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arcTo(300, 25, 500, 225, -75);
  }

  @Test
  public void testWithoutMoveToNoStroke() {
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arcTo(300, 25, 500, 225, 75);
  }

  @Test
  public void testWithMoveToNoStroke() {
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.moveTo(100, 225);
    pathRenderer.arcTo(300, 25, 500, 225, 75);
  }

  @Test
  public void testWithoutMoveToStroke() {
    batchManager.addFirstLineVertex(300, 25, transform, lineParameters);
    batchManager.addLineVertex(300, 25, transform, lineParameters);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arcTo(300, 25, 500, 225, 75);
    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  @Test
  public void testWithoutMoveToAndLineToStroke() {
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.moveTo(100, 225);
    pathRenderer.arcTo(300, 25, 500, 225, 75);
    pathRenderer.lineTo(500, 225);
  }

  @Test
  public void testWithMoveToStroke() {
    batchManager.addFirstLineVertex(100.f, 225.f, transform, lineParameters);
    float cx = (float) (Math.cos(3.9269908169872414) * 75.0 + 300.0);
    float cy = (float) (Math.sin(3.9269908169872414) * 75.0 + 131.06600952148438);
    batchManager.addLineVertex(cx, cy, transform, lineParameters);
    expectArc(batchManager, 300.0, 131.06600952148438, 75.0, 3.9269908169872414, 5.497787143782138);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.moveTo(100, 225);
    pathRenderer.arcTo(300, 25, 500, 225, 75);
    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  @Test
  public void testWithMoveToAndLineToStroke() {
    batchManager.addFirstLineVertex(100.f, 225.f, transform, lineParameters);
    float cx = (float) (Math.cos(3.9269908169872414) * 75.0 + 300.0);
    float cy = (float) (Math.sin(3.9269908169872414) * 75.0 + 131.06600952148438);
    batchManager.addLineVertex(cx, cy, transform, lineParameters);
    expectArc(batchManager, 300.0, 131.06600952148438, 75.0, 3.9269908169872414, 5.497787143782138);
    batchManager.addLineVertex(500.f, 225.f, transform, lineParameters);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.moveTo(100, 225);
    pathRenderer.arcTo(300, 25, 500, 225, 75);
    pathRenderer.lineTo(500, 225);
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
      batchManager.addLineVertex(eq((float) cx), eq((float) cy), eq(transform), eq(lineParameters));
    }
  }
}
