package de.lessvoid.nifty.java2d.tests;

import java.awt.Font;

import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;
import de.lessvoid.nifty.spi.sound.SoundDeviceNullImpl;

public class AllExamplesJava2d extends NiftyJava2dWindow {

	public static void main(String[] args) throws InterruptedException {
		new AllExamplesJava2d("Nifty Java2d Renderer - HelloWorld example",
				1024, 768).start();
	}

	public AllExamplesJava2d(String title, int width, int height) {
		super(title, width, height, new SoundDeviceNullImpl());
	}

	@Override
	protected void registerFonts(FontProviderJava2dImpl fontProviderJava2dImpl) {
		fontProviderJava2dImpl.addFont("arial.fnt", new Font("arial",
				Font.BOLD, 24));
	}

	@Override
	protected void init() {
		nifty.fromXml("all/intro.xml", "start");
	}

}
