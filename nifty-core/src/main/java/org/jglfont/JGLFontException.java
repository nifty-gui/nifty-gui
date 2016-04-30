package org.jglfont;


public class JGLFontException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public JGLFontException(final String message) {
    super(message);
  }

  public JGLFontException(final Exception e) {
    super (e);
  }
}
