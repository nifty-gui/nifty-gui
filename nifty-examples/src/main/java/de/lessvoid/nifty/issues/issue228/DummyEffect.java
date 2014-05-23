/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.issues.issue228;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectImpl;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.effects.Falloff;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.tools.Color;

/**
 *
 * @author cris
 */
public class DummyEffect implements EffectImpl {

    public void activate(Nifty nifty, Element element, EffectProperties parameter) {
        
    }

    public void execute(Element element, float effectTime, Falloff falloff, NiftyRenderEngine r) {
        
       
        r.setColor(Color.WHITE);
       
       
    }

    public void deactivate() {
       
    }
    
}
