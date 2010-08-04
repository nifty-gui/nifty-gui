package de.lessvoid.nifty.java2d.tests;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.java2d.input.InputSystemAwtImpl;
import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;
import de.lessvoid.nifty.java2d.renderer.RenderDeviceJava2dImpl;
import de.lessvoid.nifty.spi.sound.SoundDeviceNullImpl;
import de.lessvoid.nifty.tools.TimeProvider;

public class NiftyJava2dWindow {

	protected Nifty nifty;

	public NiftyJava2dWindow(String title, int width, int height,
			String filename, String screenName) {
		InputSystemAwtImpl inputSystem = new InputSystemAwtImpl();

		final Canvas canvas = new Canvas();

		canvas.addMouseMotionListener(inputSystem);
		canvas.addMouseListener(inputSystem);

		Frame f = new Frame(title);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}
		});

		f.add(canvas);
		f.pack();
		f.setSize(new Dimension(width, height));
		f.setVisible(true);
		// f.setIgnoreRepaint(true);
		// canvas.setIgnoreRepaint(true);

		FontProviderJava2dImpl fontProvider = new FontProviderJava2dImpl();
		registerFonts(fontProvider);

		RenderDeviceJava2dImpl renderDevice = new RenderDeviceJava2dImpl(canvas);
		renderDevice.setFontProvider(fontProvider);

		nifty = new Nifty(renderDevice, new SoundDeviceNullImpl(), inputSystem,
				new TimeProvider());
		nifty.fromXml(filename, screenName);
	}

	protected void registerFonts(FontProviderJava2dImpl fontProviderJava2dImpl) {

	}

	public void start() {
		boolean done = false;

		long time = System.currentTimeMillis();
		long frames = 0;

		while (!done) {

			done = nifty.render(false);
			frames++;

			long diff = System.currentTimeMillis() - time;
			if (diff >= 1000) {
				time += diff;
				System.out.println("fps: " + frames);
				frames = 0;
			}
		}
	}

}