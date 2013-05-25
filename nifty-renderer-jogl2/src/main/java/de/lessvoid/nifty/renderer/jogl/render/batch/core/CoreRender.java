package de.lessvoid.nifty.renderer.jogl.render.batch.core;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

/**
 * Simple helper methods to render vertex arrays.
 * @author void
 */
public class CoreRender {

  /**
   * Render the currently active VAO using triangle strips with the given
   * number of vertices.
   *
   * @param count number of vertices to render as triangle strips
   */
  public static void renderTriangleStrip(final int count) {
    final GL gl = GLContext.getCurrentGL();
    gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, count);
    CoreCheckGL.checkGLError("glDrawArrays");
  }

  /**
   * Render the currently active VAO using triangle fan with the given number of vertices.
   *
   * @param count number of vertices to render as triangle fan
   */
  public static void renderTriangleFan(final int count) {
    final GL gl = GLContext.getCurrentGL();
    gl.glDrawArrays(GL.GL_TRIANGLE_FAN, 0, count);
    CoreCheckGL.checkGLError("glDrawArrays");
  }

  /**
   * Render the currently active VAO using triangle strips with the given
   * number of vertices AND do that primCount times.
   *
   * @param count number of vertices to render as triangle strips per primitve
   * @param primCount number of primitives to render
   */
  public static void renderTriangleStripInstances(final int count, int primCount) {
    final GL gl = GLContext.getCurrentGL();
    gl.getGL3().glDrawArraysInstanced(GL.GL_TRIANGLE_STRIP, 0, count, primCount);
    CoreCheckGL.checkGLError("glDrawArraysInstanced(GL_TRIANGLE_STRIP)");
  }

  /**
   * Render the currently active VAO using triangle strips, sending the given number of indizes.
   *
   * @param count number of indizes to render as triangle strips
   */
  public static void renderTriangleStripIndexed(final int count) {
    final GL gl = GLContext.getCurrentGL();
    gl.glDrawElements(GL.GL_TRIANGLE_STRIP, count, GL.GL_UNSIGNED_INT, 0);
    CoreCheckGL.checkGLError("glDrawElements(GL_TRIANGLE_STRIP)");
  }

  /**
   * Render the currently active VAO using triangle fans, sending the given number of indizes.
   *
   * @param count number of indizes to render as triangle fans.
   */
  public static void renderTriangleFanIndexed(final int count) {
    final GL gl = GLContext.getCurrentGL();
    gl.glDrawElements(GL.GL_TRIANGLE_FAN, count, GL.GL_UNSIGNED_INT, 0);
    CoreCheckGL.checkGLError("glDrawElements(GL_TRIANGLE_FAN)");
  }

  /**
   * Render the currently active VAO using triangles with the given
   * number of vertices.
   *
   * @param vertexCount number of vertices to render as triangle strips
   */
  public static void renderTriangles(final int vertexCount) {
    final GL gl = GLContext.getCurrentGL();
    gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertexCount);
    CoreCheckGL.checkGLError("glDrawArrays");
  }

  /**
   * Render the currently active VAO using triangles with the given
   * number of vertices.
   *
   * @param count number of vertices to render as triangle strips
   */
  public static void renderTrianglesIndexed(final int count) {
    final GL gl = GLContext.getCurrentGL();
    gl.glDrawElements(GL.GL_TRIANGLES, count, GL.GL_UNSIGNED_INT, 0);
    CoreCheckGL.checkGLError("glDrawElements");
  }

  /**
   * Render the currently active VAO using points with the given
   * number of vertices.
   *
   * @param count number of vertices to render as points
   */
  public static void renderPoints(final int count) {
    final GL gl = GLContext.getCurrentGL();
    gl.glDrawArrays(GL.GL_POINTS, 0, count);
    CoreCheckGL.checkGLError("glDrawArrays(GL_POINTS)");
  }

  /**
   * Render the currently active VAO using points with the given
   * number of vertices AND do that primCount times.
   *
   * @param count number of vertices to render as points per primitive
   * @param primCount number of primitives to render
   */
  public static void renderPointsInstances(final int count, int primCount) {
    final GL gl = GLContext.getCurrentGL();
    gl.getGL3().glDrawArraysInstanced(GL.GL_POINTS, 0, count, primCount);
    CoreCheckGL.checkGLError("glDrawArraysInstanced(GL_POINTS)");
  }
}
