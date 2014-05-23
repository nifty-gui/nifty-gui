package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.nifty.internal.math.Mat4;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.NiftyTexture;

public class BatchManager {
  private static Logger log = Logger.getLogger(BatchManager.class.getName());

  private List<Batch> activeBatches = new ArrayList<Batch>();
  private Batch currentBatch;

  public BatchManager() {
    currentBatch = null;
  }

  public void begin() {
    activeBatches.clear();
    if (currentBatch != null) {
      currentBatch.reset();
    }
  }

  public void add(final NiftyTexture niftyTexture, final Mat4 mat) {
    currentBatch = ensureBatch(niftyTexture);
    if (currentBatch.add(
        0.0, 0.0,
        niftyTexture.getWidth(), niftyTexture.getHeight(),
        niftyTexture.getU0(), niftyTexture.getV0(),
        niftyTexture.getU1(), niftyTexture.getV1(),
        mat)) {
      return;
    }
    currentBatch = addNewBatch(niftyTexture);
    if (currentBatch.add(
        0.0, 0.0,
        niftyTexture.getWidth(), niftyTexture.getHeight(),
        niftyTexture.getU0(), niftyTexture.getV0(),
        niftyTexture.getU1(), niftyTexture.getV1(),
        mat)) {
      return;
    }
    // WTF?
    throw new RuntimeException("Created new batch but couldn't add any data to it. This should never happen!");
  }

  public void end(final NiftyRenderDevice renderDevice) {
    renderDevice.begin();
    for (int i=0; i<activeBatches.size(); i++) {
      activeBatches.get(i).render(renderDevice);
    }
    renderDevice.end();
  }

  private Batch ensureBatch(final NiftyTexture niftyTexture) {
    if (currentBatch == null) {
      return addNewBatch(niftyTexture);
    }
    if (currentBatch.needsNewBatch(niftyTexture)) {
      return addNewBatch(niftyTexture);
    }
    return currentBatch;
  }

  private Batch addNewBatch(final NiftyTexture niftyTexture) {
    Batch batch = new Batch(niftyTexture);
    activeBatches.add(batch);
    log.fine("new batch added. total count now: " + activeBatches.size());
    return batch;
  }
}
