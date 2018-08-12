package de.lessvoid.nifty.html;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
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
    expect(textBuilderMock.text("huhu")).andReturn(textBuilderMock);
    expect(textBuilderMock.wrap(true)).andReturn(textBuilderMock);
    expect(textBuilderMock.alignLeft()).andReturn(textBuilderMock);
    expect(textBuilderMock.valignTop()).andReturn(textBuilderMock);
    expect(textBuilderMock.textHAlignLeft()).andReturn(textBuilderMock);
    expect(textBuilderMock.textVAlignTop()).andReturn(textBuilderMock);
    expect(textBuilderMock.font("fontname")).andReturn(textBuilderMock);
    expect(textBuilderMock.width("100%")).andReturn(textBuilderMock);
    replay(textBuilderMock);

    assertEquals(textBuilderMock, builderFactory.createTextBuilder("huhu", "fontname", null));
  }

  @Test
  public void testCreateTextBuilderWithoutColorWithNewLine() {
    expect(textBuilderMock.text("huhu\ntest")).andReturn(textBuilderMock);
    expect(textBuilderMock.wrap(true)).andReturn(textBuilderMock);
    expect(textBuilderMock.alignLeft()).andReturn(textBuilderMock);
    expect(textBuilderMock.valignTop()).andReturn(textBuilderMock);
    expect(textBuilderMock.textHAlignLeft()).andReturn(textBuilderMock);
    expect(textBuilderMock.textVAlignTop()).andReturn(textBuilderMock);
    expect(textBuilderMock.font("fontname")).andReturn(textBuilderMock);
    expect(textBuilderMock.width("100%")).andReturn(textBuilderMock);
    replay(textBuilderMock);

    assertEquals(textBuilderMock, builderFactory.createTextBuilder("huhu\ntest", "fontname", null));
  }
}
