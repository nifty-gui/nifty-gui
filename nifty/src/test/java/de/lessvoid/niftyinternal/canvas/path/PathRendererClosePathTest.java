package de.lessvoid.niftyinternal.canvas.path;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.niftyinternal.canvas.LineParameters;
import de.lessvoid.niftyinternal.math.Mat4;
import de.lessvoid.niftyinternal.render.batch.BatchManager;

public class PathRendererClosePathTest {
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
  public void testRegularClosePath() {
    batchManager.addFirstLineVertex( 10.f,  10.f, transform, lineParameters);
    batchManager.addLineVertex(100.f, 100.f, transform, lineParameters);
    batchManager.addLineVertex(10.f, 10.f, transform, lineParameters);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.moveTo(10, 10);
    pathRenderer.lineTo(100, 100);
    pathRenderer.closePath();

    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  @Test
  public void testRegularClosePathAfterSecondMoveTo() {
    batchManager.addFirstLineVertex( 10.f,  10.f, transform, lineParameters);
    batchManager.addLineVertex(100.f, 100.f, transform, lineParameters);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.moveTo(10, 10);
    pathRenderer.lineTo(100, 100);
    pathRenderer.moveTo(200, 200);
    pathRenderer.closePath();

    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  @Test
  public void testRegularClosePathAfterSecondMoveToWithActualLine() {
    batchManager.addFirstLineVertex(10.f, 10.f, transform, lineParameters);
    batchManager.addLineVertex(100.f, 100.f, transform, lineParameters);
    batchManager.addFirstLineVertex(200.f, 200.f, transform, lineParameters);
    batchManager.addLineVertex(200.f, 300.f, transform, lineParameters);
    batchManager.addLineVertex(300.f, 300.f, transform, lineParameters);
    batchManager.addLineVertex(200.f, 200.f, transform, lineParameters);
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.moveTo(10, 10);
    pathRenderer.lineTo(100, 100);
    pathRenderer.moveTo(200, 200);
    pathRenderer.lineTo(200, 300);
    pathRenderer.lineTo(300, 300);
    pathRenderer.closePath();

    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }

  @Test
  public void testClosePathWithMoveToOnly() {
    replay(batchManager);

    pathRenderer.beginPath();
    pathRenderer.moveTo(10, 10);
    pathRenderer.closePath();

    pathRenderer.strokePath(lineParameters, transform, batchManager);
  }
}
