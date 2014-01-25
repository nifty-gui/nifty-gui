package de.lessvoid.nifty.render.batch.spi.core;

import java.nio.FloatBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an abstraction of a 4-dimensional [x, y, z, w] floating point vector.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @author void256
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class CoreVector4f {
  public float x, y, z, w;

  /**
   * Constructs a new vector initialized to [0.0f, 0.0f, 0.0f, 1.0f]
   */
  public CoreVector4f() {
    x = 0.0f;
    y = 0.0f;
    z = 0.0f;
    w = 1.0f;
  }

  /**
   * Constructs a new vector populated with the values of the specified source matrix.
   */
  public CoreVector4f(@Nonnull CoreVector4f src) {
    set(src);
  }

  /**
   * Constructs a new vector populated with the specified values.
   */
  public CoreVector4f(float x, float y, float z, float w) {
    set(x, y, z, w);
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }

  public float getW() {
    return w;
  }

  public void setX(final float x) {
    this.x = x;
  }

  public void setY(final float y) {
    this.y = y;
  }

  public void setZ(final float z) {
    this.z = z;
  }

  public void setW(final float w) {
    this.w = w;
  }

  public void set(final float x, final float y) {
    this.x = x;
    this.y = y;
  }

  public void set(final float x, final float y, final float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void set(final float x, final float y, final float z, final float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  /**
   * Populates this vector with the values from the specified source vector.
   *
   * @return This vector.
   */
  @Nonnull
  public CoreVector4f set(@Nonnull final CoreVector4f src) {
    x = src.getX();
    y = src.getY();
    z = src.getZ();
    w = src.getW();

    return this;
  }

  /**
   * Calculates and returns the length squared of this vector.
   */
  public float lengthSquared() {
    return x * x + y * y + z * z + w * w;
  }

  /**
   * Calculates and returns the length of this vector.
   */
  public float length() {
    return (float) Math.sqrt(lengthSquared());
  }

  /**
   * Translate this vector by the specified x, y, z, & w values.
   *
   * @return This vector.
   */
  @Nonnull
  public CoreVector4f translate(final float x, final float y, final float z, final float w) {
    this.x += x;
    this.y += y;
    this.z += z;
    this.w += w;

    return this;
  }

  /**
   * Adds the two specified vectors together and stores the result in the specified result vector.
   *
   * @param left  The left source vector to add.
   * @param right The right source vector to add.
   * @param result The vector containing the result of the addition, or null to create a new vector.
   *
   * @return The result vector.
   */
  @Nonnull
  public static CoreVector4f add(
          @Nonnull final CoreVector4f left,
          @Nonnull final CoreVector4f right,
          @Nullable final CoreVector4f result) {
    if (result == null) {
      return new CoreVector4f(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
    }
    else {
      result.set(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
      return result;
    }
  }

  /**
   * Subtracts the specified right vector from the specified left vector and stores the result in the specified result
   * vector.
   *
   * @param left  The left source vector to subtract.
   * @param right The right source vector to subtract.
   * @param result The vector containing the result of the subtraction, or null to create a new vector.
   *
   * @return The result vector.
   */
  @Nonnull
  public static CoreVector4f subtract(
          @Nonnull final CoreVector4f left,
          @Nonnull final CoreVector4f right,
          @Nullable final CoreVector4f result) {
    if (result == null) {
      return new CoreVector4f(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
    }
    else {
      result.set(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
      return result;
    }
  }

  /**
   * Negates this vector
   *
   * @return This vector.
   */
  @Nonnull
  public CoreVector4f negate() {
    x = -x;
    y = -y;
    z = -z;
    w = -w;

    return this;
  }

  /**
   * Negates this vector and stores the result in the specified result vector.
   *
   * @param result The vector containing the result of the negation, or null to create a new vector.
   *
   * @return The result vector.
   */
  @Nonnull
  public CoreVector4f negate(@Nullable CoreVector4f result) {
    if (result == null) {
      result = new CoreVector4f();
    }

    result.x = -x;
    result.y = -y;
    result.z = -z;
    result.w = -w;

    return result;
  }

  /**
   * Normalizes this vector and stores the result in the specified result vector.
   *
   * @param result The vector containing the result of the normalization, or null to create a new vector.
   *
   * @return The result vector.
   */
  @Nonnull
  public CoreVector4f normalize(@Nullable CoreVector4f result) {
    float l = length();

    if (result == null) {
      result = new CoreVector4f(x / l, y / l, z / l, w / l);
    } else {
      result.set(x / l, y / l, z / l, w / l);
    }

    return result;
  }

  /**
   * Calculates the dot product of the specified left and right vectors. The dot product of two vectors, v1 & v2, is
   * calculated as: v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w.
   *
   * @param left  The left source vector.
   * @param right The right source vector.
   *
   * @return The result of left dot right.
   */
  public static float dot(@Nonnull final CoreVector4f left, @Nonnull final CoreVector4f right) {
    return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
  }

  /**
   * Calculates the angle between two vectors, in radians.
   *
   * @param a The first vector.
   * @param b The second vector.
   *
   * @return The angle between the two vectors, in radians.
   */
  public static float angle(@Nonnull final CoreVector4f a, @Nonnull final CoreVector4f b) {
    float dls = dot(a, b) / (a.length() * b.length());

    if (dls < -1f) {
      dls = -1f;
    } else if (dls > 1.0f) {
      dls = 1.0f;
    }

    return (float) Math.acos(dls);
  }

  /**
   * Populates this vector from the specified {@link java.nio.FloatBuffer}.
   *
   * @return This vector.
   */
  @Nonnull
  public CoreVector4f load(@Nonnull final FloatBuffer buffer) {
    x = buffer.get();
    y = buffer.get();
    z = buffer.get();
    w = buffer.get();

    return this;
  }

  /**
   * Scales this vector by the specified scale.
   *
   * @return This vector.
   */
  @Nonnull
  public CoreVector4f scale(final float scale) {
    x *= scale;
    y *= scale;
    z *= scale;
    w *= scale;

    return this;
  }

  /**
   * Stores the x, y, z, & w values of this vector in the specified {@link java.nio.FloatBuffer}.
   *
   * @return This vector.
   */
  @Nonnull
  public CoreVector4f store(@Nonnull final FloatBuffer buffer) {
    buffer.put(x);
    buffer.put(y);
    buffer.put(z);
    buffer.put(w);

    return this;
  }

  @Override
  @Nonnull
  public String toString() {
    return "Vector4f: " + x + " " + y + " " + z + " " + w;
  }
}
