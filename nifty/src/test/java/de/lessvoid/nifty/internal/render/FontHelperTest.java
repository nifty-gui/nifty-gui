package de.lessvoid.nifty.internal.render;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.NiftyFont;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

public class FontHelperTest {
  private FontHelper fontHelper = new FontHelper();
  private NiftyFont font;

  @Before
  public void before() {
    font = createMock(NiftyFont.class);
    expect(font.getCharacterWidth(anyChar(), anyChar(), anyFloat())).andReturn(10).anyTimes();
    replay(font);
  }

  @After
  public void after() {
    verify(font);
  }

  @Test
  public void testHitFirstChar() {
    assertEquals(0, fontHelper.getVisibleCharactersFromStart(font, "Hello", 0, 1.f));
  }

  @Test
  public void testHitFirstCharInBetween() {
    assertEquals(0, fontHelper.getVisibleCharactersFromStart(font, "Hello", 5, 1.f));
  }

  @Test
  public void testHitSecondChar() {
    assertEquals(1, fontHelper.getVisibleCharactersFromStart(font, "Hello", 10, 1.f));
  }

  @Test
  public void testHitLastChar() {
    assertEquals(5, fontHelper.getVisibleCharactersFromStart(font, "Hello", 1000, 1.f));
  }

}
