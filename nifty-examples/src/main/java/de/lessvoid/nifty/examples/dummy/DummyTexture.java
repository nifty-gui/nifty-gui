package de.lessvoid.nifty.examples.dummy;

import de.lessvoid.nifty.spi.NiftyTexture;

import java.nio.ByteBuffer;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class DummyTexture implements NiftyTexture {
  private final int width;
  private final int height;

  public DummyTexture(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getAtlasId() {
    return 0;
  }

  @Override
  public double getU0() {
    return 0;
  }

  @Override
  public double getV0() {
    return 0;
  }

  @Override
  public double getV1() {
    return 1.0;
  }

  @Override
  public double getU1() {
    return 1.0;
  }

  @Override
  public void saveAsPng(String filename) {

  }

  @Override
  public void dispose() {

  }

  @Override
  public void update(final ByteBuffer buffer) {

  }
}
