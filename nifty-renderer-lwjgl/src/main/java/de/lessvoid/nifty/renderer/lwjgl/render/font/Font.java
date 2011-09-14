package de.lessvoid.nifty.renderer.lwjgl.render.font;

import java.util.Hashtable;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderFont;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderImage;
import de.lessvoid.nifty.renderer.lwjgl.render.font.ColorValueParser.Result;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.tools.Color;

/**
 * OpenGL display list based Font.
 * 
 * @author void
 */
public class Font {
  private AngelCodeFont font;
  private int listId;
  private LwjglRenderImage[] textures;
  private int selectionStart;
  private int selectionEnd;

  private float selectionBackgroundR;
  private float selectionBackgroundG;
  private float selectionBackgroundB;
  private float selectionBackgroundA;

  private float selectionR;
  private float selectionG;
  private float selectionB;
  private float selectionA;

  private Map<Character, Integer> displayListMap = new Hashtable<Character, Integer>();
  private ColorValueParser colorValueParser = new ColorValueParser();

  public Font(final RenderDevice device) {
    selectionStart = -1;
    selectionEnd = -1;
    selectionR = 1.0f;
    selectionG = 0.0f;
    selectionB = 0.0f;
    selectionA = 1.0f;
    selectionBackgroundR = 0.0f;
    selectionBackgroundG = 1.0f;
    selectionBackgroundB = 0.0f;
    selectionBackgroundA = 1.0f;
  }

  private boolean isSelection() {
    return !(selectionStart == -1 && selectionEnd == -1);
  }

  public boolean init(final String filename) {
    // get the angel code font from file
    font = new AngelCodeFont();
    if (!font.load(filename)) {
      return false;
    }

    // load textures of font into array
    textures = new LwjglRenderImage[font.getTextures().length];
    for (int i = 0; i < font.getTextures().length; i++) {
      textures[i] = new LwjglRenderImage(extractPath(filename) + font.getTextures()[i], true);
    }

    // now build open gl display lists for every character in the font
    initDisplayList();
    return true;
  }

  private String extractPath(final String filename) {
    int idx = filename.lastIndexOf("/");
    if (idx == -1) {
      return "";
    } else {
      return filename.substring(0, idx) + "/";
    }
  }

  private void initDisplayList() {
    displayListMap.clear();

    // create new list
    listId = GL11.glGenLists(font.getChars().size());
    Tools.checkGLError("glGenLists");

    // create the list
    int i = 0;
    for (Map.Entry<Character, CharacterInfo> entry : font.getChars().entrySet()) {
      displayListMap.put(entry.getKey(), listId + i);
      GL11.glNewList(listId + i, GL11.GL_COMPILE);
      Tools.checkGLError("glNewList");
      CharacterInfo charInfo = entry.getValue();
      if (charInfo != null) {
        GL11.glBegin(GL11.GL_QUADS);
        Tools.checkGLError("glBegin");

        float u0 = charInfo.getX() / (float) font.getWidth();
        float v0 = charInfo.getY() / (float) font.getHeight();
        float u1 = (charInfo.getX() + charInfo.getWidth()) / (float) font.getWidth();
        float v1 = (charInfo.getY() + charInfo.getHeight()) / (float) font.getHeight();

        GL11.glTexCoord2f(u0, v0);
        GL11.glVertex2f(charInfo.getXoffset(), charInfo.getYoffset());

        GL11.glTexCoord2f(u0, v1);
        GL11.glVertex2f(charInfo.getXoffset(), charInfo.getYoffset() + charInfo.getHeight());

        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(charInfo.getXoffset() + charInfo.getWidth(), charInfo.getYoffset() + charInfo.getHeight());

        GL11.glTexCoord2f(u1, v0);
        GL11.glVertex2f(charInfo.getXoffset() + charInfo.getWidth(), charInfo.getYoffset());

        GL11.glEnd();
        Tools.checkGLError("glEnd");
      }

      // end list
      GL11.glEndList();
      Tools.checkGLError("glEndList");
      i++;
    }
  }

  public void drawString(int x, int y, String text) {
    internalRenderText(x, y, text, 1.0f, false, 1.0f);
  }

  public void drawStringWithSize(int x, int y, String text, float size) {
    internalRenderText(x, y, text, size, false, 1.0f);
  }

  public void renderWithSizeAndColor(int x, int y, String text, float size, float r, float g, float b, float a) {
    GL11.glColor4f(r, g, b, a);
    internalRenderText(x, y, text, size, false, a);
  }

  private void internalRenderText(
      final int xPos,
      final int yPos,
      final String text,
      final float size,
      final boolean useAlphaTexture,
      final float alpha) {
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glPushMatrix();
    GL11.glLoadIdentity();

    int originalWidth = getStringWidthInternal(text, 1.0f);
    int sizedWidth = getStringWidthInternal(text, size);
    int x = xPos - (sizedWidth - originalWidth) / 2;

    int activeTextureIdx = -1;

    for (int i = 0; i < text.length(); i++) {
      Result result = colorValueParser.isColor(text, i);
      while (result.isColor()) {
        Color color = result.getColor();
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        i = result.getNextIndex();
        if (i >= text.length()) {
          break;
        }
        result = colorValueParser.isColor(text, i);
      }
      if (i >= text.length()) {
        break;
      }

      char currentc = text.charAt(i);
      char nextc = FontHelper.getNextCharacter(text, i);
      CharacterInfo charInfoC = font.getChar((char) currentc);
      int kerning = 0;
      float characterWidth = 0;
      if (charInfoC != null) {
        int texId = charInfoC.getPage();
        if (activeTextureIdx != texId) {
          activeTextureIdx = texId;
          textures[activeTextureIdx].bind();
        }

        kerning = LwjglRenderFont.getKerning(charInfoC, nextc);
        characterWidth = (float) (charInfoC.getXadvance() * size + kerning);

        GL11.glLoadIdentity();
        GL11.glTranslatef(x, yPos, 0.0f);

        GL11.glTranslatef(0.0f, getHeight() / 2, 0.0f);
        GL11.glScalef(size, size, 1.0f);
        GL11.glTranslatef(0.0f, -getHeight() / 2, 0.0f);

        boolean characterDone = false;
        if (isSelection()) {
          if (i >= selectionStart && i < selectionEnd) {
            GL11.glPushAttrib(GL11.GL_CURRENT_BIT);

            disableBlend();
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            GL11.glColor4f(selectionBackgroundR, selectionBackgroundG, selectionBackgroundB, selectionBackgroundA);
            GL11.glBegin(GL11.GL_QUADS);

            GL11.glVertex2i(0, 0);
            GL11.glVertex2i((int) characterWidth, 0);
            GL11.glVertex2i((int) characterWidth, 0 + getHeight());
            GL11.glVertex2i(0, 0 + getHeight());

            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            enableBlend();

            GL11.glColor4f(selectionR, selectionG, selectionB, selectionA);
            GL11.glCallList(displayListMap.get(currentc));
            Tools.checkGLError("glCallList");
            GL11.glPopAttrib();

            characterDone = true;
          }
        }

        if (!characterDone) {
          GL11.glCallList(displayListMap.get(currentc));
          Tools.checkGLError("glCallList");
        }

        x += characterWidth;
      }
    }

    GL11.glPopMatrix();
  }

  private void disableBlend() {
    GL11.glDisable(GL11.GL_BLEND);
  }

  private void enableBlend() {
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  }

  public int getStringWidth(String text) {
    return getStringWidthInternal(text, 1.0f);
  }

  private int getStringWidthInternal(final String text, final float size) {
    int length = 0;

    for (int i = 0; i < text.length(); i++) {
      Result result = colorValueParser.isColor(text, i);
      if (result.isColor()) {
        i = result.getNextIndex();
        if (i >= text.length()) {
          break;
        }
      }
      char currentCharacter = text.charAt(i);
      char nextCharacter = FontHelper.getNextCharacter(text, i);

      int w = getCharacterWidth(currentCharacter, nextCharacter, size);
      if (w != -1) {
        length += w;
      }
    }
    return length;
  }

  public int getHeight() {
    return font.getLineHeight();
  }

  public void setSelectionStart(int selectionStart) {
    this.selectionStart = selectionStart;
  }

  public void setSelectionEnd(int selectionEnd) {
    this.selectionEnd = selectionEnd;
  }

  public void setSelectionColor(
      final float selectionR,
      final float selectionG,
      final float selectionB,
      final float selectionA) {
    this.selectionR = selectionR;
    this.selectionG = selectionG;
    this.selectionB = selectionB;
    this.selectionA = selectionA;
  }

  public void setSelectionBackgroundColor(
      final float selectionR,
      final float selectionG,
      final float selectionB,
      final float selectionA) {
    this.selectionBackgroundR = selectionR;
    this.selectionBackgroundG = selectionG;
    this.selectionBackgroundB = selectionB;
    this.selectionBackgroundA = selectionA;
  }

  public CharacterInfo getChar(final char character) {
    return font.getChar(character);
  }

  public int getCharacterWidth(final char currentCharacter, final char nextCharacter, final float size) {
    CharacterInfo currentCharacterInfo = font.getChar(currentCharacter);
    if (currentCharacterInfo == null) {
      return -1;
    } else {
      return (int) (currentCharacterInfo.getXadvance() * size + getKerning(currentCharacterInfo, nextCharacter));
    }
  }

  public static int getKerning(final CharacterInfo charInfoC, final char nextc) {
    Integer kern = charInfoC.getKerning().get(Character.valueOf(nextc));
    if (kern != null) {
      return kern.intValue();
    }
    return 0;
  }
}
