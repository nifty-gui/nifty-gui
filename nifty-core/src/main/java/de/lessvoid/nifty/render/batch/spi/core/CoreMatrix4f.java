package de.lessvoid.nifty.render.batch.spi.core;

import java.nio.FloatBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a 4x4 floating point matrix.
 *
 * @author foo
 * @author void256
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CoreMatrix4f {
  public float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;

  /**
   * Constructs a new matrix initialized to the identity.
   */
  public CoreMatrix4f() {
    setIdentity();
  }

  /**
   * Constructs a new matrix populated with the values of the specified source matrix.
   */
  public CoreMatrix4f(@Nonnull final CoreMatrix4f src) {
    load(src);
  }

  /**
   * Returns a string representation of this matrix.
   */
  @Override
  @Nonnull
  public String toString() {
    return String.valueOf(m00) + ' ' + m10 + ' ' + m20 + ' ' + m30 + '\n' + m01 + ' ' + m11 + ' ' + m21 + ' ' + m31 +
            '\n' + m02 + ' ' + m12 + ' ' + m22 + ' ' + m32 + '\n' + m03 + ' ' + m13 + ' ' + m23 + ' ' + m33 + '\n';
  }

  /**
   * Sets this matrix to be the identity matrix.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f setIdentity() {
    return setIdentity(this);
  }

  /**
   * Sets the specified matrix to be the identity matrix.
   *
   * @param m The matrix to set to the identity matrix.
   *
   * @return The specified matrix.
   */
  @Nonnull
  public static CoreMatrix4f setIdentity(@Nonnull CoreMatrix4f m) {
    m.m00 = 1.0f;
    m.m01 = 0.0f;
    m.m02 = 0.0f;
    m.m03 = 0.0f;
    m.m10 = 0.0f;
    m.m11 = 1.0f;
    m.m12 = 0.0f;
    m.m13 = 0.0f;
    m.m20 = 0.0f;
    m.m21 = 0.0f;
    m.m22 = 1.0f;
    m.m23 = 0.0f;
    m.m30 = 0.0f;
    m.m31 = 0.0f;
    m.m32 = 0.0f;
    m.m33 = 1.0f;

    return m;
  }

  /**
   * Sets this matrix to all zero values.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f setZero() {
    return setZero(this);
  }

  /**
   * Set the specified matrix to all zero values.
   *
   * @param m The matrix to set to all zero values.
   *
   * @return The specified matrix.
   */
  @Nonnull
  public static CoreMatrix4f setZero(@Nonnull CoreMatrix4f m) {
    m.m00 = 0.0f;
    m.m01 = 0.0f;
    m.m02 = 0.0f;
    m.m03 = 0.0f;
    m.m10 = 0.0f;
    m.m11 = 0.0f;
    m.m12 = 0.0f;
    m.m13 = 0.0f;
    m.m20 = 0.0f;
    m.m21 = 0.0f;
    m.m22 = 0.0f;
    m.m23 = 0.0f;
    m.m30 = 0.0f;
    m.m31 = 0.0f;
    m.m32 = 0.0f;
    m.m33 = 0.0f;

    return m;
  }

  /**
   * Creates a translation matrix.
   *
   * @return The translation matrix.
   */
  @Nonnull
  public static CoreMatrix4f createTranslation(final float x, final float y, final float z) {
    CoreMatrix4f result = new CoreMatrix4f();

    result.m30 = x;
    result.m31 = y;
    result.m32 = z;

    return result;
  }

  /**
   * Creates a scaling matrix.
   *
   * @return The scaling matrix.
   */
  @Nonnull
  public static CoreMatrix4f createScaling(final float x, final float y, final float z) {
    CoreMatrix4f result = new CoreMatrix4f();

    result.m00 = x;
    result.m11 = y;
    result.m22 = z;

    return result;
  }

  /**
   * Creates a rotation matrix.
   *
   * @return The rotation matrix.
   */
  @Nonnull
  public static CoreMatrix4f createRotation(final float angle, final float x, final float y, final float z) {
    double angleRad = angle * Math.PI / 180.;
    float c = (float) Math.cos(angleRad);
    float s = (float) Math.sin(angleRad);

    CoreMatrix4f result = new CoreMatrix4f();

    result.m00 = x * x * (1 - c) + c;
    result.m10 = x * y * (1 - c) - z * s;
    result.m20 = x * z * (1 - c) + y * s;
    result.m30 = 0;

    result.m01 = y * x * (1 - c) + z * s;
    result.m11 = y * y * (1 - c) + c;
    result.m21 = y * z * (1 - c) - x * s;
    result.m31 = 0;

    result.m02 = x * z * (1 - c) - y * s;
    result.m12 = y * z * (1 - c) + x * s;
    result.m22 = z * z * (1 - c) + c;
    result.m32 = 0;

    result.m03 = 0;
    result.m13 = 0;
    result.m23 = 0;
    result.m33 = 1;

    return result;
  }

  /**
   * Populates this matrix from the specified source matrix.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f load(@Nonnull final CoreMatrix4f src) {
    return load(src, this);
  }

  /**
   * Copies the source matrix into the destination matrix
   *
   * @param src  The source matrix.
   * @param dest The destination matrix, or null to create a new matrix.
   *
   * @return The copied matrix.
   */
  @Nonnull
  public static CoreMatrix4f load(@Nonnull final CoreMatrix4f src, @Nullable CoreMatrix4f dest) {
    if (dest == null) {
      dest = new CoreMatrix4f();
    }

    dest.m00 = src.m00;
    dest.m01 = src.m01;
    dest.m02 = src.m02;
    dest.m03 = src.m03;
    dest.m10 = src.m10;
    dest.m11 = src.m11;
    dest.m12 = src.m12;
    dest.m13 = src.m13;
    dest.m20 = src.m20;
    dest.m21 = src.m21;
    dest.m22 = src.m22;
    dest.m23 = src.m23;
    dest.m30 = src.m30;
    dest.m31 = src.m31;
    dest.m32 = src.m32;
    dest.m33 = src.m33;

    return dest;
  }

  /**
   * Populates this matrix from a {@link java.nio.FloatBuffer}. The buffer stores the matrix in column major (OpenGL)
   * order.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f load(@Nonnull final FloatBuffer buffer) {
    m00 = buffer.get();
    m01 = buffer.get();
    m02 = buffer.get();
    m03 = buffer.get();
    m10 = buffer.get();
    m11 = buffer.get();
    m12 = buffer.get();
    m13 = buffer.get();
    m20 = buffer.get();
    m21 = buffer.get();
    m22 = buffer.get();
    m23 = buffer.get();
    m30 = buffer.get();
    m31 = buffer.get();
    m32 = buffer.get();
    m33 = buffer.get();

    return this;
  }

  /**
   * Populates this matrix from a {@link java.nio.FloatBuffer}. The buffer stores the matrix in row major (maths)
   * order.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f loadTransposed(@Nonnull final FloatBuffer buffer) {
    m00 = buffer.get();
    m10 = buffer.get();
    m20 = buffer.get();
    m30 = buffer.get();
    m01 = buffer.get();
    m11 = buffer.get();
    m21 = buffer.get();
    m31 = buffer.get();
    m02 = buffer.get();
    m12 = buffer.get();
    m22 = buffer.get();
    m32 = buffer.get();
    m03 = buffer.get();
    m13 = buffer.get();
    m23 = buffer.get();
    m33 = buffer.get();

    return this;
  }

  /**
   * Store this matrix in a {@link java.nio.FloatBuffer}. The matrix is stored in column major (OpenGL) order.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f store(@Nonnull final FloatBuffer buffer) {
    buffer.put(m00);
    buffer.put(m01);
    buffer.put(m02);
    buffer.put(m03);
    buffer.put(m10);
    buffer.put(m11);
    buffer.put(m12);
    buffer.put(m13);
    buffer.put(m20);
    buffer.put(m21);
    buffer.put(m22);
    buffer.put(m23);
    buffer.put(m30);
    buffer.put(m31);
    buffer.put(m32);
    buffer.put(m33);

    return this;
  }

  /**
   * Store this matrix in a {@link java.nio.FloatBuffer}. The matrix is stored in row major (maths) order.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f storeTransposed(@Nonnull final FloatBuffer buffer) {
    buffer.put(m00);
    buffer.put(m10);
    buffer.put(m20);
    buffer.put(m30);
    buffer.put(m01);
    buffer.put(m11);
    buffer.put(m21);
    buffer.put(m31);
    buffer.put(m02);
    buffer.put(m12);
    buffer.put(m22);
    buffer.put(m32);
    buffer.put(m03);
    buffer.put(m13);
    buffer.put(m23);
    buffer.put(m33);

    return this;
  }

  /**
   * Stores the rotation portion of this matrix in a {@link java.nio.FloatBuffer}. The matrix is stored in column major
   * (OpenGL) order.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f store3f(@Nonnull final FloatBuffer buffer) {
    buffer.put(m00);
    buffer.put(m01);
    buffer.put(m02);
    buffer.put(m10);
    buffer.put(m11);
    buffer.put(m12);
    buffer.put(m20);
    buffer.put(m21);
    buffer.put(m22);

    return this;
  }

  /**
   * Adds the two specified matrices together and stores the result in the specified result matrix.
   *
   * @param left  The left source matrix to add.
   * @param right The right source matrix to add.
   * @param result The matrix containing the result of the addition, or null to create a new matrix.
   *
   * @return The result matrix.
   */
  @Nonnull
  public static CoreMatrix4f add(
          @Nonnull final CoreMatrix4f left,
          @Nonnull final CoreMatrix4f right,
          @Nullable CoreMatrix4f result) {
    if (result == null) {
      result = new CoreMatrix4f();
    }

    result.m00 = left.m00 + right.m00;
    result.m01 = left.m01 + right.m01;
    result.m02 = left.m02 + right.m02;
    result.m03 = left.m03 + right.m03;
    result.m10 = left.m10 + right.m10;
    result.m11 = left.m11 + right.m11;
    result.m12 = left.m12 + right.m12;
    result.m13 = left.m13 + right.m13;
    result.m20 = left.m20 + right.m20;
    result.m21 = left.m21 + right.m21;
    result.m22 = left.m22 + right.m22;
    result.m23 = left.m23 + right.m23;
    result.m30 = left.m30 + right.m30;
    result.m31 = left.m31 + right.m31;
    result.m32 = left.m32 + right.m32;
    result.m33 = left.m33 + right.m33;

    return result;
  }

  /**
   * Subtracts the specified right matrix from the specified left matrix and stores the result in the specified result
   * matrix.
   *
   * @param left  The left source matrix to subtract.
   * @param right The right source matrix to subtract.
   * @param result The matrix containing the result of the subtraction, or null to create a new matrix.
   *
   * @return The result matrix.
   */
  @Nonnull
  public static CoreMatrix4f subtract(
          @Nonnull final CoreMatrix4f left,
          @Nonnull final CoreMatrix4f right,
          @Nullable CoreMatrix4f result) {
    if (result == null) {
      result = new CoreMatrix4f();
    }

    result.m00 = left.m00 - right.m00;
    result.m01 = left.m01 - right.m01;
    result.m02 = left.m02 - right.m02;
    result.m03 = left.m03 - right.m03;
    result.m10 = left.m10 - right.m10;
    result.m11 = left.m11 - right.m11;
    result.m12 = left.m12 - right.m12;
    result.m13 = left.m13 - right.m13;
    result.m20 = left.m20 - right.m20;
    result.m21 = left.m21 - right.m21;
    result.m22 = left.m22 - right.m22;
    result.m23 = left.m23 - right.m23;
    result.m30 = left.m30 - right.m30;
    result.m31 = left.m31 - right.m31;
    result.m32 = left.m32 - right.m32;
    result.m33 = left.m33 - right.m33;

    return result;
  }

  /**
   * Multiplies the specified right matrix by the specified left matrix and returns the result matrix.
   *
   * @param left  The left source matrix.
   * @param right The right source matrix.
   *
   * @return The result matrix.
   */
  @Nonnull
  public static CoreMatrix4f multiply(@Nonnull final CoreMatrix4f left, @Nonnull final CoreMatrix4f right) {
    return CoreMatrix4f.multiply(left, right, null);
  }

  /**
   * Multiplies the specified right matrix by the specified left matrix and stores the result in the specified result
   * matrix.
   *
   * @param left  The left source matrix.
   * @param right The right source matrix.
   * @param result The matrix containing the result of the multiplication, or null to create a new matrix.
   *
   * @return The result matrix.
   */
  @Nonnull
  public static CoreMatrix4f multiply(
          @Nonnull final CoreMatrix4f left,
          @Nonnull final CoreMatrix4f right,
          @Nullable CoreMatrix4f result) {
    if (result == null) {
      result = new CoreMatrix4f();
    }

    float m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
    float m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
    float m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
    float m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
    float m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
    float m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
    float m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
    float m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
    float m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
    float m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
    float m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
    float m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
    float m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
    float m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
    float m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
    float m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;

    result.m00 = m00;
    result.m01 = m01;
    result.m02 = m02;
    result.m03 = m03;
    result.m10 = m10;
    result.m11 = m11;
    result.m12 = m12;
    result.m13 = m13;
    result.m20 = m20;
    result.m21 = m21;
    result.m22 = m22;
    result.m23 = m23;
    result.m30 = m30;
    result.m31 = m31;
    result.m32 = m32;
    result.m33 = m33;

    return result;
  }

  /**
   * Transposes this matrix.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f transpose() {
    return transpose(this);
  }

  /**
   * Transposes this matrix and stores the result in the specified result matrix.
   *
   * @param result The matrix containing the result of the transpose, or null to create a new matrix.
   *
   * @return The result matrix.
   */
  @Nonnull
  public CoreMatrix4f transpose(@Nullable final CoreMatrix4f result) {
    return transpose(this, result);
  }

  /**
   * Transposes the specified source matrix and stores the result in the specified result matrix.
   *
   * @param src  The source matrix.
   * @param result The matrix containing the result of the transpose, or null to create a new matrix.
   *
   * @return The result matrix.
   */
  @Nonnull
  public static CoreMatrix4f transpose(@Nonnull final CoreMatrix4f src, @Nullable CoreMatrix4f result) {
    if (result == null) {
      result = new CoreMatrix4f();
    }

    float m00 = src.m00;
    float m01 = src.m10;
    float m02 = src.m20;
    float m03 = src.m30;
    float m10 = src.m01;
    float m11 = src.m11;
    float m12 = src.m21;
    float m13 = src.m31;
    float m20 = src.m02;
    float m21 = src.m12;
    float m22 = src.m22;
    float m23 = src.m32;
    float m30 = src.m03;
    float m31 = src.m13;
    float m32 = src.m23;
    float m33 = src.m33;

    result.m00 = m00;
    result.m01 = m01;
    result.m02 = m02;
    result.m03 = m03;
    result.m10 = m10;
    result.m11 = m11;
    result.m12 = m12;
    result.m13 = m13;
    result.m20 = m20;
    result.m21 = m21;
    result.m22 = m22;
    result.m23 = m23;
    result.m30 = m30;
    result.m31 = m31;
    result.m32 = m32;
    result.m33 = m33;

    return result;
  }

  /**
   * Calculates and returns the determinant of this matrix.
   */
  public float determinant() {
    float f =
        m00
            * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
            - m13 * m22 * m31
            - m11 * m23 * m32
            - m12 * m21 * m33);
    f -= m01
            * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
            - m13 * m22 * m30
            - m10 * m23 * m32
            - m12 * m20 * m33);
    f += m02
            * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
            - m13 * m21 * m30
            - m10 * m23 * m31
            - m11 * m20 * m33);
    f -= m03
            * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
            - m12 * m21 * m30
            - m10 * m22 * m31
            - m11 * m20 * m32);
    return f;
  }

  /**
   * Inverts this matrix.
   *
   * @return This matrix if the inversion was successful, null otherwise.
   */
  @Nullable
  public CoreMatrix4f invert() {
    return invert(this, this);
  }

  /**
   * Inverts the specified source matrix and stores the result in the specified result matrix.
   *
   * @param src  The source matrix.
   * @param result The matrix containing the result of the inversion, or null to create a new matrix.
   *
   * @return The result matrix if the inversion was successful, null otherwise.
   */
  @Nullable
  public static CoreMatrix4f invert(@Nonnull final CoreMatrix4f src, @Nullable CoreMatrix4f result) {
    float determinant = src.determinant();

    if (determinant != 0) {

      if (result == null) {
        result = new CoreMatrix4f();
      }

      float determinant_inv = 1f / determinant;

      // first row
      float t00 = determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
      float t01 = -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
      float t02 = determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
      float t03 = -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
      // second row
      float t10 = -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
      float t11 = determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
      float t12 = -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
      float t13 = determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
      // third row
      float t20 = determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33);
      float t21 = -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33);
      float t22 = determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33);
      float t23 = -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32);
      // fourth row
      float t30 = -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23);
      float t31 = determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23);
      float t32 = -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23);
      float t33 = determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22);

      // transpose and divide by the determinant
      result.m00 = t00 * determinant_inv;
      result.m11 = t11 * determinant_inv;
      result.m22 = t22 * determinant_inv;
      result.m33 = t33 * determinant_inv;
      result.m01 = t10 * determinant_inv;
      result.m10 = t01 * determinant_inv;
      result.m20 = t02 * determinant_inv;
      result.m02 = t20 * determinant_inv;
      result.m12 = t21 * determinant_inv;
      result.m21 = t12 * determinant_inv;
      result.m03 = t30 * determinant_inv;
      result.m30 = t03 * determinant_inv;
      result.m13 = t31 * determinant_inv;
      result.m31 = t13 * determinant_inv;
      result.m32 = t23 * determinant_inv;
      result.m23 = t32 * determinant_inv;

      return result;
    } else
      return null;
  }

  /**
   * Negates this matrix.
   *
   * @return This matrix.
   */
  @Nonnull
  public CoreMatrix4f negate() {
    return negate(this);
  }

  /**
   * Negates this matrix and stores the result in the specified result matrix.
   *
   * @param result The matrix containing the result of the negation, or null to create a new matrix.
   *
   * @return The result matrix.
   */
  @Nonnull
  public CoreMatrix4f negate(@Nullable final CoreMatrix4f result) {
    return negate(this, this);
  }

  /**
   * Negates the specified source matrix and stores the result in the specified result matrix.
   *
   * @param src  The source matrix.
   * @param result The matrix containing the result of the negation, or null to create a new matrix.
   *
   * @return The result matrix.
   */
  @Nonnull
  public static CoreMatrix4f negate(@Nonnull final CoreMatrix4f src, @Nullable CoreMatrix4f result) {
    if (result == null) {
      result = new CoreMatrix4f();
    }

    result.m00 = -src.m00;
    result.m01 = -src.m01;
    result.m02 = -src.m02;
    result.m03 = -src.m03;
    result.m10 = -src.m10;
    result.m11 = -src.m11;
    result.m12 = -src.m12;
    result.m13 = -src.m13;
    result.m20 = -src.m20;
    result.m21 = -src.m21;
    result.m22 = -src.m22;
    result.m23 = -src.m23;
    result.m30 = -src.m30;
    result.m31 = -src.m31;
    result.m32 = -src.m32;
    result.m33 = -src.m33;

    return result;
  }

  /**
   * Creates a new identity matrix.
   *
   * @return The new identity matrix.
   */
  @Nonnull
  public static CoreMatrix4f createIdentity() {
    return new CoreMatrix4f();
  }

  /**
   * Transforms the specified left vector by the specified right matrix and returns the result vector.
   *
   * @param left  The left matrix.
   * @param right The right vector.
   *
   * @return The result vector.
   */
  @Nonnull
  public static CoreVector4f transform(@Nonnull final CoreMatrix4f left, @Nonnull final CoreVector4f right) {
    return transform(left, right, null);
  }

  /**
   * Transforms the specified left vector by the specified right matrix and stores the result in the specified result
   * vector.
   *
   * @param left  The left matrix.
   * @param right The right vector.
   * @param result The vector containing the result of the transformation, or null to create a new vector.
   *
   * @return The result vector.
   */
  @Nonnull
  public static CoreVector4f transform(
          @Nonnull final CoreMatrix4f left,
          @Nonnull final CoreVector4f right,
          @Nullable CoreVector4f result) {
    if (result == null) {
      result = new CoreVector4f();
    }

    float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * right.w;
    float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * right.w;
    float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * right.w;
    float w = left.m03 * right.x + left.m13 * right.y + left.m23 * right.z + left.m33 * right.w;

    result.x = x;
    result.y = y;
    result.z = z;
    result.w = w;

    return result;
  }

  /**
   * Compares this matrix with the specified other matrix and returns true if they are equal and false otherwise.
   *
   * @param other The matrix to compare with this matrix.
   *
   * @return The result of the comparison.
   */
  public boolean compare(@Nonnull final CoreMatrix4f other) {
    return
        equals(this.m00, other.m00) &&
                equals(this.m01, other.m01) &&
                equals(this.m02, other.m02) &&
                equals(this.m03, other.m03) &&
                equals(this.m10, other.m10) &&
                equals(this.m11, other.m11) &&
                equals(this.m12, other.m12) &&
                equals(this.m13, other.m13) &&
                equals(this.m20, other.m20) &&
                equals(this.m21, other.m21) &&
                equals(this.m22, other.m22) &&
                equals(this.m23, other.m23) &&
                equals(this.m30, other.m30) &&
                equals(this.m31, other.m31) &&
                equals(this.m32, other.m32) &&
                equals(this.m33, other.m33);
  }

  // Internal implementations

  // Calculates and returns the determinant of the specified 3x3 matrix.
  private static float determinant3x3(
          float t00, float t01, float t02,
          float t10, float t11, float t12,
          float t20, float t21, float t22) {
    return t00 * (t11 * t22 - t12 * t21) +
           t01 * (t12 * t20 - t10 * t22) +
           t02 * (t10 * t21 - t11 * t20);
  }

  private boolean equals(final float a, final float b) {
    return Math.abs(a - b) < 0.00000001f;
  }
}

