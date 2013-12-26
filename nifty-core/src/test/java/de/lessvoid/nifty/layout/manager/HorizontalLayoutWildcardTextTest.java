package de.lessvoid.nifty.layout.manager;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.loaderv2.types.apply.ApplyRenderText;
import de.lessvoid.nifty.loaderv2.types.apply.Convert;
import de.lessvoid.nifty.render.NiftyRenderEngine;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class HorizontalLayoutWildcardTextTest {
  private Element root;
  private Element a;
  private Element b;
  private final String line1 = "Abc";
  private final String line2 = "Defg hijklm 12345 6789";
  private final String line3 = "nopqrstuvwxyz0123";

  @Before
  public void before() {
    Nifty niftyMock = createNiceMock(Nifty.class);
    RenderFont font = new RenderFont() {

      @Override
      public int getWidth(@Nonnull String text) {
        return text.length() * 10;
      }

      @Override
      public int getWidth(@Nonnull String text, float size) {
        return text.length() * 10;
      }

      @Override
      public int getHeight() {
        return 20;
      }

      @Override
      public int getCharacterAdvance(char currentCharacter, char nextCharacter, float size) {
        return 100000;
      }

      @Override
      public void dispose() {
      }
    };
    NiftyRenderEngine renderMock = createNiceMock(NiftyRenderEngine.class);
    expect(renderMock.getFont()).andReturn(font).anyTimes();
    replay(renderMock);

    String text = line1 + "\n" + line2 + "\n" + line3;
    expect(niftyMock.specialValuesReplace(text)).andReturn(text).anyTimes();
    expect(niftyMock.specialValuesReplace(null)).andReturn(text).anyTimes();
    expect(niftyMock.getRenderEngine()).andReturn(renderMock).anyTimes();
    replay(niftyMock);

    root = new Element(niftyMock, null, null, null, null, false, null);
    root.setConstraintWidth(SizeValue.px(150));
    root.setWidth(150);
    a = new Element(niftyMock, null, null, null, null, false, null);
    b = new Element(niftyMock, null, null, null, null, false, null, new TextRenderer(niftyMock, font, text));

    new ApplyRenderText(new Convert()).apply(null, b, new Attributes(), renderMock);

    root.setLayoutManager(new HorizontalLayout());
  }

  @Test
  public void testAbsoluteWidthNoWrap() {
    b.getRenderer(TextRenderer.class).setLineWrapping(false);
    b.setConstraintWidth(SizeValue.px(100));
    root.addChild(b);
    root.layoutElements();

    assertEquals(20 * 3, b.getConstraintHeight().getValueAsInt(0));
    assertEquals(20 * 3, root.getConstraintHeight().getValueAsInt(0));
  }

  @Test
  public void testAbsoluteWidthWrap() {
    b.getRenderer(TextRenderer.class).setLineWrapping(true);
    b.setConstraintWidth(SizeValue.px(100));
    root.addChild(b);
    root.layoutElements();

    assertEquals(20 * 7, b.getConstraintHeight().getValueAsInt(0));
    assertEquals(20 * 7, root.getConstraintHeight().getValueAsInt(0));
  }

  @Test
  public void testWildcardWidthNoWrap() {
    b.getRenderer(TextRenderer.class).setLineWrapping(false);
    a.setConstraintWidth(SizeValue.px(50));
    a.setConstraintHeight(SizeValue.px(20));
    a.setWidth(50);
    a.setHeight(20);
    b.setConstraintWidth(SizeValue.wildcard());

    root.addChild(a);
    root.addChild(b);
    root.layoutElements();

    assertEquals(20 * 3, b.getConstraintHeight().getValueAsInt(0));
    assertEquals(20 * 3, root.getConstraintHeight().getValueAsInt(0));
  }

  @Test
  public void testWildcardWidthWrap() {
    b.getRenderer(TextRenderer.class).setLineWrapping(true);
    a.setConstraintWidth(SizeValue.px(50));
    a.setConstraintHeight(SizeValue.px(20));
    a.setWidth(50);
    a.setHeight(20);
    b.setConstraintWidth(SizeValue.wildcard());

    root.addChild(a);
    root.addChild(b);
    root.layoutElements();

    assertEquals(20 * 7, b.getConstraintHeight().getValueAsInt(0));
    assertEquals(20 * 7, root.getConstraintHeight().getValueAsInt(0));
  }
}
