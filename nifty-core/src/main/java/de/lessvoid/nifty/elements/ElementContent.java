package de.lessvoid.nifty.elements;

import java.util.logging.Logger;

import de.lessvoid.coregl.MatrixFactory;
import de.lessvoid.coregl.RenderToTexture;
import de.lessvoid.coregl.Shader;
import de.lessvoid.coregl.VBO;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.tools.Color;

/**
 * The visible representation of an element.
 */
public class ElementContent {
  private Logger log = Logger.getLogger(ElementContent.class.getName());

  /**
   * The actual content of the element.
   */
  private RenderToTexture texture;

  /**
   * The actual background color.
   */
  private Color background = Color.NONE();
  private boolean needsRedraw = true;

  /**
   * The position of the element on screen in absolute coordinates.
   */
  private final Box pos = new Box();
  private VBO content;

  /**
   * Initialize this content and fix its position on screen.
   * @param Box source
   */
  public void initialize(final Box box) {
    this.pos.from(box);
    this.texture = new RenderToTexture(this.pos.getWidth(), this.pos.getHeight());
    this.content = VBO.createStatic(createQuad());
  }

  private float[] createQuad() {
    return new float[] {
        0.f,                  0.f,                   0.f, 0.f,
        0.f + pos.getWidth(), 0.f,                   1.f, 0.f,
        0.f,                  0.f + pos.getHeight(), 0.f, 1.f,
        0.f + pos.getWidth(), 0.f + pos.getHeight(), 1.f, 1.f,
    };
  }

  public void render(final Shader shader) {
    redraw();
    texture.bindTexture();
  }

  private void redraw() {
    if (!needsRedraw) {
      return;
    }

    log.info("redrawing content");

    texture.on();
    texture.off();

    needsRedraw = false;
  }

  public void setBackgroundColor(final Color color) {
    background.update(color);
  }
}
