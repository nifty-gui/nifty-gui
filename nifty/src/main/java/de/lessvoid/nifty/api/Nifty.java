package de.lessvoid.nifty.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.jglfont.JGLFontFactory;

import de.lessvoid.nifty.api.input.NiftyInputConsumer;
import de.lessvoid.nifty.api.input.NiftyKeyboardEvent;
import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.InternalNiftyImage;
import de.lessvoid.nifty.internal.InternalNiftyNode;
import de.lessvoid.nifty.internal.NiftyNodeRenderOrderComparator;
import de.lessvoid.nifty.internal.accessor.NiftyAccessor;
import de.lessvoid.nifty.internal.common.Statistics;
import de.lessvoid.nifty.internal.common.StatisticsRendererFPS;
import de.lessvoid.nifty.internal.render.NiftyRenderer;
import de.lessvoid.nifty.internal.render.font.FontRenderer;
import de.lessvoid.nifty.spi.NiftyInputDevice;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.nifty.spi.TimeProvider;

/**
 * The main control class of all things Nifty.
 * @author void
 */
public class Nifty {
  private final static Logger logger = Logger.getLogger(Nifty.class.getName());

  // The resource loader.
  private final NiftyResourceLoader resourceLoader = new NiftyResourceLoader();

  // The one and only NiftyStatistics instance.
  private final NiftyStatistics statistics;
  private final Statistics stats;

  // The NiftyRenderDevice we'll forward all render calls to.
  private final NiftyRenderDevice renderDevice;

  // The TimeProvider to use.
  private final TimeProvider timeProvider;

  // The list of root nodes.
  private final List<NiftyNode> rootNodes = new ArrayList<NiftyNode>();

  // The list of nodes that are able to receive input events
  private final List<NiftyNode> nodesToReceiveEvents = new ArrayList<NiftyNode>();

  // the class performing the conversion from NiftyNode to RenderNode and takes care of all rendering.
  private final NiftyRenderer renderer;

  // the class that interfaces us to input events (mouse, touch, keyboard)
  private NiftyInputDevice inputDevice;

  // the FontFactory
  private final JGLFontFactory fontFactory;

  // the comparator used to sort the list of inputEventReceivers
  private final Comparator<? super NiftyNode> inputEventReceiversComparator = new NiftyNodeRenderOrderComparator();

  // whenever we need to build a string we'll re-use this instance instead of creating new instances all the time
  private final StringBuilder str = new StringBuilder();

  /**
   * Create a new Nifty instance.
   * @param newRenderDevice the NiftyRenderDevice this instance will be using
   * @param newTimeProvider the TimeProvider implementation to use
   */
  public Nifty(final NiftyRenderDevice newRenderDevice, final NiftyInputDevice newInputDevice, final TimeProvider newTimeProvider) {
    renderDevice = newRenderDevice;
    renderDevice.setResourceLoader(resourceLoader);

    inputDevice = newInputDevice;
    inputDevice.setResourceLoader(resourceLoader);

    timeProvider = newTimeProvider;
    statistics = new NiftyStatistics(new Statistics(timeProvider));
    stats = statistics.getImpl();
    renderer = new NiftyRenderer(statistics.getImpl(), newRenderDevice);
    fontFactory = new JGLFontFactory(new FontRenderer(newRenderDevice));
  }

  /**
   * Set the NiftyStatisticsMode to display the statistics.
   * @param mode the new NiftyStatisticsMode
   */
  public void showStatistics(final NiftyStatisticsMode mode) throws IOException {
    switch (mode) {
      case ShowFPS:
        new StatisticsRendererFPS(this);
        break;
    }
  }

  /**
   * Update.
   */
  public void update() {
    stats.startFrame();

    stats.startUpdate();
    for (int i=0; i<rootNodes.size(); i++) {
      rootNodes.get(i).getImpl().update();
    }
    stats.stopUpdate();

    stats.startInputProcessing();
    processInputEvents(collectInputReceivers());
    stats.stopInputProcessing();
  }

  private List<NiftyNode> collectInputReceivers() {
    nodesToReceiveEvents.clear();
    for (int i=0; i<rootNodes.size(); i++) {
      rootNodes.get(i).getImpl().addInputNodes(nodesToReceiveEvents);
    }
    return sortInputReceivers(nodesToReceiveEvents);
  }

  // sorts in place (the source list) and returns the sorted source list
  private List<NiftyNode> sortInputReceivers(final List<NiftyNode> source) {
    Collections.sort(source, inputEventReceiversComparator);
    return source;
  }

  private void logInputReceivers(final List<NiftyNode> source) {
    str.setLength(0);
    str.append("inputReceivers: ");
    for (int j=source.size()-1; j>=0; j--) {
      str.append("[");
      str.append(source.get(j).getImpl().getId());
      str.append("]");
      str.append(" ");
    }
    logger.fine(str.toString());
  }

  private void processInputEvents(final List<NiftyNode> inputReceivers) {
    inputDevice.forwardEvents(new NiftyInputConsumer() {
      @Override
      public boolean processPointerEvent(final NiftyPointerEvent... pointerEvents) {
        logInputReceivers(inputReceivers);

        for (int i=0; i<pointerEvents.length; i++) {
          for (int j=inputReceivers.size()-1; j>=0; j--) {
            inputReceivers.get(j).getImpl().pointerEvent(pointerEvents[i]);
          }
        }
        return false;
      }
      
      @Override
      public boolean processKeyboardEvent(final NiftyKeyboardEvent keyEvent) {
        return false;
      }
    });
  }

  /**
   * Render.
   */
  public boolean render() {
    stats.startRender();
    boolean frameChanged = renderer.render(rootNodes);
    stats.stopRender();
    stats.endFrame();
    return frameChanged;
  }

  /**
   * Create a new root node with a given width, height and child layout. A root node is just a regular NiftyNode that
   * forms the base node of a scene graph. You can add several root nodes!
   *
   * @param rootNodePlacementLayout the child layout that defines how to place the new root node on the screen
   * @param width the width of the root node
   * @param height the height of the root node
   * @param childLayout the childLayout for the root node (this determines the way any child nodes will be laid out
   * in the new rootNode)
   * 
   * @return a new NiftyNode acting as the root of a Nifty scene graph
   */
  public NiftyNode createRootNode(
      final UnitValue width,
      final UnitValue height,
      final ChildLayout childLayout) {
    return createRootNode(ChildLayout.Center, width, height, childLayout);
  }

  /**
   * Create a new root node with a given width, height and child layout. A root node is just a regular NiftyNode that
   * forms the base node of a scene graph. You can add several root nodes!
   *
   * @param rootNodePlacementLayout the child layout that defines how to place the new root node on the screen
   * @param width the width of the root node
   * @param height the height of the root node
   * @param childLayout the childLayout for the root node (this determines the way any child nodes will be laid out
   * in the new rootNode)
   * 
   * @return a new NiftyNode acting as the root of a Nifty scene graph
   */
  public NiftyNode createRootNode(
      final ChildLayout rootNodePlacementLayout,
      final UnitValue width,
      final UnitValue height,
      final ChildLayout childLayout) {
    NiftyNode rootNodeInternal = createRootNode(rootNodePlacementLayout);
    rootNodeInternal.setWidthConstraint(width);
    rootNodeInternal.setHeightConstraint(height);
    rootNodeInternal.setChildLayout(childLayout);
    return rootNodeInternal;
  }

  /**
   * @see #createRootNode(UnitValue, UnitValue, ChildLayout)
   *
   * Additionally this method will make the created root node the same size as the current screen.
   * 
   * @return a new NiftyNode
   */
  public NiftyNode createRootNodeFullscreen() {
    return createRootNode(ChildLayout.Center, UnitValue.px(getScreenWidth()), UnitValue.px(getScreenHeight()), ChildLayout.None);
  }

  /**
   * @see #createRootNode(UnitValue, UnitValue, ChildLayout)
   *
   * Additionally this method will make the created root node the same size as the current screen.
   * 
   * @param childLayout the childLayout for the root node (this determines the way any child nodes will be layed out
   * in the new rootNode)
   * @return a new NiftyNode
   */
  public NiftyNode createRootNodeFullscreen(final ChildLayout childLayout) {
    return createRootNode(ChildLayout.Center, UnitValue.px(getScreenWidth()), UnitValue.px(getScreenHeight()), childLayout);
  }

  /**
   * Create a new NiftyImage.
   * @param filename the filename to load
   *
   * @return a new NiftyImage
   */
  public NiftyImage createNiftyImage(final String filename) {
    return NiftyImage.newInstance(InternalNiftyImage.newImage(renderDevice.loadTexture(filename, true)));
  }

  /**
   * Get the width of the current screen mode.
   * @return width of the current screen
   */
  public int getScreenWidth() {
    return renderDevice.getDisplayWidth();
  }

  /**
   * Get the height of the current screen mode.
   * @return height of the current screen
   */
  public int getScreenHeight() {
    return renderDevice.getDisplayHeight();
  }

  /**
   * Output the state of all root nodes (and the whole tree below) to a String. This is meant to aid in debugging.
   * DON'T RELY ON ANY INFORMATION IN HERE SINCE THIS CAN BE CHANGED IN FUTURE RELEASES!
   *
   * @return String that contains the debugging info for all root nodes
   */
  public String getSceneInfoLog() {
    return getSceneInfoLog("(?s).*");
  }

  /**
   * Output the state of all root nodes (and the whole tree below) to a String. This is meant to aid in debugging.
   * DON'T RELY ON ANY INFORMATION IN HERE SINCE THIS CAN BE CHANGED IN FUTURE RELEASES!
   *
   * @param filter regexp to filter the output (Example: "position" will only output position info)
   * @return String that contains the debugging info for all root nodes
   */
  public String getSceneInfoLog(final String filter) {
    StringBuilder result = new StringBuilder("Nifty scene info log\n");
    for (int i=0; i<rootNodes.size(); i++) {
      rootNodes.get(i).getStateInfo(result, filter);
    }
    return result.toString();
  }

  /**
   * Get the NiftyStatistics instance where you can request a lot of statistics about Nifty.
   * @return the NiftyStatistics instance
   */
  public NiftyStatistics getStatistics() {
    return statistics;
  }

  /**
   * Get the TimeProvider of this Nifty instance.
   * @return the TimeProvider
   */
  public TimeProvider getTimeProvider() {
    return timeProvider;
  }

  /**
   * Call this to let Nifty clear the screen when it renders the GUI. This might be useful when the only thing you're
   * currently rendering is the GUI. If you render the GUI as an overlay you better not enable that :)
   */
  public void clearScreenBeforeRender() {
    renderDevice.clearScreenBeforeRender(true);
  }

  /**
   * Load a NiftyFont with the given name.
   *
   * @param name the name of the NiftyFont
   * @return NiftyFont
   * @throws IOException
   */
  public NiftyFont createFont(final String name) throws IOException {
    return new NiftyFont(fontFactory.loadFont(resourceLoader.getResourceAsStream(name), name, 12));
  }

  /**
   * Create a NiftyCanvasPainter that uses a customer shader to render into the canvas.
   *
   * @param shaderName the fragment shader filename to load and use
   * @return a NiftyCanvasPainter using the given shader
   */
  public NiftyCanvasPainter customShaderCanvasPainter(final String shaderName) {
    return new NiftyCanvasPainterShader(renderDevice, shaderName);
  }

  // Friend methods

  NiftyRenderDevice getRenderDevice() {
    return renderDevice;   
  }

  // Internal methods

  private NiftyNode createRootNode(final ChildLayout rootNodePlacementLayout) {
    NiftyNode rootNodeInternal = createRootNodeInternal(rootNodePlacementLayout);
    rootNodes.add(rootNodeInternal);
    return rootNodeInternal;
  }

  private NiftyNode createRootNodeInternal(final ChildLayout rootNodePlacementLayout) {
    return NiftyNode.newInstance(InternalNiftyNode.newRootNode(this, rootNodePlacementLayout));
  }

  static {
    NiftyAccessor.DEFAULT = new InternalNiftyAccessorImpl();
  }
}
