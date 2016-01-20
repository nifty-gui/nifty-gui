package de.lessvoid.nifty.processing.input;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import de.lessvoid.nifty.input.keyboard.*;
import processing.event.*;
import processing.core.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Implementation of InputSystem to handle mouse and key events passed from Processing.
 * @author Xuanming
 */
public class InputSystemProcessing implements InputSystem {
	
	private final PApplet app;
	private final ConcurrentLinkedQueue<MouseEventProcessing> mouseEvents = 
			new ConcurrentLinkedQueue<MouseEventProcessing>();
	private final ConcurrentLinkedQueue<KeyboardInputEvent> keyEvents = 
			new ConcurrentLinkedQueue<KeyboardInputEvent>();
	private final AwtToNiftyKeyCodeConverter keyConverter = 
			new AwtToNiftyKeyCodeConverter();
	private final int scale;
	
	/**
	 * Create an instance of InputSystemProcessing.
	 * @param app PApplet instance Processing is running in.
	 */
	public InputSystemProcessing(final PApplet app){
		this(app, 1);
	}
	
	/**
	 * Create an instance of InputSystemProcessing (verbose version).
	 * @param app PApplet instance Processing is running in.
	 * @param Scale factor by which UI is scaled (mouse position is scaled accordingly).
	 */
	public InputSystemProcessing(final PApplet app, int Scale){
		this.scale = Scale;
		this.app = app;
		
		// Register methods with Processing.
		// This will enable Processing to call registered functions in this class when events happen.
		app.registerMethod("mouseEvent", this);
		app.registerMethod("keyEvent", this);
	}

	@Override
	public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
	}

	@Override
	public void forwardEvents(NiftyInputConsumer inputEventConsumer) {
	    MouseEventProcessing mouseEvent = mouseEvents.poll();
	    while (mouseEvent != null) {
	      inputEventConsumer.processMouseEvent(mouseEvent.getX()/scale, mouseEvent.getY()/scale, mouseEvent.getWheel() * (-1), mouseEvent.getButton(), mouseEvent.getState());
	      mouseEvent = mouseEvents.poll();
	    }

	    KeyboardInputEvent keyEvent = keyEvents.poll();
	    while (keyEvent != null) {
	      inputEventConsumer.processKeyboardEvent(keyEvent);
	      keyEvent = keyEvents.poll();
	    }
	}

	@Override
	public void setMousePosition(int x, int y) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Handle MouseEvents from Processing.
	 * @param event processing.event.MouseEvent class passed from Processing.
	 */
	public void mouseEvent(MouseEvent event) {
		
		// Figure out what MouseEvent just happened.
		switch (event.getAction()){
			case MouseEvent.PRESS:
				mouseEvents.add(new MouseEventProcessing(event, true));
				break;
				
			case MouseEvent.RELEASE:
				mouseEvents.add(new MouseEventProcessing(event, false));
				break;
				
			case MouseEvent.WHEEL:
				mouseEvents.add(new MouseEventProcessing(event, false));
				break;
				
			case MouseEvent.MOVE:
				mouseEvents.add(new MouseEventProcessing(event, false));
				break;
				
			case MouseEvent.DRAG:
				mouseEvents.add(new MouseEventProcessing(event, true));
				break;
		}
	}
	
	/**
	 * Handle KeyEvents from Processing.
	 * @param event processing.event.KeyEvent class passed from Processing.
	 */
	public void keyEvent(KeyEvent event) {
	
		// Figure out what KeyEvent just happened.
		switch(event.getAction()){
			case KeyEvent.PRESS:
				handleKeyEvent(event, app.key, true);
				break;
				
			case KeyEvent.RELEASE:
				handleKeyEvent(event, app.key, false);
				break;
		}
	}

	/**
	 * Translates processing.event.KeyEvent class into Nifty's KeyboardInputEvent and adds it to queue to be consumed.
	 * @param event processing.event.KeyEvent passed from Processing.
	 * @param key The character that has been pressed/released.
	 * @param isKeyDown TRUE if key is pressed, FALSE if key is released.
	 */
	private void handleKeyEvent(KeyEvent event, char key, boolean isKeyDown) {
		int newKeyCode = keyConverter.convertToNiftyKeyCode(event.getKeyCode(), 2);
		keyEvents.add(new KeyboardInputEvent(newKeyCode, key, isKeyDown, event.isShiftDown(), event.isControlDown()));
	}
}
