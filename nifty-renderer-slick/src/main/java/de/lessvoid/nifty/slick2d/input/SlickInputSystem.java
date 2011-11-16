package de.lessvoid.nifty.slick2d.input;

import org.newdawn.slick.InputListener;

import de.lessvoid.nifty.spi.input.InputSystem;

/**
 * This interface merges the Nifty and the Slick Input interfaces as needed
 * so the implementing classes are able to work properly with the Nifty-Slick
 * binding.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public interface SlickInputSystem extends InputSystem, InputListener {
    
}
