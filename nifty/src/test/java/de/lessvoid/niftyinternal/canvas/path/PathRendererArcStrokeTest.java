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
    Capture<ArcParameters> capture = newCapture();

    batchManager.addArc(eq(100.), eq(75.), eq(transform), capture(capture));
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arc(100, 75, 50, 0, 2 * Math.PI);
    pathRenderer.strokePath(lineParameters, transform, batchManager);

    assertArcParameters(capture.getValue(), 0.f, (float)(2 * Math.PI), 50.f);
  }

  @Test
  public void testStrokeConnectedArcs() {
    Capture<ArcParameters> capture = newCapture();

    batchManager.addArc(eq(100.), eq(100.), eq(transform), capture(capture));
    batchManager.addFirstLineVertex(eq(150.f), eq(100.f), eq(transform), eq(lineParameters));
    batchManager.addLineVertex(eq(462.f), eq(284.f), eq(transform), eq(lineParameters));
    batchManager.addArc(eq(412.), eq(284.), eq(transform), capture(capture));
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arc(100., 100., 50., 0., 2 * Math.PI);
    pathRenderer.arc(412., 284., 50., 0., 2 * Math.PI);
    pathRenderer.strokePath(lineParameters, transform, batchManager);

    assertArcParameters(capture.getValue(), 0.f, (float)(2 * Math.PI), 50.f);
  }

  @Test
  public void testStrokeWithClosePath() {
    Capture<ArcParameters> capture = newCapture();

    batchManager.addArc(eq(100.), eq(75.), eq(transform), capture(capture));
    batchManager.addFirstLineVertex(50.f, 75.f, transform, lineParameters);
    batchManager.addLineVertex(150.f, 75.f, transform, lineParameters);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.arc(100, 75, 50, 0, Math.PI);
    pathRenderer.closePath();
    pathRenderer.strokePath(lineParameters, transform, batchManager);

    assertArcParameters(capture.getValue(), 0.f, (float)(Math.PI), 50.f);
  }

  private void assertArcParameters(
      final ArcParameters arcParameters,
      final float startAngle,
      final float endAngle,
      final float radius) {
    assertEquals(lineParameters, arcParameters.getLineParameters());
    assertEquals(startAngle, arcParameters.getStartAngle(), 0.01);
    assertEquals(endAngle, arcParameters.getEndAngle(), 0.01);
    assertEquals(radius, arcParameters.getRadius(), 0.001);
  }
}
