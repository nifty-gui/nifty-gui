package de.lessvoid.nifty.java2d.tests;

import java.awt.Font;

import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;

public class HelloWorldJava2d extends NiftyJava2dWindow {

	public static void main(String[] args) throws InterruptedException {
		new HelloWorldJava2d("Nifty Java2d Renderer - HelloWolrd example", 800,
				600).start();
	}

	public HelloWorldJava2d(String title, int width, int height) {
		super(title, width, height);
	}

	@Override
	protected void registerFonts(FontProviderJava2dImpl fontProviderJava2dImpl) {
		fontProviderJava2dImpl.addFont("arial.fnt", new Font("arial",
				Font.BOLD, 24));
	}

	@Override
	protected void init() {
		nifty.fromXml("helloworld.xml", "start");
	}

}
