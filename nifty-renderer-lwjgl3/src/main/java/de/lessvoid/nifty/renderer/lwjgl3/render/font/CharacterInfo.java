package de.lessvoid.nifty.renderer.lwjgl3.render.font;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * info of an individual character.
 *
 * @author void
 */
public class CharacterInfo {
  /**
   * id.
   */
  private int id;

  /**
   * x position.
   */
  private int x;

  /**
   * y position.
   */
  private int y;

  /**
   * width.
   */
  private int width;

  /**
   * height.
   */
  private int height;

  /**
   * xoffset.
   */
  private int xoffset;

  /**
   * yoffset.
   */
  private int yoffset;

  /**
   * xadvance.
   */
  private int xadvance;

  /**
   * page.
   */
  private int page;

  /**
   * kerning information.
   */
  @Nonnull
  private Map<Character, Integer> kerning = new HashMap<Character, Integer>();

  /**
   * set id.
   *
   * @param newId the id
   */
  public final void setId(final int newId) {
    this.id = newId;
  }

  /**
   * get id.
   *
   * @return the id
   */
  public final int getId() {
    return id;
  }

  /**
   * set x.
   *
   * @param newX new x to set
   */
  public final void setX(final int newX) {
    this.x = newX;
  }

  /**
   * get x.
   *
   * @return the x
   */
  public final int getX() {
    return x;
  }

  /**
   * set y.
   *
   * @param newY new y
   */
  public final void setY(final int newY) {
    this.y = newY;
  }

  /**
   * get y.
   *
   * @return get y
   */
  public final int getY() {
    return y;
  }

  /**
   * set width.
   *
   * @param newWidth new width
   */
  public final void setWidth(final int newWidth) {
    this.width = newWidth;
  }

  /**
   * get width.
   *
   * @return the width.
   */
  public final int getWidth() {
    return width;
  }

  /**
   * set height.
   *
   * @param newHeight new height
   */
  public final void setHeight(final int newHeight) {
    this.height = newHeight;
  }

  /**
   * get height.
   *
   * @return height
   */
  public final int getHeight() {
    return height;
  }

  /**
   * set xoffset.
   *
   * @param newxoffset the new xoffset
   */
  public final void setXoffset(final int newxoffset) {
    this.xoffset = newxoffset;
  }

  /**
   * get xoffset.
   *
   * @return xoffset
   */
  public final int getXoffset() {
    return xoffset;
  }

  /**
   * set yoffset.
   *
   * @param newyoffset new yoffset
   */
  public final void setYoffset(final int newyoffset) {
    this.yoffset = newyoffset;
  }

  /**
   * get yoffset.
   *
   * @return yoffset
   */
  public final int getYoffset() {
    return yoffset;
  }

  /**
   * set xadvance.
   *
   * @param newXadvance
   */
  public final void setXadvance(final int newXadvance) {
    this.xadvance = newXadvance;
  }

  /**
   * get xadvance.
   *
   * @return xadvance
   */
  public final int getXadvance() {
    return xadvance;
  }

  /**
   * set page.
   *
   * @param newPage the new page
   */
  public final void setPage(final int newPage) {
    this.page = newPage;
  }

  /**
   * get page.
   *
   * @return the page
   */
  public final int getPage() {
    return page;
  }

  /**
   * set kerning.
   *
   * @param newKerning the kerning to set
   */
  public final void setKerning(@Nonnull final Map<Character, Integer> newKerning) {
    kerning.clear();
    kerning.putAll(newKerning);
  }

  /**
   * get kerning.
   *
   * @return the kerning
   */
  public final Map<Character, Integer> getKerning() {
    return kerning;
  }
}
