package de.lessvoid.nifty.renderer.lwjgl.render.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ImageData {
  int getDepth();
  int getHeight();
  int getTexHeight();
  int getTexWidth();
  int getWidth();
  ByteBuffer getImageBufferData();
  ByteBuffer loadImage(InputStream fis) throws IOException;
  ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException;
  ByteBuffer loadImage(InputStream fis, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException;

  // these methods don't scale to power of 2 stuff and are used for special loading (f.i. for mouse cursors)
  ByteBuffer loadMouseCursorImage(InputStream fis) throws IOException;
}