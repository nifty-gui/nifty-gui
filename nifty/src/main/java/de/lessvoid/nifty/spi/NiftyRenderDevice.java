package de.lessvoid.nifty.spi;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.api.BlendMode;
import de.lessvoid.nifty.api.NiftyColor;
import de.lessvoid.nifty.api.NiftyLineCapType;
import de.lessvoid.nifty.api.NiftyLineJoinType;
import de.lessvoid.nifty.api.NiftyLinearGradient;
import de.lessvoid.nifty.api.NiftyResourceLoader;

/**
 * NiftyRenderDevice is part of the SPI that allows Nifty to use different graphics backends. Everything that can
 * be rendered will go through implementations of this interface.
 *
 * @author void
 */
public interface NiftyRenderDevice {

  /**
   * Gives this RenderDevice access to the NiftyResourceLoader.
   *
   * @param niftyResourceLoader NiftyResourceLoader
   */
  void setResourceLoader(@Nonnull NiftyResourceLoader niftyResourceLoader);

  /**
   * Get the width of the display.
   * @return width of display mode
   */
  int getDisplayWidth();

  /**
   * Get the height of the display.
   * @return height of display mode
   */
  int getDisplayHeight();

  /**
   * Enable clearing the screen when rendering.
   */
  void clearScreenBeforeRender(boolean clearScreenBeforeRender);

  /**
   * Create a texture of the given width and height.
   *
   * @param width the width of the texture
   * @param height the height of the texture
   * @return NiftyTexture
   */
  NiftyTexture createTexture(int width, int height, boolean filterLinear);

  /**
   * Create a texture of the given width and height and initialize it with the given pixel data.
   *
   * @param width the width of the texture
   * @param height the height of the texture
   * @param data the pixel data of the texture
   * @return NiftyTexture
   */
  NiftyTexture createTexture(int width, int height, ByteBuffer data, boolean filterLinear);

  /**
   * Load an image and return a NiftyTexture from the image.
   *
   * @param filename the image filename to load
   * @return NiftyTexture
   */
  NiftyTexture loadTexture(String filename, boolean filterLinear);

  /**
   * Nifty will call this before it issues render*() calls.
   */
  void beginRender();

  /**
   * Render a textured Quad with the given NiftyTexture using vertex data given in the FloatBuffer containing:
   * - 2 floats x and y vertex position
   * - 2 floats u and v texture coordinates
   * - 4 floats red, green, blue, alpha color values
   *
   * @param texture the NiftyTexture to render
   * @param vertices the vertex data to render
   */
  void render(NiftyTexture texture, FloatBuffer vertices);

  /**
   * Render an untextured Quad with the vertex data given in the FloatBuffer containing:
   * - 2 floats x and y vertex position
   * - 4 floats red, green, blue, alpha color values for the vertex
   *
   * @param vertices the vertex data to render
   */
  void renderColorQuads(FloatBuffer vertices);

  /**
   * Render a linear gradient with the given parameters using vertex data given in the FloatBuffer containing:
   * - 2 floats x and y vertex position
   *
   * @param gradientParams the NiftyLinearGradient containing the linear gradient parameters to use
   * @param vertices the vertex data to render
   */
  void renderLinearGradientQuads(NiftyLinearGradient gradientParams, FloatBuffer vertices);

  /**
   * Called after all render*() calls are done to end rendering.
   */
  void endRender();

  /**
   * Redirect all subsequent render calls to the NiftyTexture given as a parameter. 
   * @param texture the texture to use
   */
  void beginRenderToTexture(NiftyTexture texture);

  /**
   * Stop rendering to the given texture.
   * @param texture the texture to use in the beginRenderToTexture() call to start rendering.
   */
  void endRenderToTexture(NiftyTexture texture);

  /**
   * Change the current BlendMode for subsequent render calls.
   * @param blendMode the BlendMode
   */
  void changeBlendMode(BlendMode blendMode);

  /**
   * Load a custom shader to be used for rendering later.
   * @param filename the filename of the shader code to load.
   * @return some kind of identification to activate this custom shader later for rendering
   */
  public String loadCustomShader(String filename);

  /**
   * Activate the custom shader with the given shaderId
   * @param shaderId the shaderId to activate
   */
  public void activateCustomShader(String shaderId);

  /**
   * @param lineParameters 
   */
  void pathBegin(LineParameters lineParameters);

  /**
   * Render lines from the given FloatBuffer containing the line vertices.
   * @param b the FloatBuffer containing the vertex data
   * @param lineParameters the LineParameters to render ALL lines in the FloatBuffer with
   */
  void pathLines(FloatBuffer b, LineParameters lineParameters);

  /**
   * Render arcs using the information given. You'll get a quad the size of the full arc with the following coordinates
   *
   *  (-1, -1)        ( 1, -1)
   *     +---------------+
   *     |               |
   *     |               |
   *     |       +       |
   *     |               |
   *     |               |
   *     +---------------+
   *  (-1, 1)         ( 1,  1)
   *
   * The quad will be the the size of the circle centered around the center of the circle. You'll get the following
   * per vertex informations for each circle / quad (4 vertices per quad): 
   * - 2 floats x and y vertex position
   * - 2 floats u and v "texture" coordinates with the coordinates given in the diagram above
   *
   * @param vertices the vertex data to render
   * @param arcParameters the parameters for all the arcs to be rendered
   */
  void pathArcs(FloatBuffer vertices, ArcParameters arcParameters);

  /**
   * @param lineParameters 
   */
  void pathEnd(LineParameters lineParameters);

  /**
   * This collects all available parameters for rendering lines. This is part of the NiftyRenderDevice interface
   * because it is only part of the SPI and not really a part of the public Nifty API in the sense that this class
   * is only used to communicate settings between Nifty and the NiftyRenderDevice. This class is not meant to be used
   * by actual user code.
   */
  public static class LineParameters {
    private NiftyLineCapType lineCapType = NiftyLineCapType.Round;
    private NiftyLineJoinType lineJoinType = NiftyLineJoinType.Miter;
    private float lineWidth = 1.f;
    private NiftyColor lineColor = NiftyColor.WHITE();

    public LineParameters() {
    }

    public LineParameters(final LineParameters lineParameters) {
      lineCapType = lineParameters.getLineCapType();
      lineJoinType = lineParameters.getLineJoinType();
      lineWidth = lineParameters.getLineWidth();
      lineColor = lineParameters.getColor();
    }

    public NiftyLineCapType getLineCapType() {
      return lineCapType;
    }

    public void setLineCapType(final NiftyLineCapType lineCapType) {
      if (lineCapType == null) {
        throw new IllegalArgumentException("LineCapType can't be null");
      }
      this.lineCapType = lineCapType;
    }

    public NiftyLineJoinType getLineJoinType() {
      return lineJoinType;
    }

    public void setLineJoinType(final NiftyLineJoinType lineJoinType) {
      if (lineJoinType == null) {
        throw new IllegalArgumentException("LineJoinType can't be null");
      }
      this.lineJoinType = lineJoinType;
    }

    public float getLineWidth() {
      return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
      this.lineWidth = lineWidth;
    }

    public void setColor(final NiftyColor color) {
      this.lineColor = color;
    }

    public NiftyColor getColor() {
      return lineColor;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((lineCapType == null) ? 0 : lineCapType.hashCode());
      result = prime * result + ((lineColor == null) ? 0 : lineColor.hashCode());
      result = prime * result + ((lineJoinType == null) ? 0 : lineJoinType.hashCode());
      result = prime * result + Float.floatToIntBits(lineWidth);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      LineParameters other = (LineParameters) obj;
      if (lineCapType != other.lineCapType)
        return false;
      if (lineColor == null) {
        if (other.lineColor != null)
          return false;
      } else if (!lineColor.equals(other.lineColor))
        return false;
      if (lineJoinType != other.lineJoinType)
        return false;
      if (Float.floatToIntBits(lineWidth) != Float.floatToIntBits(other.lineWidth))
        return false;
      return true;
    }
  }

  /**
   * This class stores all of the properties necessary to render an arc.
   */
  public static class ArcParameters {
    private final LineParameters lineParameters;
    private final float startAngle;
    private final float endAngle;
    private final float r;

    public ArcParameters(final LineParameters lineParameters, final float startAngle, final float endAngle, final float r) {
      this.lineParameters = new LineParameters(lineParameters);
      this.startAngle = startAngle;
      this.endAngle = endAngle;
      this.r = r;
    }

    public ArcParameters(final ArcParameters arcParameters) {
      this.lineParameters = new LineParameters(arcParameters.lineParameters);
      this.startAngle = arcParameters.startAngle;
      this.endAngle = arcParameters.endAngle;
      this.r = arcParameters.r;
    }

    public LineParameters getLineParameters() {
      return lineParameters;
    }

    public float getStartAngle() {
      return startAngle;
    }

    public float getEndAngle() {
      return endAngle;
    }

    public float getRadius() {
      return r;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Float.floatToIntBits(endAngle);
      result = prime * result + ((lineParameters == null) ? 0 : lineParameters.hashCode());
      result = prime * result + Float.floatToIntBits(r);
      result = prime * result + Float.floatToIntBits(startAngle);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ArcParameters other = (ArcParameters) obj;
      if (Float.floatToIntBits(endAngle) != Float.floatToIntBits(other.endAngle))
        return false;
      if (lineParameters == null) {
        if (other.lineParameters != null)
          return false;
      } else if (!lineParameters.equals(other.lineParameters))
        return false;
      if (Float.floatToIntBits(r) != Float.floatToIntBits(other.r))
        return false;
      if (Float.floatToIntBits(startAngle) != Float.floatToIntBits(other.startAngle))
        return false;
      return true;
    }
  }
}
