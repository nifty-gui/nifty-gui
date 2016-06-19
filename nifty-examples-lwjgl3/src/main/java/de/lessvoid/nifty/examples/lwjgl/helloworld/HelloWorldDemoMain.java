package de.lessvoid.nifty.examples.lwjgl.helloworld;

import de.lessvoid.nifty.examples.helloworld.HelloWorldStartScreen;
import de.lessvoid.nifty.examples.lwjgl.NiftyExampleLoaderLWJGL3;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public class HelloWorldDemoMain {
  public static void main(final String[] args) {
    NiftyExampleLoaderLWJGL3.run(new HelloWorldStartScreen());
  }
}
