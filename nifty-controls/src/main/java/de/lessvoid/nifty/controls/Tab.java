/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

/**
 *
 * @author ractoc
 */
public interface Tab extends NiftyControl {
    
    /**
     * Set the caption for the tab.
     * @param caption The caption for the tab.
     */
    public void setCaption(String caption);
    
    /**
     * Get the caption for the tab.
     * @return The caption for the tab.
     */
    public String getCaption();
    
}
