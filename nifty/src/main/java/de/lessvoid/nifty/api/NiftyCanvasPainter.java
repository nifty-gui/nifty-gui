package de.lessvoid.nifty.api;

/**
 * The NiftyCanvasPainter allows you to provide the content of a NiftyNode. When it is time to render the content the
 * paint method will be called by Nifty with a NiftyCanvas instance. You then use methods on the NiftyCanvas to render
 * whatever content you like.
 *
 * @author void
 */
public interface NiftyCanvasPainter {

  /**
   * Paint into the given NiftyCanvas for the given NiftyNode.
   * @param node the NiftyNode this NiftyCanvasPainter is now painting
   * @param canvas the NiftyCanvas to paint into
   */
  void paint(NiftyNode node, NiftyCanvas canvas);
}
