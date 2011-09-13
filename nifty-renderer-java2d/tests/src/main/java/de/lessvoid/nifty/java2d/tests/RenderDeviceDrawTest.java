package de.lessvoid.nifty.java2d.tests;

import java.awt.Font;

import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;

public class RenderDeviceDrawTest extends NiftyJava2dWindow {

	public static void main(String[] args) throws InterruptedException {
		new RenderDeviceDrawTest("Nifty Java2d Renderer - draw test example",
				800, 600).start();
	}

	public RenderDeviceDrawTest(String title, int width, int height) {
		super(title, width, height);
	}

	@Override
	protected void registerFonts(FontProviderJava2dImpl fontProviderJava2dImpl) {
		fontProviderJava2dImpl.addFont("arial.fnt", new Font("arial",
				Font.BOLD, 24));
	}

	@Override
	protected void init() {
		nifty.fromXml("imagetest.xml", "start");
	}

}
