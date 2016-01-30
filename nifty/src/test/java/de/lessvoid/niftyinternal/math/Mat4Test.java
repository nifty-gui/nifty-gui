/*
 * Copyright (c) 2016, Nifty GUI Community
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

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by void on 16.01.16.
 */
public class Mat4Test {

  @Test
  public void testSetTranslate() {
    Mat4 mat4 = Mat4.createIdentity().setTranslate(1.0f, 2.0f, 3.0f);
    assertEquals(
        "1.0 0.0 0.0 1.0\n" +
        "0.0 1.0 0.0 2.0\n" +
        "0.0 0.0 1.0 3.0\n" +
        "0.0 0.0 0.0 1.0\n",
        mat4.toString());
  }

  @Test
  public void testSetScale() {
    Mat4 mat4 = Mat4.createIdentity().setScale(1.0f, 2.0f, 3.0f);
    assertEquals(
        "1.0 0.0 0.0 0.0\n" +
        "0.0 2.0 0.0 0.0\n" +
        "0.0 0.0 3.0 0.0\n" +
        "0.0 0.0 0.0 1.0\n",
        mat4.toString());
  }

  @Test
  public void testSetRotate() {
    Mat4 mat4 = Mat4.createIdentity().setRotate(10.f, 1.0f, 2.0f, 3.0f);
    assertEquals(
        "1.0 -0.49056 0.39287317 0.0\n" +
        "0.5513291 1.0455768 -0.08249456 0.0\n" +
        "-0.30171955 0.2648018 1.1215382 0.0\n" +
        "0.0 0.0 0.0 1.0\n",
        mat4.toString());
  }
}
