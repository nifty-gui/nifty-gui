package de.lessvoid.nifty.slick2d;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.slick2d.input.PlainSlickInputSystem;
import de.lessvoid.nifty.slick2d.render.SlickRenderDevice;
import de.lessvoid.nifty.slick2d.sound.SlickSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

public abstract class NiftyGameState extends NiftyOverlayBasicGameState {
    /**
     * The screen that is called upon entering the game state.
     */
    private final String startScreen;

    /**
     * Create this game state and set the screen that is called upon entering
     * this state.
     * 
     * @param niftyStartScreen the name of the screen Nifty is supposed to goto
     *            once the game state is entered
     */
    protected NiftyGameState(final String niftyStartScreen) {
        startScreen = niftyStartScreen;
    }

    /**
     * Create this game state.
     */
    protected NiftyGameState() {
        this("start");
    }

    /**
     * Updating the game is not needed in this implementation as only the GUI is
     * displayed.
     */
    @Override
    protected final void updateGame(final GameContainer container,
        final StateBasedGame game, final int delta) throws SlickException {
        // nothing to do
    }

    /**
     * Rendering the GUI only requires that the display is cleared before
     * rendering the screen.
     */
    @Override
    protected final void renderGame(final GameContainer container,
        final StateBasedGame game, final Graphics g) throws SlickException {
        g.clear();
    }

    /**
     * When initializing the game its only needed to prepare the GUI for
     * display.
     */
    @Override
    protected final void initGameAndGUI(GameContainer container,
        final StateBasedGame game) throws SlickException {
        initNifty(container, new SlickRenderDevice(container),
            new SlickSoundDevice(), new PlainSlickInputSystem(),
            new TimeProvider());
        getNifty().gotoScreen(startScreen);
    }

    /**
     * Enter this game state.
     */
    public void enter(final GameContainer container,
        final StateBasedGame game) throws SlickException {
        if (startScreen != null) {
            getNifty().gotoScreen(startScreen);
        }
    }
}
