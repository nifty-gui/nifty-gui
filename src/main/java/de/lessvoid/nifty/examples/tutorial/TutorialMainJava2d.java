package de.lessvoid.nifty.examples.tutorial;


public final class TutorialMainJava2d {

  public static void main(final String[] args) {
    // The Java2d Renderer is still work in progress and have been disabled for the nifty-examples 1.2 release for now

    /*
    InputSystemAwtImpl inputSystem = new InputSystemAwtImpl();

    Dimension dimension = new Dimension(1024, 768);

    final Canvas canvas = new Canvas();
    canvas.addMouseMotionListener(inputSystem);
    canvas.addMouseListener(inputSystem);

    Frame f = new Frame("Nifty Java 2d");
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
    f.setSize(dimension);
    f.setVisible(true);
    // f.setIgnoreRepaint(true);
    // canvas.setIgnoreRepaint(true);

    RenderDeviceJava2dImpl renderDevice = new RenderDeviceJava2dImpl(canvas);

    Nifty nifty = new Nifty(renderDevice, new SoundSystem(new SlickSoundDevice()), inputSystem, new TimeProvider());
    nifty.fromXml("tutorial/tutorial.xml", "start");

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
*/
  }
}
