package de.lessvoid.nifty.examples.lwjgl3.helloworld;

import de.lessvoid.nifty.examples.helloworld.HelloWorldStartScreen;
import de.lessvoid.nifty.examples.lwjgl3.NiftyExampleLoaderLWJGL3;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class HelloWorldDemoMain {
  public static void main(final String[] args) {
    NiftyExampleLoaderLWJGL3.run(new HelloWorldStartScreen());
  }
}
