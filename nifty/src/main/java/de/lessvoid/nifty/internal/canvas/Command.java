package de.lessvoid.nifty.internal.canvas;

import de.lessvoid.nifty.internal.render.batch.BatchManager;



public interface Command {

  void execute(BatchManager contentBatchManager, Context context);

}
