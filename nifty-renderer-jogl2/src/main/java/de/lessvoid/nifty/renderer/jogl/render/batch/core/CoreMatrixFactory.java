package de.lessvoid.nifty.renderer.jogl.render.batch.core;

import de.lessvoid.nifty.renderer.jogl.math.Mat4;

import javax.annotation.Nonnull;


/**
 * Creates LWJGL Matrix4j instances for projection- and orthographic  projection.
 *
 * @author void
 */
public class CoreMatrixFactory {

  /**
   * Create orthographic projection Matrix4f.
   *
   * @param width  width
   * @param height height
   * @return new Matrix4j
   */
  @Nonnull
  public static Mat4 createOrtho(final float left, final float right, final float bottom, final float top) {
    float zNear = -9999;
    float zFar = 9999;

    Mat4 projection = new Mat4();
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
   * Create perspective projection Matrix4f in glFrustrum() style =)
   *
   * @param left   left
   * @param right  right
   * @param top    top
   * @param bottom bottom
   * @param zNear  z near value (for example 1)
   * @param zFar   z far value (for example 10000)
   * @return new perspective projection matrix
   */
  @Nonnull
  public static Mat4 createProjection(
      final float left,
      final float right,
      final float top,
      final float bottom,
      final float zNear,
      final float zFar) {
    Mat4 projection = new Mat4();
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

  @Nonnull
  public static Mat4 createProjection(
      final float fov,
      final float aspectRatio,
      final float zNear,
      final float zFar) {
    Mat4 projection = new Mat4();
    float e = 1.f / (float) Math.tan(fov / 2.f);
    projection.m00 = e;
    projection.m11 = e / aspectRatio;
    projection.m22 = -(zFar + zNear) / (zFar - zNear);
    projection.m23 = -1.f;
    projection.m32 = -2.f * zFar * zNear / (zFar - zNear);
    projection.m33 = 0.f;
    return projection;
  }

  @Nonnull
  public static Mat4 createProjection2(
      final float fov,
      final float aspectRatio,
      final float zNear,
      final float zFar) {
    float DEG2RAD = (float) (Math.PI / 180.f);

    float tangent = (float) Math.tan(fov / 2 * DEG2RAD);   // tangent of half fovY
    float height = zNear * tangent;          // half height of near plane
    float width = height * aspectRatio;      // half width of near plane

    // params: left, right, bottom, top, near, far
    return createProjection2(-width, width, -height, height, zNear, zFar);
  }

  @Nonnull
  public static Mat4 createProjection2(
      final float left,
      final float right,
      final float bottom,
      final float top,
      final float near,
      final float far) {
    float A = -(right + left) / (right - left);
    float B = -(top + bottom) / (top - bottom);
    float C = -(far + near) / (far - near);
    float D = -2 * far * near / (far - near);

    Mat4 projection = new Mat4();
    projection.m00 = 2.f / (right - left);
    projection.m20 = A;
    projection.m11 = 2.f / (top - bottom);
    projection.m21 = B;
    projection.m22 = C;
    projection.m32 = D;
    projection.m23 = -1.f;
    projection.m33 = 0.f;
    return projection;
  }

}
