/* 
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.renderer.jogl.math;

import java.io.Serializable;
import java.nio.FloatBuffer;

/**
 *
 * Holds a 4-tuple vector.
 * 
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision: 2983 $
 * $Id: Vector4f.java 2983 2008-04-07 18:36:09Z matzon $
 *
 * @author void256 - removed the interfaces this class implements and remove the base class to make that a simple
 *                   and plain self-contained class
 */

public class Vec4 implements Serializable {

	private static final long serialVersionUID = 1L;

	public float x, y, z, w;

	/**
	 * Constructor for Vector4f.
	 */
	public Vec4() {
		x = 0.f;
		y = 0.f;
		z = 0.f;
		w = 1.f;
	}

	/**
	 * Constructor
	 */
	public Vec4(Vec4 src) {
		set(src);
	}

	/**
	 * Constructor
	 */
	public Vec4(float x, float y, float z, float w) {
		set(x, y, z, w);
	}

	/* (non-Javadoc)
	 * @see org.lwjgl.util.vector.WritableVector2f#set(float, float)
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/* (non-Javadoc)
	 * @see org.lwjgl.util.vector.WritableVector3f#set(float, float, float)
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/* (non-Javadoc)
	 * @see org.lwjgl.util.vector.WritableVector4f#set(float, float, float, float)
	 */
	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Load from another Vector4f
	 * @param src The source vector
	 * @return this
	 */
	public Vec4 set(Vec4 src) {
		x = src.getX();
		y = src.getY();
		z = src.getZ();
		w = src.getW();
		return this;
	}

	/**
	 * @return the length squared of the vector
	 */
	public float lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}

	 public final float length() {
	    return (float) Math.sqrt(lengthSquared());
	  }

	/**
	 * Translate a vector
	 * @param x The translation in x
	 * @param y the translation in y
	 * @return this
	 */
	public Vec4 translate(float x, float y, float z, float w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}

	/**
	 * Add a vector to another vector and place the result in a destination
	 * vector.
	 * @param left The LHS vector
	 * @param right The RHS vector
	 * @param dest The destination vector, or null if a new vector is to be created
	 * @return the sum of left and right in dest
	 */
	public static Vec4 add(Vec4 left, Vec4 right, Vec4 dest) {
		if (dest == null)
			return new Vec4(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
		else {
			dest.set(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
			return dest;
		}
	}

	/**
	 * Subtract a vector from another vector and place the result in a destination
	 * vector.
	 * @param left The LHS vector
	 * @param right The RHS vector
	 * @param dest The destination vector, or null if a new vector is to be created
	 * @return left minus right in dest
	 */
	public static Vec4 sub(Vec4 left, Vec4 right, Vec4 dest) {
		if (dest == null)
			return new Vec4(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
		else {
			dest.set(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
			return dest;
		}
	}


	/**
	 * Negate a vector
	 * @return this
	 */
	public Vec4 negate() {
		x = -x;
		y = -y;
		z = -z;
		w = -w;
		return this;
	}

	/**
	 * Negate a vector and place the result in a destination vector.
	 * @param dest The destination vector or null if a new vector is to be created
	 * @return the negated vector
	 */
	public Vec4 negate(Vec4 dest) {
		if (dest == null)
			dest = new Vec4();
		dest.x = -x;
		dest.y = -y;
		dest.z = -z;
		dest.w = -w;
		return dest;
	}


	/**
	 * Normalise this vector and place the result in another vector.
	 * @param dest The destination vector, or null if a new vector is to be created
	 * @return the normalised vector
	 */
	public Vec4 normalise(Vec4 dest) {
		float l = length();

		if (dest == null)
			dest = new Vec4(x / l, y / l, z / l, w / l);
		else
			dest.set(x / l, y / l, z / l, w / l);

		return dest;
	}

	/**
	 * The dot product of two vectors is calculated as
	 * v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w
	 * @param left The LHS vector
	 * @param right The RHS vector
	 * @return left dot right
	 */
	public static float dot(Vec4 left, Vec4 right) {
		return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
	}

	/**
	 * Calculate the angle between two vectors, in radians
	 * @param a A vector
	 * @param b The other vector
	 * @return the angle between the two vectors, in radians
	 */
	public static float angle(Vec4 a, Vec4 b) {
		float dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1f)
			dls = -1f;
		else if (dls > 1.0f)
			dls = 1.0f;
		return (float)Math.acos(dls);
	}

	/* (non-Javadoc)
	 * @see org.lwjgl.vector.Vector#load(FloatBuffer)
	 */
	public Vec4 load(FloatBuffer buf) {
		x = buf.get();
		y = buf.get();
		z = buf.get();
		w = buf.get();
		return this;
	}

	/* (non-Javadoc)
	 * @see org.lwjgl.vector.Vector#scale(float)
	 */
	public Vec4 scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		w *= scale;
		return this;
	}

	/* (non-Javadoc)
	 * @see org.lwjgl.vector.Vector#store(FloatBuffer)
	 */
	public Vec4 store(FloatBuffer buf) {

		buf.put(x);
		buf.put(y);
		buf.put(z);
		buf.put(w);

		return this;
	}

	public String toString() {
		return "Vector4f: " + x + " " + y + " " + z + " " + w;
	}

	/**
	 * @return x
	 */
	public final float getX() {
		return x;
	}

	/**
	 * @return y
	 */
	public final float getY() {
		return y;
	}

	/**
	 * Set X
	 * @param x
	 */
	public final void setX(float x) {
		this.x = x;
	}

	/**
	 * Set Y
	 * @param y
	 */
	public final void setY(float y) {
		this.y = y;
	}

	/**
	 * Set Z
	 * @param z
	 */
	public void setZ(float z) {
		this.z = z;
	}


	/* (Overrides)
	 * @see org.lwjgl.vector.ReadableVector3f#getZ()
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Set W
	 * @param w
	 */
	public void setW(float w) {
		this.w = w;
	}

	/* (Overrides)
	 * @see org.lwjgl.vector.ReadableVector3f#getZ()
	 */
	public float getW() {
		return w;
	}


}
