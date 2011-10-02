/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.tabs.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

/**
 *
 * @author ractoc
 */
public class TabBuilder extends ControlBuilder {
    
  public TabBuilder(String caption) {
    super("nifty-tab");
    caption(caption);
  }

  public TabBuilder(final String id, String caption) {
    super(id, "nifty-tab");
    caption(caption);
  }

  public void caption(final String caption) {
    set("caption", caption);
  }
    
}
