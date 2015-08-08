/*
 * Copyright (c) 2015, Nifty GUI Community 
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
package de.lessvoid.nifty.api.types;

/**
 * Composite operations Nifty supports.
 * @author void
 */
public enum NiftyCompositeOperation {
  // Clean
  Clear,

  // Only use destination color
  Destination,

  // Default. Displays the source image over the destination image
  SourceOver,

  // Displays the source image on top of the destination image. The part of the source image that
  // is outside the destination image is not shown
  SourceAtop,

  // Displays the source image in to the destination image. Only the part of the source image that
  // is INSIDE the destination image is shown, and the destination image is transparent
  SourceIn,

  // Displays the source image out of the destination image. Only the part of the source image that
  // is OUTSIDE the destination image is shown, and the destination image is transparent
  SourceOut,

  // Displays the destination image over the source image
  DestinationOver,

  // Displays the destination image on top of the source image. The part of the destination image that
  // is outside the source image is not shown
  DestinationAtop,

  // Displays the destination image in to the source image. Only the part of the destination image that
  // is INSIDE the source image is shown, and the source image is transparent
  DestinationIn,

  // Displays the destination image out of the source image. Only the part of the destination image that
  // is OUTSIDE the source image is shown, and the source image is transparent
  DestinationOut,

  // Displays the source image + the destination image
  Lighter,

  // Displays the source image. The destination image is ignored
  Copy,

  // The source image is combined by using an exclusive OR with the destination imaga
  XOR,

  // additional blending modes not part of HTML5 canvas or the original porter/duff paper

  // completely disable blending
  Off,

  // Use the maximum color and alpha values
  Max
}
