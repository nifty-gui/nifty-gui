package de.lessvoid.nifty.examples.helloworld;

import de.lessvoid.nifty.api.Nifty;
import de.lessvoid.nifty.api.NiftyNode;

/**
 * Nifty Hello World Example.
 * @author void
 */
public class NiftyHelloWorld  {
  private final Nifty nifty;

  public NiftyHelloWorld(final Nifty niftyParam) {
    nifty = niftyParam;

    NiftyNode rootNode = nifty.createNode(nifty.getScreenWidth(), nifty.getScreenHeight());
    nifty.setRootNode(rootNode);
  }
}
