package de.lessvoid.nifty.examples.slick.niftyoverlay;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.slick.NiftyOverlayGameState;

/**
 * The second Slick test GameState that has a Nifty overlay
 * using NiftyOverlayGameState.
 * 
 * @author Durandal
 * @see mail to: nifty(at)durandal.nl
 *
 */
public class TestStateTwo extends NiftyOverlayGameState {

	private NiftyImage icon[] = new NiftyImage[3];	// Some icons to show
	private Color currentColor;						// color of the Slick text
	private Font font;								// font for the visually impaired
	

	public TestStateTwo(int ID) {
		super(ID);

		// load the icons for this example
		icon[0] = getNifty().getRenderEngine().createImage("data/ui/icon1.png",	false);
		icon[1] = getNifty().getRenderEngine().createImage("data/ui/icon2.png",	false);
		icon[2] = getNifty().getRenderEngine().createImage("data/ui/icon3.png",	false);
	}

	
	/**
	 * Override this method to set up the Nifty XML and ScreenControllers you want to use
	 */
	@Override
	public void xmlLoader() {
		// declare your Nifty xmlFile, start screen and controller(s) here
		setXML("data/ui/multiOverlay.xml", "screen2", new TestStateTwo.Controller());

		// note: screen controller cannot be an in-line or private inner Class class 
		// because it needs to be referenced by class in the Nifty XML controller property 
	}

	
	/**
	 * This method catches all events not handled by Nifty, and should be handled
	 * by the game.
	 * 
	 * Note that events are thrown twice: one for key up and one for key down
	 * 
	 * Note also that event processing runs in the render() method so event handling should
	 * be made as fast as possible.
	 * 
	 * @return true if you consumed the event, false if not 
	 */
	@Override
	public boolean processKeyboardEvent(KeyboardInputEvent event) {
		System.out.println("* kb * "+event.getKey()+" "+event.getCharacter()+" "+event.isKeyDown());

		if(!event.isKeyDown()) return false; // we don't want to parse keyUp events for this example
		
		switch(event.getKey()){
		case KeyboardInputEvent.KEY_ESCAPE: 		
			System.exit(0);
			break;
		case KeyboardInputEvent.KEY_1: 		
			currentColor = Color.red;
			doEffect("red");
			break;
		case KeyboardInputEvent.KEY_2: 		
			currentColor = Color.green;
			doEffect("green");
			break;
		case KeyboardInputEvent.KEY_3: 		
			currentColor = Color.blue;
			doEffect("blue");
			break;
		case KeyboardInputEvent.KEY_A: 		
			switchIcon(0);
			break;
		case KeyboardInputEvent.KEY_B: 		
			switchIcon(1);
			break;
		case KeyboardInputEvent.KEY_C: 		
			switchIcon(2);
			break;
		case KeyboardInputEvent.KEY_SPACE: 	
			showOverlay(!isOverlayShown()); // toggle showing the Nifty Overlay on space key
			break;
		case KeyboardInputEvent.KEY_F1:
			// switches between Game States (and their Nifty Screens)
			((MultiStateOverlayExample)getGame()).nextState();	// TODO would be nice to get rid of the type casting requirement
			break;
		case KeyboardInputEvent.KEY_F2:
			// switches between Nifty screens within the GameState
			((MultiStateOverlayExample)getGame()).nextScreen();	// TODO would be nice to get rid of the type casting requirement
			break;
		}

		return false;	// actually we should return true on every event that we consumed
	}

	
	/**
	 * This method catches all events not handled by Nifty, and should be handled
	 * by the game.
	 * 
	 * Note that events are thrown twice: one for mouse up and one for mouse down
	 * 
	 * Note also that event processing runs in the render() method so event handling should
	 * be made as fast as possible.
	 * 
	 * @return true if you consumed the event, false if not 
	 */
	@Override
	 public boolean processMouseEvent(int mouseX, int mouseY, int mouseWheel, int button, boolean buttonDown) {
    System.out.println("*mous* "+mouseX+" "+mouseY+" "+mouseX+" "+buttonDown);
		return false;
	}

	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
	    font = new AngelCodeFont("data/fonts/menu.fnt", "data/fonts/menu.png");
	    currentColor = Color.white;
	}

	/**
	 * Override the render method with your own Slick render logic, but make sure
	 * to call it's superclass to render the Nifty overlay.
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// render our own Slick stuff
	    g.setColor(currentColor);
	    g.setFont(font);
	    g.drawString("TestState "+getID(), 100, 100);
	    g.drawString("1-3 will switch between colors", 100, 200);
	    g.drawString("a-c will switch between images", 100, 250);
	    g.drawString("(this is all slick rendering!)", 100, 300);
	    g.drawString("space toggles the Nifty overlay on/off", 100, 370);
	    g.drawString("F1 switches between GameStates", 100, 420);
	    g.drawString("(F2 switches screens in a GameState)", 100, 470);
	    g.drawString("and this is more slick text", 360, 650);
	    g.drawString("below (!) a nifty-gui overlay", 360, 700);
		
		// super class renders Nifty. Don't forget!
		super.render(container, game, g);
	}

	
	// for th1s example: switch the Nifty test icons upon request
	public void switchIcon(final int iconIndex) {
		getElement("inventar").getRenderer(ImageRenderer.class).setImage( icon[iconIndex] );
		getElement("inventar").startEffect(EffectEventId.onCustom);
	}

	// for th1s example: fire off a Nifty effect
	public void doEffect(final String color) {
		getElement(color).startEffect(EffectEventId.onCustom);
	}


	// TODO  Screen Controller should be a separate in-line class but
	// this can't be referenced to in the Nifty XML (yet)
	//
	/**
	 * This screen controller is is used for this GameState's only Nifty overlay screen,
	 * and performs an exit upon clicking the box.
	 */
	public class Controller implements ScreenController {
		@Override
		public void bind(Nifty nifty, Screen screen) {
			System.out.println(this.getClass().getName() + ".bind(). CurrentScreenID=" + screen.getScreenId());
		}
		@Override
		public void onEndScreen() {
			System.out.println(this.getClass().getName() + ".onEndScreen(). CurrentScreenID="+getNifty().getCurrentScreen().getScreenId());
		}
		@Override
		public void onStartScreen() {
			System.out.println(this.getClass().getName() + ".onStartScreen(). CurrentScreenID="+getNifty().getCurrentScreen().getScreenId());
		}
		public void quit() {
			System.out.println(this.getClass().getName() + ".quit().");
			getNifty().getCurrentScreen().endScreen(new EndNotify() {
				public void perform() {
					getContainer().exit();
				}
			});
		}
	}

}
