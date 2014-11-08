package de.lessvoid.nifty.api;

/**
 * Composite operations Nifty supports.
 * @author void
 */
public enum NiftyCompositeOperation {
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
  XOR
}
