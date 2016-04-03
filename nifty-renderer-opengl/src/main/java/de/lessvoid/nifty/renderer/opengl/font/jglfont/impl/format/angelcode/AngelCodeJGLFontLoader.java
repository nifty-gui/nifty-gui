package de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.angelcode;

import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLAbstractFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLBitmapFontData;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.impl.format.JGLFontLoader;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.spi.JGLFontRenderer;
import de.lessvoid.nifty.renderer.opengl.font.jglfont.spi.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A BitmapFontDataLoader implementation for AngelCode Font file.
 * @author void
 */
public class AngelCodeJGLFontLoader implements JGLFontLoader {
  private final static Logger log = Logger.getLogger(AngelCodeJGLFontLoader.class.getName());
  private final AngelCodeLineParser parser = new AngelCodeLineParser();
  private final AngelCodeLineData parsed = new AngelCodeLineData();
  private final AngelCodeLineProcessors lineProcessors;

  public AngelCodeJGLFontLoader(final AngelCodeLineProcessors lineProcessors) {
    this.lineProcessors = lineProcessors;
  }

  public JGLAbstractFontData load(
          final JGLFontRenderer renderer,
          final ResourceLoader resourceLoader,
          final InputStream in,
          final String filename,
          final int size,
          final int style,
          String params
  ) throws IOException {
    JGLAbstractFontData result = new JGLBitmapFontData(renderer, resourceLoader, filename);
    load(in, result);
    result.init();
    return result;
  }

  private void load(final InputStream in, final JGLAbstractFontData bitmapFont) throws IOException {
    if (in == null) {
      throw new IOException("InputStream is null");
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    try {
      while (true) {
        String line = reader.readLine();
        if (line == null) {
          break;
        }

        String[] split = line.split(" ");
        if (split[0].length() == 0) {
          break;
        }

        parser.parse(line, parsed);

        AngelCodeLine processor = lineProcessors.get(split[0]);
        if (processor != null) {
          if (!processor.process(parsed, bitmapFont)) {
            log.warning("parsing error for line [" + line + "] using " + processor + " with " + parsed);
          }
        }
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "error while parsing font file: ", e);
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
      }
    }
  }
}
