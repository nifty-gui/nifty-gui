package de.lessvoid.nifty.slick2d;

/**
 * This interface is applied to classes that use the Nifty-Carrier.
 *
 * @author Martin Karing &gt;nitram@illarion.org&lt;
 */
public interface NiftyCarrierUser {
  /**
   * Set the carrier that is supposed to be used by this carrier user.
   *
   * @param carrier the carrier to use
   */
  void setCarrier(NiftyCarrier carrier);
}
