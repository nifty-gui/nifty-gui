package de.lessvoid.nifty.html;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.builder.TextBuilder;

public class NiftyBuilderFactoryTextBuilderTest {
  private TextBuilder textBuilderMock;
  private NiftyBuilderFactory builderFactory;

  @Before
  public void before() {
    textBuilderMock = createMock(TextBuilder.class);

    builderFactory = new NiftyBuilderFactory() {
      @Override
      public TextBuilder createTextBuilder() {
        return textBuilderMock;
      }
    };
  }

  @After
  public void after() {
    verify(textBuilderMock);
  }

  @Test
  public void testCreateTextBuilderWithoutColor() {
    textBuilderMock.text("huhu");
    textBuilderMock.wrap(true);
    textBuilderMock.alignLeft();
    textBuilderMock.valignTop();
    textBuilderMock.textHAlignLeft();
    textBuilderMock.textVAlignTop();
    textBuilderMock.font("fontname");
    textBuilderMock.width("100%");
    replay(textBuilderMock);

    assertEquals(textBuilderMock, builderFactory.createTextBuilder("huhu", "fontname", null));
  }

  @Test
  public void testCreateTextBuilderWithoutColorWithNewLine() {
    textBuilderMock.text("huhutest");
    textBuilderMock.wrap(true);
    textBuilderMock.alignLeft();
    textBuilderMock.valignTop();
    textBuilderMock.textHAlignLeft();
    textBuilderMock.textVAlignTop();
    textBuilderMock.font("fontname");
    textBuilderMock.width("100%");
    replay(textBuilderMock);

    assertEquals(textBuilderMock, builderFactory.createTextBuilder("huhu\ntest", "fontname", null));
  }
}
