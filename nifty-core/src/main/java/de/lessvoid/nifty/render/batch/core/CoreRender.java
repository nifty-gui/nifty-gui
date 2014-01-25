package de.lessvoid.nifty.render.batch.core;

import de.lessvoid.nifty.render.batch.CheckGL;
import de.lessvoid.nifty.render.batch.spi.core.CoreGL;

import javax.annotation.Nonnull;

/**
 * Simple helper methods to render vertex arrays.
 *
 * Note: Requires OpenGL 3.2 or greater.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CoreRender {

  /**
   * Renders the currently active VAO using triangle strips with the specified number of vertices.
   *
   * @param count The number of vertices to render as triangle strips.
   */
  public static void renderTriangleStrip(@Nonnull final CoreGL gl, final int count) {
    gl.glDrawArrays(gl.GL_TRIANGLE_STRIP(), 0, count);
    CheckGL.checkGLError(gl, "glDrawArrays");
  }

  /**
   * Renders the currently active VAO using triangle fan with the specified number of vertices.
   *
   * @param count The number of vertices to render as a triangle fan.
   */
  public static void renderTriangleFan(@Nonnull final CoreGL gl, final int count) {
    gl.glDrawArrays(gl.GL_TRIANGLE_FAN(), 0, count);
    CheckGL.checkGLError(gl, "glDrawArrays");
  }

  /**
   * Renders the currently active VAO using triangle strips with the specified number of vertices AND does that
   * primCount times.
   *
   * @param count     The number of vertices to render as triangle strips per primitive.
   * @param primCount The number of primitives to render.
   */
  public static void renderTriangleStripInstances(@Nonnull final CoreGL gl, final int count, int primCount) {
    gl.glDrawArraysInstanced(gl.GL_TRIANGLE_STRIP(), 0, count, primCount);
    CheckGL.checkGLError(gl, "glDrawArraysInstanced(GL_TRIANGLE_STRIP)");
  }

  /**
   * Renders the currently active VAO using triangle strips, sending the specified number of indices.
   *
   * @param count The number of indices to render as triangle strips.
   */
  public static void renderTriangleStripIndexed(@Nonnull final CoreGL gl, final int count) {
    gl.glDrawElements(gl.GL_TRIANGLE_STRIP(), count, gl.GL_UNSIGNED_INT(), 0);
    CheckGL.checkGLError(gl, "glDrawElements(GL_TRIANGLE_STRIP)");
  }

  /**
   * Renders the currently active VAO using triangle fans, sending the specified number of indices.
   *
   * @param count The number of indices to render as triangle fans.
   */
  public static void renderTriangleFanIndexed(@Nonnull final CoreGL gl, final int count) {
    gl.glDrawElements(gl.GL_TRIANGLE_FAN(), count, gl.GL_UNSIGNED_INT(), 0);
    CheckGL.checkGLError(gl, "glDrawElements(GL_TRIANGLE_FAN)");
  }

  /**
   * Renders the currently active VAO using triangles with the specified number of vertices.
   *
   * @param vertexCount The number of vertices to render as triangle strips.
   */
  public static void renderTriangles(@Nonnull final CoreGL gl, final int vertexCount) {
    gl.glDrawArrays(gl.GL_TRIANGLES(), 0, vertexCount);
    CheckGL.checkGLError(gl, "glDrawArrays");
  }

  /**
   * Renders the currently active VAO using triangles with the specified number of vertices.
   *
   * @param count The number of vertices to render as triangle strips.
   */
  public static void renderTrianglesIndexed(@Nonnull final CoreGL gl, final int count) {
    gl.glDrawElements(gl.GL_TRIANGLES(), count, gl.GL_UNSIGNED_INT(), 0);
    CheckGL.checkGLError(gl, "glDrawElements");
  }

  /**
   * Renders the currently active VAO using points with the specified number of vertices.
   *
   * @param count The number of vertices to render as points.
   */
  public static void renderPoints(@Nonnull final CoreGL gl, final int count) {
    gl.glDrawArrays(gl.GL_POINTS(), 0, count);
    CheckGL.checkGLError(gl, "glDrawArrays(GL_POINTS)");
  }

  /**
   * Renders the currently active VAO using points with the specified number of vertices AND does that primCount times.
   *
   * @param count     The number of vertices to render as points per primitive.
   * @param primCount The number of primitives to render.
   */
  public static void renderPointsInstances(@Nonnull final CoreGL gl, final int count, int primCount) {
    gl.glDrawArraysInstanced(gl.GL_POINTS(), 0, count, primCount);
    CheckGL.checkGLError(gl, "glDrawArraysInstanced(GL_POINTS)");
  }
}
