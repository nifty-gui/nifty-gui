package de.lessvoid.nifty.slick2d.input;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.InputAdapter;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.slick2d.input.events.InputEvent;
import de.lessvoid.nifty.slick2d.input.events.KeyboardEventPressed;
import de.lessvoid.nifty.slick2d.input.events.KeyboardEventReleased;
import de.lessvoid.nifty.slick2d.input.events.MouseEventClicked;
import de.lessvoid.nifty.slick2d.input.events.MouseEventDragged;
import de.lessvoid.nifty.slick2d.input.events.MouseEventMoved;
import de.lessvoid.nifty.slick2d.input.events.MouseEventPressed;
import de.lessvoid.nifty.slick2d.input.events.MouseEventReleased;
import de.lessvoid.nifty.slick2d.input.events.MouseEventWheelMoved;
import de.lessvoid.nifty.spi.input.InputSystem;

/**
 * This is the abstract Input System implementation to connect the Input of
 * Slick and Nifty.
 * 
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractSlickInputSystem extends InputAdapter implements
    InputSystem {
    /**
     * The list of input events that was registered but yet not processed.
     */
    private final List<InputEvent> inputEventList;

    /**
     * The input system that feeds this input system with data.
     */
    private Input input;

    /**
     * The list of buttons that got pressed and are still pressed.
     */
    private final List<Integer> buttonPressedStack;

    protected AbstractSlickInputSystem() {
        inputEventList = new ArrayList<InputEvent>();
        buttonPressedStack = new LinkedList<Integer>();
    }

    @Override
    public final void mouseWheelMoved(final int change) {
        inputEventList.add(new MouseEventWheelMoved(input.getMouseX(), input
            .getMouseY(), change / 120));
    }

    @Override
    public final void mouseClicked(final int button, final int x, final int y,
        final int clickCount) {
        inputEventList.add(new MouseEventClicked(x, y, button, clickCount));
    }

    @Override
    public final void mousePressed(final int button, final int x, final int y) {
        inputEventList.add(new MouseEventPressed(x, y, button));
        buttonPressedStack.add(Integer.valueOf(button));
    }

    @Override
    public final void mouseReleased(final int button, final int x, final int y) {
        inputEventList.add(new MouseEventReleased(x, y, button));
        buttonPressedStack.remove(Integer.valueOf(button));
    }

    @Override
    public final void mouseMoved(final int oldx, final int oldy,
        final int newx, final int newy) {
        inputEventList.add(new MouseEventMoved(oldx, oldy, newx, newy));
    }

    @Override
    public final void mouseDragged(final int oldx, final int oldy,
        final int newx, final int newy) {
        final int lastButton =
            buttonPressedStack.get(buttonPressedStack.size()).intValue();
        inputEventList.add(new MouseEventDragged(lastButton, oldx, oldy, newx,
            newy));
    }

    @Override
    public final void setInput(final Input input) {
        this.input = input;
    }

    @Override
    public final boolean isAcceptingInput() {
        return true;
    }

    @Override
    public final void inputEnded() {
    }

    @Override
    public final void inputStarted() {
    }

    private final boolean isShiftDown() {
        return input.isKeyDown(Input.KEY_LSHIFT)
            || input.isKeyDown(Input.KEY_RSHIFT);
    }

    private final boolean isControlDown() {
        return input.isKeyDown(Input.KEY_LCONTROL)
            || input.isKeyDown(Input.KEY_RCONTROL);
    }

    @Override
    public final void keyPressed(final int key, final char c) {
        inputEventList.add(new KeyboardEventPressed(key, c, isShiftDown(),
            isControlDown()));
    }

    @Override
    public final void keyReleased(final int key, final char c) {
        inputEventList.add(new KeyboardEventReleased(key, c, isShiftDown(),
            isControlDown()));
    }

    @Override
    public final void forwardEvents(final NiftyInputConsumer inputEventConsumer) {
        InputEvent currentEvent;
        while (!inputEventList.isEmpty()) {
            currentEvent = inputEventList.remove(0);
            if (!currentEvent.sendToNifty(inputEventConsumer)) {
                handleInputEvent(currentEvent);
            }
        }
    }

    /**
     * This function is called in case a input event was not handled by the
     * Nifty event consumer of the Nifty GUI.
     * 
     * @param event the event that needs to be handled
     */
    protected abstract void handleInputEvent(InputEvent event);

    /**
     * Set the current location of the mouse to a new spot.
     */
    public final void setMousePosition(final int x, final int y) {
        Mouse.setCursorPosition(x, y);
    }
}
