package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.lessvoid.nifty.internal.math.Mat4;
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
  }

  public void add(final NiftyTexture niftyTexture, final Mat4 mat) {
    currentBatch = ensureBatch(niftyTexture.getAtlasId());
    if (currentBatch.add(
        0.0, 0.0,
        niftyTexture.getWidth(), niftyTexture.getHeight(),
        niftyTexture.getU0(), niftyTexture.getV0(),
        niftyTexture.getU1(), niftyTexture.getV1(),
        mat)) {
      return;
    }
    currentBatch = addNewBatch(niftyTexture.getAtlasId());
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

  public void end() {
  }

  private Batch ensureBatch(final int atlasId) {
    if (currentBatch == null) {
      return addNewBatch(atlasId);
    }
    if (currentBatch.needsNewBatch(atlasId)) {
      return addNewBatch(atlasId);
    }
    return currentBatch;
  }

  private Batch addNewBatch(final int atlasId) {
    Batch batch = new Batch(atlasId);
    activeBatches.add(batch);
    log.info("new batch added. total count now: " + activeBatches.size());
    return batch;
  }
}
