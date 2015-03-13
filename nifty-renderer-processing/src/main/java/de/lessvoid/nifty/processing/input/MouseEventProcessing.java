package de.lessvoid.nifty.processing.input;

import processing.event.*;

/**
 * A container class for Processing's MouseEvent, containing the buttonState (true/false).
 * @author Xuanming
 */
public class MouseEventProcessing {
	
	  private final MouseEvent e;
	  private final boolean buttonState;
	  
	  /**
	   * Create an instance of MouseEventProcessing.
	   * @param event The instance of Processing's MouseEvent class for storage.
	   * @param state The buttonState to be recorded in this class.
	   */
	  MouseEventProcessing(final MouseEvent event, final boolean state) {
		  this.e = event;
		  this.buttonState = state;
	  }
	  
	  /**
	   * Get Y-coordinate of mouse cursor.
	   * @return Mouse Y-coordinate.
	   */
	  public int getY() {
		  return e.getY();
	  }
	  
	  /**
	   * Get X-coordinate of mouse cursor.
	   * @return Mouse X-coordinate.
	   */
	  public int getX() {
		  return e.getX();
	  }
	  
	  /**
	   * Get the mouse button currently being interacted with.
	   * @return -1, no button. 0, left button. 2, middle button. 1, right button.
	   */
	  public int getButton() {
		  return translateMouseButtonCode(e.getButton());
	  }
	  
	  /**
	   * Current state of mouse wheel.
	   * @return -1 if rolled downwards. 1 if rolled upwards.
	   */
	  public int getWheel() {
		  if (this.getButton() == -1) {
			  return (int)e.getCount();
		  } else {
			  return 0;
		  }
	  }
	  
	  /**
	   * Get button state of interaction.
	   * @return True, mouse button down. False, mouse button up.
	   */
	  public boolean getState() {
		  return buttonState;
	  }
	  
	  /**
	   * Translates Processing's mouse button code into one recognizable by Nifty.
	   * @param code 37, left button. 3, middle button. 39, right button.
	   * @return -1, no button. 0, left button. 2, middle button. 1, right button.
	   */
	  private int translateMouseButtonCode(final int code){
		  
		  int returnInt = -1;
		  
		  switch (code) {    
		    /* Left-click. */   case 37: returnInt = 0; break;      
		    /* Middle-click. */ case 3: returnInt = 2; break;      
		    /* Right-click. */  case 39: returnInt = 1; break;
		  }
		  
		  return returnInt;
	  }
}