import ddf.minim.*;

Nifty nifty;
Minim minim;

void setup() {
  
  // Setup.
  size(1024, 768);
  frameRate(60);
  
  // Initiate Minim.
  minim = new Minim(this);
  
  // Initialize Nifty.
  nifty = new Nifty(new RenderDeviceProcessing(this), new SoundDeviceMinim(minim), new InputSystemProcessing(this), new FastTimeProvider());
  
  // Load UI.
  nifty.fromXml("all/intro.xml", "start");
}

void draw() {
  
  // Clear screen.
  background(0);
  
  // Update Nifty.
  nifty.update();
  nifty.render(false);
}