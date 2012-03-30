package de.lessvoid.nifty.examples.slick.niftyoverlay;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.slick.NiftyOverlayGameState;


/**
 * This example shows the use of the NiftyOverlayGameState to build
 * a Slick game using multiple GameStates, each containing its own
 * Nifty UI overlay consisting of one more selectable screens that keep
 * their state between GameState switches.
 * 
 * Input Events are caught in the overlay first, and if not used
 * passed through to the actual game.
 * To use this, the NiftyOverlayGameState event handlers have to be used.
 * Alternatively the unfiltered Slick input handlers are also still
 * available although these will contain the events used by Nifty. 
 * 
 * @author Durandal
 * @see mail to: nifty(at)durandal.nl
 *
 */
public class MultiStateOverlayExample extends StateBasedGame {

	TestStateOne state1;
	TestStateTwo state2;	
	
	public MultiStateOverlayExample(final String title) {
		super(title);
	}
	
	// TODO what about adding more buttons for the nifty mouse events since we will be using those
	// in the game ?
	
	// TODO bug: getting a WARNING: class [nl.durandal.nifty.TestStateOne$Controller] could not be instanziated
	// while Nifty should not attempt to instantiate it in the first place as it has been given a controller instance
	
	

	// state 'manager'. Switches GameStates
	public void nextState() {
		switch (this.getCurrentStateID()) {
		case 1:
			this.enterState(2);
			break;
		case 2:
			this.enterState(1);
			break;
		}
	}

	// Switches screens within the active GameState
	public void nextScreen() {
		NiftyOverlayGameState currentState = (NiftyOverlayGameState)this.getCurrentState();  
		String currentScreen = currentState.getNifty().getCurrentScreen().getScreenId();
		
		if(currentScreen.equals("screen1")){
			currentState.gotoScreenIfNotActive("screen2");	// regular gotoScreen() will restart and loose its state
		} else {
			currentState.gotoScreenIfNotActive("screen1");
		}
	}
	
	
	// initialize the two GameStates
	@Override
	public void initStatesList(final GameContainer container) throws SlickException {
		state1 = new TestStateOne(1);
		state2 = new TestStateTwo(2);
		
		addState(state1);
		addState(state2);
		
		// starting state. First is default. Use the following to start with state2
		// enterState(2);
	}

	
	public static void main(final String[] args) {
		try {
			AppGameContainer container = new AppGameContainer(new MultiStateOverlayExample(
					"Multi GameState Nifty Overlay over Slick"));
			container.setDisplayMode(1024, 768, false);
			container.setTargetFrameRate(1000);
			container.setMinimumLogicUpdateInterval(10);
			container.setMinimumLogicUpdateInterval(20);
//			container.setVSync(true);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
