/**
 * Copyright (c) 2013, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.niftyinternal.math;


/**
 * Creates Mat4 instances for projection- and ortho projection.
 * @author void
 */
public class MatrixFactory {

  /**
   * Create orthographic projection Matrix4f.
   * @param width width
   * @param height height
   * @return new Matrix4j
   */
  public static Mat4 createOrtho(final float left, final float right, final float bottom, final float top) {
    float zNear = -9999;
    float zFar = 9999;

    Mat4 projection = new Mat4();
    projection.m00 = 2 / (right-left);
    projection.m11 = 2 / (top-bottom);
    projection.m22 = -2 / (zFar-zNear);
    projection.m30 = - (right+left) / (right-left);
    projection.m31 = - (top+bottom) / (top-bottom);
    projection.m32 = - (zFar+zNear) / (zFar-zNear);
    projection.m33 = 1;
    return projection;
  }

  /**
   * Create perspective projection Matrix4f in glFrustrum() style =)
   *
   * @param left left
   * @param right right
   * @param top top
   * @param bottom bottom
   * @param zNear z near value (for example 1)
   * @param zFar z far value (for example 10000)
   * @return new perspective projection matrix
   */
  public static Mat4 createProjection(
      final float left,
      final float right,
      final float top,
      final float bottom,
      final float zNear,
      final float zFar) {
    Mat4 projection = new Mat4();
    projection.m00 = 2.f * zNear / (right-left);
    projection.m11 = 2.f * zNear / (top-bottom);
    projection.m20 = (right+left) / (right-left);
    projection.m21 = (top+bottom) / (top-bottom);
    projection.m22 = - (zFar + zNear) / (zFar-zNear);
    projection.m23 = -1.f;
    projection.m32 = - 2.f * zFar * zNear / (zFar - zNear);
    projection.m33 = 0.f;
    return projection;
  }

  /**
   * Create perspective projection Matrix4f with fov
   * @param fov
   * @param aspectRatio
   * @param zNear
   * @param zFar
   * @return
   */
  public static Mat4 createProjection(
      final float fov,
      final float aspectRatio,
      final float zNear,
      final float zFar) {
    Mat4 projection = new Mat4();
    float e = 1.f/(float)Math.tan(fov / 2.f);
    projection.m00 = e;
    projection.m11 = e / aspectRatio;
    projection.m22 = -(zFar+zNear)/(zFar-zNear);
    projection.m23 = -1.f;
    projection.m32 = - 2.f * zFar * zNear / (zFar - zNear);
    projection.m33 = 0.f;
    return projection;
  }

  public static Mat4 createProjection2(
      final float fov,
      final float aspectRatio,
      final float zNear,
      final float zFar) {
    float DEG2RAD = (float)( Math.PI / 180.f );

    float tangent = (float) Math.tan(fov/2 * DEG2RAD);   // tangent of half fovY
    float height = zNear * tangent;          // half height of near plane
    float width = height * aspectRatio;      // half width of near plane

    // params: left, right, bottom, top, near, far
    return createProjection2(-width, width, -height, height, zNear, zFar);
  }

  public static Mat4 createProjection2(
      final float left,
      final float right,
      final float bottom,
      final float top,
      final float near,
      final float far) {
    float A = - (right + left) / (right - left);
    float B = - (top + bottom) / (top - bottom);
    float C = - (far + near) / (far - near);
    float D = - 2 * far * near / (far - near);

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
