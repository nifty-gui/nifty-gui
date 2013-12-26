/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls.treebox.builder;

import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;

import javax.annotation.Nonnull;

/**
 * @author ractoc
 */
public class TreeBoxBuilder extends ListBoxBuilder {

  public TreeBoxBuilder() {
    this(NiftyIdCreator.generate());
  }

  public TreeBoxBuilder(@Nonnull final String id) {
    super(id, "treeBox");
  }

}
