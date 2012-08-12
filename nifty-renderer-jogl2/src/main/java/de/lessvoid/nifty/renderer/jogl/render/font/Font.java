package de.lessvoid.nifty.renderer.jogl.render.font;

import java.util.Hashtable;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.renderer.jogl.render.JoglRenderImage;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.ColorValueParser;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * OpenGL display list based Font.
 * 
 * @author void
 */
public class Font {
    /**
     * the font reader.
     */
    private AngelCodeFont font;

    /**
     * display list id.
     */
    private int listId;

    /**
     * textures.
     */
    private JoglRenderImage[] textures;

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

    private NiftyResourceLoader resourceLoader;

    /**
     * construct the font.
     */
    public Font(final RenderDevice device, final NiftyResourceLoader resourceLoader) {
      this.resourceLoader = resourceLoader;
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

    /**
     * has selection.
     * 
     * @return true or false
     */
    private boolean isSelection() {
        return !(selectionStart == -1 && selectionEnd == -1);
    }

    /**
     * init the font with the given filename.
     * 
     * @param filename the filename
     * @return true, when success or false on error
     */
    public final boolean init(final String filename) {
        // get the angel code font from file
        font = new AngelCodeFont(resourceLoader);
        if (!font.load(filename)) {
            return false;
        }

        // load textures of font into array
        textures = new JoglRenderImage[font.getTextures().length];
        for (int i = 0; i < font.getTextures().length; i++) {
            textures[i] = new JoglRenderImage(extractPath(filename) + font.getTextures()[i], true, resourceLoader);
        }

        // now build open gl display lists for every character in the font
        initDisplayList();
        return true;
    }

    /**
     * extract the path from the given filename.
     * 
     * @param filename file
     * @return path
     */
    private String extractPath(final String filename) {
        int idx = filename.lastIndexOf("/");
        if (idx == -1) {
            return "";
        }
        else {
            return filename.substring(0, idx) + "/";
        }
    }

    /**
   *
   */
    private void initDisplayList() {
        displayListMap.clear();
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        // create new list
        listId = gl.glGenLists(font.getChars().size());
        Tools.checkGLError("glGenLists");

        // create the list
        int i = 0;
        for (Map.Entry<Character, CharacterInfo> entry : font.getChars().entrySet()) {
            displayListMap.put(entry.getKey(), listId + i);
            gl.glNewList(listId + i, GL2.GL_COMPILE);
            Tools.checkGLError("glNewList");
            CharacterInfo charInfo = entry.getValue();
            if (charInfo != null) {
                gl.glBegin(GL2.GL_QUADS);
                Tools.checkGLError("glBegin");

                float u0 = charInfo.getX() / (float) font.getWidth();
                float v0 = charInfo.getY() / (float) font.getHeight();
                float u1 = (charInfo.getX() + charInfo.getWidth()) / (float) font.getWidth();
                float v1 = (charInfo.getY() + charInfo.getHeight()) / (float) font.getHeight();

                gl.glTexCoord2f(u0, v0);
                gl.glVertex2f(charInfo.getXoffset(), charInfo.getYoffset());

                gl.glTexCoord2f(u0, v1);
                gl.glVertex2f(charInfo.getXoffset(), charInfo.getYoffset() + charInfo.getHeight());

                gl.glTexCoord2f(u1, v1);
                gl.glVertex2f(charInfo.getXoffset() + charInfo.getWidth(), charInfo.getYoffset()
                        + charInfo.getHeight());

                gl.glTexCoord2f(u1, v0);
                gl.glVertex2f(charInfo.getXoffset() + charInfo.getWidth(), charInfo.getYoffset());

                gl.glEnd();
                Tools.checkGLError("glEnd");
            }

            // end list
            gl.glEndList();
            Tools.checkGLError("glEndList");
            i++;
        }
    }

    /**
     * @param x
     * @param y
     * @param text
     */
    public void drawString(int x, int y, String text) {
        // enableBlend();
        internalRenderText(x, y, text, 1.0f, 1.0f, false, 1.0f);
        // disableBlend();
    }

    public void drawStringWithSize(int x, int y, String text, float sizeX, float sizeY) {
        // enableBlend();
        internalRenderText(x, y, text, sizeX, sizeY, false, 1.0f);
        // disableBlend();
    }

    public void renderWithSizeAndColor(int x, int y, String text, float sizeX, float sizeY, float r, float g,
            float b, float a) {
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        // enableBlend();
        gl.glColor4f(r, g, b, a);
        internalRenderText(x, y, text, sizeX, sizeY, false, a);
        // disableBlend();
    }

    /**
     * @param xPos x
     * @param yPos y
     * @param text text
     * @param size size
     * @param useAlphaTexture use alpha
     * @param a
     */
    private void internalRenderText(final int xPos, final int yPos, final String text,
            final float sizeX, final float sizeY, final boolean useAlphaTexture, final float alpha) {
        final GL2 gl = GLContext.getCurrentGL().getGL2();
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        float normHeightScale = getHeight() * sizeY;
        int x = xPos;
        int y = yPos;

        int activeTextureIdx = -1;

        for (int i = 0; i < text.length(); i++) {
            colorValueParser.isColor(text, i);
            while (colorValueParser.isColor()) {
                Color color = colorValueParser.getColor();
                gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                i = colorValueParser.getNextIndex();
                if (i >= text.length()) {
                    break;
                }
                colorValueParser.isColor(text, i);
            }
            if (i >= text.length()) {
                break;
            }

            char currentc = text.charAt(i);
            char nextc = FontHelper.getNextCharacter(text, i);
            CharacterInfo charInfoC = font.getChar(currentc);

            float characterWidth = 0;
            if (charInfoC != null) {
                int texId = charInfoC.getPage();
                if (activeTextureIdx != texId) {
                    activeTextureIdx = texId;
                    textures[activeTextureIdx].bind();
                }

                characterWidth = getCharacterWidth((char) currentc, nextc, sizeX);

                gl.glLoadIdentity();
                gl.glTranslatef(x, y, 0.0f);
                gl.glScalef(sizeX, sizeY, 1.0f);

                boolean characterDone = false;
                if (isSelection()) {
                    if (i >= selectionStart && i < selectionEnd) {
                        gl.glPushAttrib(GL2.GL_CURRENT_BIT);

                        disableBlend();
                        gl.glDisable(GL.GL_TEXTURE_2D);

                        gl.glColor4f(selectionBackgroundR, selectionBackgroundG,
                                selectionBackgroundB, selectionBackgroundA);
                        gl.glBegin(GL2.GL_QUADS);

                        gl.glVertex2i(0, 0);
                        gl.glVertex2i((int) characterWidth, 0);
                        gl.glVertex2i((int) characterWidth, (int) normHeightScale);
                        gl.glVertex2i(0, 0 + (int)normHeightScale);

                        gl.glEnd();
                        gl.glEnable(GL.GL_TEXTURE_2D);
                        enableBlend();

                        gl.glColor4f(selectionR, selectionG, selectionB, selectionA);
                        gl.glCallList(displayListMap.get(currentc));
                        Tools.checkGLError("glCallList");
                        gl.glPopAttrib();

                        characterDone = true;
                    }
                }

                if (!characterDone) {
                    gl.glCallList(displayListMap.get(currentc));
                    Tools.checkGLError("glCallList");
                }

                x += characterWidth;
            }
        }

        gl.glPopMatrix();
    }

    /**
   * 
   */
    private void disableBlend() {
        final GL gl = GLContext.getCurrentGL();
        gl.glDisable(GL.GL_BLEND);
    }

    /**
   * 
   */
    private void enableBlend() {
        final GL gl = GLContext.getCurrentGL();
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    }

    public int getStringWidth(final String text, final float size) {
      return getStringWidthInternal(text, size);
    }

    public int getStringWidth(String text) {
        return getStringWidthInternal(text, 1.0f);
    }

    /**
     * @param text text
     * @param size size
     * @return length
     */
    private int getStringWidthInternal(final String text, final float size) {
        int length = 0;

        for (int i = 0; i < text.length(); i++) {
            colorValueParser.isColor(text, i);
            if (colorValueParser.isColor()) {
                i = colorValueParser.getNextIndex();
                if (i >= text.length()) {
                    break;
                }
            }
            char currentCharacter = text.charAt(i);
            char nextCharacter = FontHelper.getNextCharacter(text, i);

            Integer w = getCharacterWidth(currentCharacter, nextCharacter, size);
            if (w != null) {
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

    public void setSelectionColor(final float selectionR, final float selectionG,
            final float selectionB, final float selectionA) {
        this.selectionR = selectionR;
        this.selectionG = selectionG;
        this.selectionB = selectionB;
        this.selectionA = selectionA;
    }

    public void setSelectionBackgroundColor(final float selectionR, final float selectionG,
            final float selectionB, final float selectionA) {
        this.selectionBackgroundR = selectionR;
        this.selectionBackgroundG = selectionG;
        this.selectionBackgroundB = selectionB;
        this.selectionBackgroundA = selectionA;
    }

    /**
     * get character information.
     * 
     * @param character char
     * @return CharacterInfo
     */
    public CharacterInfo getChar(final char character) {
        return font.getChar(character);
    }

    /**
     * Return the width of the given character including kerning information.
     * 
     * @param currentCharacter current character
     * @param nextCharacter next character
     * @param size font size
     * @return width of the character or null when no information for the character is available
     */
    public Integer getCharacterWidth(final char currentCharacter, final char nextCharacter,
            final float size) {
        CharacterInfo currentCharacterInfo = font.getChar(currentCharacter);
        if (currentCharacterInfo == null) {
            return null;
        }
        else {
            return Integer.valueOf((int) (currentCharacterInfo.getXadvance() * size + getKerning(
                    currentCharacterInfo, nextCharacter)));
        }
    }

    /**
     * @param charInfoC
     * @param nextc
     * @return
     */
    public static int getKerning(final CharacterInfo charInfoC, final char nextc) {
        Integer kern = charInfoC.getKerning().get(Character.valueOf(nextc));
        if (kern != null) {
            return kern.intValue();
        }
        return 0;
    }
}
