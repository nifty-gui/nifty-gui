package de.lessvoid.nifty.java2d.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mouse.MouseInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;

public class InputSystemAwtImpl implements InputSystem, MouseMotionListener,
		MouseListener, KeyListener {

	private ConcurrentLinkedQueue<MouseInputEvent> mouseEvents = new ConcurrentLinkedQueue<MouseInputEvent>();

	private ConcurrentLinkedQueue<KeyboardInputEvent> keyboardEvents = new ConcurrentLinkedQueue<KeyboardInputEvent>();

	@Override
	public void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
		MouseInputEvent mouseEvent = mouseEvents.poll();
		while (mouseEvent != null) {
			inputEventConsumer.processMouseEvent(mouseEvent);
			mouseEvent = mouseEvents.poll();
		}

		KeyboardInputEvent keyEvent = keyboardEvents.poll();
		while (keyEvent != null) {
			inputEventConsumer.processKeyboardEvent(keyEvent);
			keyEvent = keyboardEvents.poll();
		}
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {

	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent
				.getY(), mouseEvent.getButton() == MouseEvent.BUTTON1));
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {

	}

	@Override
	public void mouseEntered(MouseEvent mouseEvent) {

	}

	@Override
	public void mouseExited(MouseEvent mouseEvent) {

	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {

	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		mouseEvents.add(new MouseInputEvent(mouseEvent.getX(), mouseEvent
				.getY(), mouseEvent.getButton() == MouseEvent.BUTTON1));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int convertToNiftyKeyCode = keyCodeConverter.convertToNiftyKeyCode(e);
		keyboardEvents.add(new KeyboardInputEvent(convertToNiftyKeyCode, e
				.getKeyChar(), true, e.isShiftDown(), e.isControlDown()));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int convertToNiftyKeyCode = keyCodeConverter.convertToNiftyKeyCode(e);
		keyboardEvents.add(new KeyboardInputEvent(convertToNiftyKeyCode, e
				.getKeyChar(), false, e.isShiftDown(), e.isControlDown()));
	}

	class AwtToNiftyKeyCodeConverter {

		Map<Integer, Integer> keyCodeConversionMap = new HashMap<Integer, Integer>() {
			private static final long serialVersionUID = 858494893395813981L;
			{
				put(KeyEvent.VK_LEFT, KeyboardInputEvent.KEY_LEFT);
				put(KeyEvent.VK_RIGHT, KeyboardInputEvent.KEY_RIGHT);
				put(KeyEvent.VK_UP, KeyboardInputEvent.KEY_UP);
				put(KeyEvent.VK_DOWN, KeyboardInputEvent.KEY_DOWN);
				put(KeyEvent.VK_BACK_SPACE, KeyboardInputEvent.KEY_BACK);
				put(KeyEvent.VK_DELETE, KeyboardInputEvent.KEY_DELETE);
				put(KeyEvent.VK_ESCAPE, KeyboardInputEvent.KEY_ESCAPE);
				put(KeyEvent.VK_SHIFT, KeyboardInputEvent.KEY_RSHIFT);
				put(KeyEvent.VK_ENTER, KeyboardInputEvent.KEY_RETURN);
				put(KeyEvent.VK_TAB, KeyboardInputEvent.KEY_TAB);
				put(KeyEvent.VK_F1, KeyboardInputEvent.KEY_F1);
				put(KeyEvent.VK_F2, KeyboardInputEvent.KEY_F2);
				put(KeyEvent.VK_F3, KeyboardInputEvent.KEY_F3);
				put(KeyEvent.VK_F4, KeyboardInputEvent.KEY_F4);
				put(KeyEvent.VK_F5, KeyboardInputEvent.KEY_F5);
				put(KeyEvent.VK_F6, KeyboardInputEvent.KEY_F6);
				put(KeyEvent.VK_F7, KeyboardInputEvent.KEY_F7);
				put(KeyEvent.VK_F8, KeyboardInputEvent.KEY_F8);
				put(KeyEvent.VK_F9, KeyboardInputEvent.KEY_F9);
				put(KeyEvent.VK_F10, KeyboardInputEvent.KEY_F10);
				put(KeyEvent.VK_F11, KeyboardInputEvent.KEY_F11);
				put(KeyEvent.VK_F12, KeyboardInputEvent.KEY_F12);
			}
		};

		public int convertToNiftyKeyCode(KeyEvent e) {
			if (keyCodeConversionMap.containsKey(e.getKeyCode()))
				return keyCodeConversionMap.get(e.getKeyCode());
			return e.getKeyCode();
		}

	}

	AwtToNiftyKeyCodeConverter keyCodeConverter = new AwtToNiftyKeyCodeConverter();

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}