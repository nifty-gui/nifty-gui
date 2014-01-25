package de.lessvoid.nifty.render.batch.spi.core;

import javax.annotation.Nonnull;

/**
 * Creates projection matrices.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CoreMatrixFactory {
  /**
   * Creates an orthographic (2-dimensional) projection matrix.
   */
  @Nonnull
  public static CoreMatrix4f createOrthoMatrix(
          final float left,
          final float right,
          final float bottom,
          final float top) {
    float zNear = -9999;
    float zFar = 9999;

    CoreMatrix4f projection = new CoreMatrix4f();

    projection.m00 = 2 / (right - left);
    projection.m11 = 2 / (top - bottom);
    projection.m22 = -2 / (zFar - zNear);
    projection.m30 = -(right + left) / (right - left);
    projection.m31 = -(top + bottom) / (top - bottom);
    projection.m32 = -(zFar + zNear) / (zFar - zNear);
    projection.m33 = 1;

    return projection;
  }

  /**
   * Creates a perspective projection matrix using only x, y, & z-axis frustum bounds.
   */
  public static CoreMatrix4f createPerspectiveMatrix(
          final float left,
          final float right,
          final float top,
          final float bottom,
          final float zNear,
          final float zFar) {
    CoreMatrix4f projection = new CoreMatrix4f();

    projection.m00 = 2.f * zNear / (right - left);
    projection.m11 = 2.f * zNear / (top - bottom);
    projection.m20 = (right + left) / (right - left);
    projection.m21 = (top + bottom) / (top - bottom);
    projection.m22 = -(zFar + zNear) / (zFar - zNear);
    projection.m23 = -1.f;
    projection.m32 = -2.f * zFar * zNear / (zFar - zNear);
    projection.m33 = 0.f;

    return projection;
  }

  /**
   * Creates a perspective projection matrix using FOV (field-of-view), aspect ratio, and z-axis frustum bounds.
   */
  @Nonnull
  public static CoreMatrix4f createPerspectiveMatrix(
          final float fov,
          final float aspectRatio,
          final float zNear,
          final float zFar) {
    CoreMatrix4f projection = new CoreMatrix4f();

    float e = 1.f / (float) Math.tan(fov / 2.f);

    projection.m00 = e;
    projection.m11 = e / aspectRatio;
    projection.m22 = -(zFar + zNear) / (zFar - zNear);
    projection.m23 = -1.f;
    projection.m32 = -2.f * zFar * zNear / (zFar - zNear);
    projection.m33 = 0.f;

    return projection;
  }
}
