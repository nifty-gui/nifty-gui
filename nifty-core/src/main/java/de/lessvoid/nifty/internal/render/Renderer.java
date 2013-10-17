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
 * This class internally translates the list of root NiftyNodes into a list of RootRenderNode. This means there exists
 * a duplicate of the original scene graph as a list of render nodes.
 *
 * The reason for this extra amount of work including the work to synchronize the trees is that rendering will happen
 * in it's own thread and must not interference with the main scene graph (that is happening in another thread). The
 * call to RendererNodeSynch.synchronize() will therefore be the only point where we need to stop both threads.
 * Everything can happen independently from each other.
 *
 * @author void
 */
public class Renderer {
  // the reference to the Statistics instance we'll update
  private final Statistics stats;

  // since we need to render stuff we'll keep the NiftyRenderDevice instance around
  private final NiftyRenderDevice renderDevice;

  // the helper class to sync real nifty nodes with render nodes 
  private final RendererNodeSync rendererSync;

  // the list of root render nodes representing the real Nifty root nodes (and their hierarchy)
  private final List<RootRenderNode> rootRenderNodes = new ArrayList<RootRenderNode>();

  /**
   * Constructor.
   *
   * @param stats the Statistics instance to gather stats about rendering
   * @param renderDevice the NiftyRenderDevice used for rendering
   */
  public Renderer(final Statistics stats, final NiftyRenderDevice renderDevice) {
    this.stats = stats;
    this.renderDevice = renderDevice;
    this.rendererSync = new RendererNodeSync(renderDevice);
  }

  /**
   * Render the given list of root NiftyNodes. This class will translate the Nodes into RenderNodes and keep a list.
   * On each following call this list will be synchronized with the list of root nodes.
   *
   * @param rootNodes the list of Nifty root nodes that should be rendered
   * @return true when any content of the new frame changed and false if nothing has been rendered (since the frame
   * looks exactly as the last one). 
   */
  public boolean render(final List<NiftyNode> rootNodes) {
    if (!synchronize(rootNodes, rootRenderNodes)) {
      return false;
    }

    render();
    return true;
  }

  private boolean synchronize(final List<NiftyNode> sourceNodes, final List<RootRenderNode> destinationNodes) {
    stats.start(Type.Synchronize);
    boolean changed = rendererSync.synchronize(sourceNodes, destinationNodes);
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
