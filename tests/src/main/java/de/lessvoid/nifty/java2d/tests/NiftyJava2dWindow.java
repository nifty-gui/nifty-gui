package de.lessvoid.nifty.java2d.tests;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.java2d.input.InputSystemAwtImpl;
import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;
import de.lessvoid.nifty.java2d.renderer.GraphicsWrapper;
import de.lessvoid.nifty.java2d.renderer.RenderDeviceJava2dImpl;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundDeviceNullImpl;
import de.lessvoid.nifty.tools.TimeProvider;

public abstract class NiftyJava2dWindow implements GraphicsWrapper {

	protected static final Logger logger = LoggerFactory
			.getLogger(NiftyJava2dWindow.class);

	protected Nifty nifty;

    private final Canvas canvas;

	public NiftyJava2dWindow(String title, int width, int height) {
		this(title, width, height, new SoundDeviceNullImpl());
	}

	public NiftyJava2dWindow(String title, int width, int height,
			SoundDevice soundDevice) {

		InputSystemAwtImpl inputSystem = new InputSystemAwtImpl();

		canvas = new Canvas();

		canvas.addMouseMotionListener(inputSystem);
		canvas.addMouseListener(inputSystem);
		canvas.addKeyListener(inputSystem);

		Frame f = new Frame(title);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				logger.info("exit from main window");
				System.exit(0);
			}
		});

		f.add(canvas);
		f.pack();
		f.setSize(new Dimension(width, height));
		f.setVisible(true);
		// f.setIgnoreRepaint(true);
		// canvas.setIgnoreRepaint(true);

        canvas.createBufferStrategy(2);

        FontProviderJava2dImpl fontProvider = new FontProviderJava2dImpl();
		registerFonts(fontProvider);

		RenderDeviceJava2dImpl renderDevice = new RenderDeviceJava2dImpl(this);
		renderDevice.setFontProvider(fontProvider);

		nifty = new Nifty(renderDevice, soundDevice, inputSystem,
				new TimeProvider());
		init();
		// init(filename, screenName);
	}

	// protected void init(String filename, String screenName) {
	// nifty.fromXml(filename, screenName);
	// }

	protected abstract void init();

	protected void registerFonts(FontProviderJava2dImpl fontProviderJava2dImpl) {

	}

	long fps = 0;

    private Graphics2D graphics2D;

	public long getFrameTime() {
		return 1000 / getFramesPerSecond();
	}

	public long getFramesPerSecond() {
		return fps;
	}

	public void start() {
		boolean done = false;

		long time = System.currentTimeMillis();
		long frames = 0;

		while (!done) {

	        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
            graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

			done = nifty.render(true);

			bufferStrategy.show();
			graphics2D.dispose();
			graphics2D = null;

			frames++;

			long diff = System.currentTimeMillis() - time;
			if (diff >= 1000) {
				fps = frames;
				time += diff;
				System.out.println("fps: " + frames);
				frames = 0;
			}
		}
	}

    @Override
    public Graphics2D getGraphics2d() {
        return graphics2D;
    }

    @Override
    public int getHeight() {
        return canvas.getHeight();
    }

    @Override
    public int getWidth() {
        return canvas.getWidth();
    }

}