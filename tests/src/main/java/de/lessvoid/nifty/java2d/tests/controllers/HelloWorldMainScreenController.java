package de.lessvoid.nifty.java2d.tests.controllers;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class HelloWorldMainScreenController implements ScreenController {

	private Nifty nifty;

	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
	}

	public void onStartScreen() {
		System.out.println("onstart");
	}

	public void onEndScreen() {
		System.out.println("onend");
	}
	
	public void quit() {
	    nifty.setAlternateKeyForNextLoadXml("fade");
	    nifty.fromXml("helloworld.xml", "start");
	}
	


}
