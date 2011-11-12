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
public class TabsBuilder extends ControlBuilder {
    
  public TabsBuilder() {
    super("nifty-tabs");
  }

  public TabsBuilder(final String id) {
    super(id, "nifty-tabs");
  }
  
  public void buttonWidth(final String buttonWidth) {
      set("buttonWidth", buttonWidth);
  }
  
  public void buttonHeight(final String buttonHeight) {
      set("buttonHeight", buttonHeight);
  }
    
}
