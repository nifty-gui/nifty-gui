package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.common.InternalNiftyStatistics;
import de.lessvoid.nifty.internal.common.InternalNiftyStatistics.Type;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

public class Render {
  private final List<RenderNodeContent> rootRenderNodes = new ArrayList<RenderNodeContent>();
  private final InternalNiftyStatistics statistics;
  private final NiftyRenderDevice renderDevice;
  private final RenderSync rendererSync;

  public Render(final InternalNiftyStatistics statistics, final NiftyRenderDevice renderDevice) {
    this.statistics = statistics;
    this.renderDevice = renderDevice;
    this.rendererSync = new RenderSync(statistics, renderDevice);
  }

  public boolean render(final List<NiftyNode> rootNodes) {
    if (!synchronize(rootNodes)) {
      return false;
    }

    render();
    return true;
  }

  private void render() {
    renderDevice.begin();
    for (int i=0; i<rootRenderNodes.size(); i++) {
      rootRenderNodes.get(i).render(renderDevice);
    }
    renderDevice.end();
  }

  private boolean synchronize(final List<NiftyNode> rootNodes) {
    statistics.start(Type.Synchronize);
    boolean changed = rendererSync.synchronize(rootNodes, rootRenderNodes);
    statistics.stop(Type.Synchronize);
    return changed;
  }
}
