package de.lessvoid.nifty.gdx.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.BufferUtils;

import de.lessvoid.nifty.gdx.render.batch.GdxBatchRenderImage;
import de.lessvoid.nifty.spi.render.MouseCursor;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 */
public final class GdxMouseCursor implements MouseCursor {
  private static Logger log = Logger.getLogger(GdxMouseCursor.class.getName());
  private static org.lwjgl.input.Cursor emptyHWcursor; // Used to hide the hardware cursor
  private final static int VERTICES_PER_QUAD = 6;
  private final static int ATTRIBUTES_PER_VERTEX = 8;
  private final static int BYTES_PER_ATTRIBUTE = 4;
  private final static int PRIMITIVE_SIZE = VERTICES_PER_QUAD * ATTRIBUTES_PER_VERTEX;
  private final static int STRIDE = ATTRIBUTES_PER_VERTEX * BYTES_PER_ATTRIBUTE;
  private FloatBuffer vertexBuffer;
  private float cursorWidth;
  private float cursorHeight;
  // upper left mouse texture location in screen coordinates
  private float mouseX;
  private float mouseY;
  // lower right mouse texture location in screen coordinates
  private float mouseX2;
  private float mouseY2;
  // location of the pixel in the cursor texture that actually clicks
  private float hotspotX;
  private float hotspotY;
  private int mouseTextureId;
  private IntBuffer textureIds; // Save the texture id's (only 1 in this case) for deleting with glDeleteTextures
  private static boolean isHWcursorVisible = true;

  /**
   * @param cursorImage The pre-loaded cursor image. It will be managed / disposed of by this class. You should not
   *                    attempt to use it anymore after this.
   * @param hotspotX The x location of the pixel in the cursor image that actually clicks
   * @param hotspotY The y location of the pixel in the cursor image that actually clicks
   */
  public GdxMouseCursor(GdxBatchRenderImage cursorImage, final int hotspotX, final int hotspotY) {
    textureIds = BufferUtils.newIntBuffer(1);
    cursorWidth = (float) cursorImage.getWidth();
    cursorHeight = (float) cursorImage.getHeight();
    this.hotspotX = (float) hotspotX;
    this.hotspotY = (float) hotspotY;
    updateMousePosition();
    mouseTextureId = createMouseTexture(cursorImage);
    textureIds.put(mouseTextureId);
    initializeVertexBuffer();
    createEmptyHWcursor();
  }

 /**
   * Binds (makes current) the mouse cursor texture. You must call this before {@link #render()}. You are responsible
   * for re-binding any texture you had bound before calling this method.
   */
  public void bind() {
    bind(mouseTextureId);
  }

  /**
   * Draws the mouse cursor on the screen at it's current location, and attempt to hide the system cursor. It will also
   * update the mouse coordinates just before drawing to minimize any lag. Make sure you call {@link #bind()} just
   * before calling this method!
   */
  public void render() {
    updateMousePosition();
    updateVertexBuffer();

    Gdx.gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

    vertexBuffer.position(0);
    Gdx.gl10.glVertexPointer(2, GL10.GL_FLOAT, STRIDE, vertexBuffer);

    vertexBuffer.position(2);
    Gdx.gl10.glColorPointer(4, GL10.GL_FLOAT, STRIDE, vertexBuffer);

    vertexBuffer.position(6);
    Gdx.gl10.glTexCoordPointer(2, GL10.GL_FLOAT, STRIDE, vertexBuffer);

    // GL_QUADS is not supported in libGDX / OpenGL ES, so we'll use GL_TRIANGLES instead.
    Gdx.gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, VERTICES_PER_QUAD);
  }

  /**
   * Hide the system mouse cursor. Call before rendering your custom cursor. This is only viable on the desktop.
   */
  public static void hideSystemCursor() {
    if (isHWcursorVisible) setHWcursorVisible(false);
  }

  /**
   * Show the system mouse cursor on the screen. Call when you stop rendering your custom cursor. This is only viable
   * on the desktop.
   */
  public static void showSystemCursor() {
    if (! isHWcursorVisible) setHWcursorVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    if (textureIds != null) {
      Gdx.gl10.glDeleteTextures(1, textureIds);
      textureIds = null;
    }
    vertexBuffer = null;
  }

  // internal implementations

  private void bind(int textureId) {
    Gdx.gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
  }

  // Create an empty hardware cursor that can be used to hide the hardware cursor
  private static void createEmptyHWcursor() {
    if (emptyHWcursor != null) {
      return;
    }
    try {
      if (org.lwjgl.input.Mouse.isCreated()) {
        int min = org.lwjgl.input.Cursor.getMinCursorSize();
        IntBuffer tmp = BufferUtils.newIntBuffer(min * min);
        emptyHWcursor = new org.lwjgl.input.Cursor(min, min, min / 2, min / 2, 1, tmp, null);
      } else {
        log.log(Level.WARNING, "Could not create empty hardware cursor before Mouse object is created.\n");
      }
    } catch (org.lwjgl.LWJGLException e) {
      log.log(Level.WARNING, "Could not create empty hardware cursor. Reason:\n" + e.getMessage(), e);
    }
  }

  // Make the hardware cursor on desktop either visible or invisible.
  private static void setHWcursorVisible(boolean isVisible) {
    if (Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.Desktop ||
            ! (Gdx.app instanceof com.badlogic.gdx.backends.lwjgl.LwjglApplication) ||
            ! org.lwjgl.input.Mouse.isInsideWindow()) {
      return;
    }
    try {
      org.lwjgl.input.Mouse.setNativeCursor(isVisible ? null : emptyHWcursor);
      isHWcursorVisible = isVisible;
    } catch (org.lwjgl.LWJGLException e) {
      log.log(Level.WARNING, "Could not make the hardware cursor " + (isVisible? "" : "in") + "visible. Reason:\n" + e.getMessage(), e);
    }
  }

  private int createMouseTexture(GdxBatchRenderImage cursorImage) {
    int mouseTextureId = createTextureId();
    bind(mouseTextureId);

    Gdx.gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
    Gdx.gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

    Gdx.gl10.glTexImage2D(
            GL10.GL_TEXTURE_2D,
            0,
            GL10.GL_RGBA,
            cursorImage.getWidth(),
            cursorImage.getHeight(),
            0,
            GL10.GL_RGBA,
            GL10.GL_UNSIGNED_BYTE,
            cursorImage.getData());

    cursorImage.dispose();

    return mouseTextureId;
  }

  private int createTextureId() {
    IntBuffer tmp = BufferUtils.newIntBuffer(1);
    Gdx.gl10.glGenTextures(1, tmp);
    return tmp.get(0);
  }

  private void initializeVertexBuffer() {
    vertexBuffer = BufferUtils.newFloatBuffer(PRIMITIVE_SIZE);
    vertexBuffer.clear();

    // Quad, tessellated into a triangle list with clockwise-winded vertices - (0,1,2), (0,2,3), (0,3,3)
    //
    // 0---1
    // | \ |
    // |  \|
    // 3---2

    // Triangle 1 (0,1,2)

    // Vertex 0
    vertexBuffer.put(mouseX); // x1
    vertexBuffer.put(mouseY); // y1
    vertexBuffer.put(1.0f);   // red
    vertexBuffer.put(1.0f);   // green
    vertexBuffer.put(1.0f);   // blue
    vertexBuffer.put(1.0f);   // alpha
    vertexBuffer.put(0.0f);   // u1
    vertexBuffer.put(0.0f);   // v1

    // Vertex 1
    vertexBuffer.put(mouseX2); // x2
    vertexBuffer.put(mouseY);  // y1
    vertexBuffer.put(1.0f);    // red
    vertexBuffer.put(1.0f);    // green
    vertexBuffer.put(1.0f);    // blue
    vertexBuffer.put(1.0f);    // alpha
    vertexBuffer.put(1.0f);    // u2
    vertexBuffer.put(0.0f);    // v1

    // Vertex 2
    vertexBuffer.put(mouseX2); // x2
    vertexBuffer.put(mouseY2); // y2
    vertexBuffer.put(1.0f);    // red
    vertexBuffer.put(1.0f);    // green
    vertexBuffer.put(1.0f);    // blue
    vertexBuffer.put(1.0f);    // alpha
    vertexBuffer.put(1.0f);    // u2
    vertexBuffer.put(1.0f);    // v2

    // Triangle 2 (0,2,3)

    // Vertex 0
    vertexBuffer.put(mouseX); // x1
    vertexBuffer.put(mouseY); // y1
    vertexBuffer.put(1.0f);   // red
    vertexBuffer.put(1.0f);   // green
    vertexBuffer.put(1.0f);   // blue
    vertexBuffer.put(1.0f);   // alpha
    vertexBuffer.put(0.0f);   // u1
    vertexBuffer.put(0.0f);   // v1

    // Vertex 2
    vertexBuffer.put(mouseX2); // x2
    vertexBuffer.put(mouseY2); // y2
    vertexBuffer.put(1.0f);    // red
    vertexBuffer.put(1.0f);    // green
    vertexBuffer.put(1.0f);    // blue
    vertexBuffer.put(1.0f);    // alpha
    vertexBuffer.put(1.0f);    // u2
    vertexBuffer.put(1.0f);    // v2

    // Vertex 3
    vertexBuffer.put(mouseX);  // x1
    vertexBuffer.put(mouseY2); // y2
    vertexBuffer.put(1.0f);    // red
    vertexBuffer.put(1.0f);    // green
    vertexBuffer.put(1.0f);    // blue
    vertexBuffer.put(1.0f);    // alpha
    vertexBuffer.put(0.0f);    // u1
    vertexBuffer.put(1.0f);    // v2
  }

  private void updateMousePosition() {
    // Shift the mouse texture show that its hotspot is lined up with the system hotspot.
    mouseX = (float) Gdx.input.getX() - hotspotX - 1;
    mouseY = (float) Gdx.input.getY() - hotspotY - 1;
    mouseX2 = mouseX + cursorWidth;
    mouseY2 = mouseY + cursorHeight;
  }

  private void updateVertexBuffer() {
    vertexBuffer.clear();
    vertexBuffer.put(0,  mouseX);  // vertex 0, x1
    vertexBuffer.put(1,  mouseY);  // vertex 0, y1
    vertexBuffer.put(8,  mouseX2); // vertex 1, x2
    vertexBuffer.put(9,  mouseY);  // vertex 1, y1
    vertexBuffer.put(16, mouseX2); // vertex 2, x2
    vertexBuffer.put(17, mouseY2); // vertex 2, y2
    vertexBuffer.put(24, mouseX);  // vertex 0, x1
    vertexBuffer.put(25, mouseY);  // vertex 0, y1
    vertexBuffer.put(32, mouseX2); // vertex 2, x2
    vertexBuffer.put(33, mouseY2); // vertex 2, x2
    vertexBuffer.put(40, mouseX);  // vertex 3, x1
    vertexBuffer.put(41, mouseY2); // vertex 3, y2
  }
}
