package de.lessvoid.nifty.internal.render;

import java.util.ArrayList;
import java.util.List;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.common.Statistics;
import de.lessvoid.nifty.internal.common.Statistics.Type;
import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * Take a list of public NiftyNodes and render them.
 *
 * This class internally translates the list of NiftyNodes into a list of RenderNodes.
 * @author void
 */
public class Render {
  private final List<RenderNode> rootRenderNodes = new ArrayList<RenderNode>();
  private final Statistics stats;
  private final NiftyRenderDevice renderDevice;
  private final RenderSync rendererSync;

  public Render(final Statistics stats, final NiftyRenderDevice renderDevice) {
    this.stats = stats;
    this.renderDevice = renderDevice;
    this.rendererSync = new RenderSync(stats, renderDevice);
  }

  public boolean render(final List<NiftyNode> rootNodes) {
    if (!synchronize(rootNodes)) {
      return false;
    }

    render();
    return true;
  }

  private boolean synchronize(final List<NiftyNode> rootNodes) {
    stats.start(Type.Synchronize);
    boolean changed = rendererSync.synchronize(rootNodes, rootRenderNodes);
    stats.stop(Type.Synchronize);
    return changed;
  }

  private void render() {
    renderDevice.begin();
    for (int i=0; i<rootRenderNodes.size(); i++) {
      rootRenderNodes.get(i).render(renderDevice);
    }
    renderDevice.end();
  }
}
