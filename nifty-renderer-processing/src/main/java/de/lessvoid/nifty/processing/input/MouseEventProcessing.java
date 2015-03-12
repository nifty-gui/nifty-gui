package de.lessvoid.nifty.processing.input;

import processing.event.*;

public class MouseEventProcessing {
	
	  private final MouseEvent e;
	  private final boolean buttonState;
	  
	  MouseEventProcessing(final MouseEvent event, final boolean state) {
		  this.e = event;
		  this.buttonState = state;
	  }
	  
	  public int getY() {
		  return e.getY();
	  }
	  
	  public int getX() {
		  return e.getX();
	  }
	  
	  public int getButton() {
		  return translateMouseButtonCode(e.getButton());
	  }
	  
	  public int getWheel() {
		  if (this.getButton() == -1) {
			  return (int)e.getCount();
		  } else {
			  return 0;
		  }
	  }
	  
	  public boolean getState() {
		  return buttonState;
	  }
	  
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