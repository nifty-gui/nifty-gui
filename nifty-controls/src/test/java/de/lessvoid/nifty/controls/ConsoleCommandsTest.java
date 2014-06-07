package de.lessvoid.nifty.controls;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.ConsoleCommands.ConsoleCommand;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.KeyInputHandler;

public class ConsoleCommandsTest {
  private ConsoleCommands consoleCommands;

  @Before
  public void before() {
    Element element = createMock(Element.class);
    element.addPreInputHandler(isA(KeyInputHandler.class));
    replay(element);

    TextField textfield = createMock(TextField.class);
    expect(textfield.getElement()).andReturn(element);
    replay(textfield);

    Console console = createMock(Console.class);
    expect(console.getTextField()).andReturn(textfield);
    replay(console);

    consoleCommands = new ConsoleCommands(null, console);

    verify(console);
    verify(textfield);
    verify(element);
  }

  @Test
  public void testNothingFound() {
    assertNull(consoleCommands.findCommand("testDirect"));
  }

  @Test
  public void testDirectHit() {
    ConsoleCommand command1 = createMock(ConsoleCommand.class);
    replay(command1);

    consoleCommands.registerCommand("testDirect", command1);
    assertEquals(command1, consoleCommands.findCommand("testDirect"));

    verify(command1);
  }

  @Test
  public void testBeginningHit() {
    ConsoleCommand command1 = createMock(ConsoleCommand.class);
    replay(command1);

    consoleCommands.registerCommand("testDirect", command1);
    assertEquals(command1, consoleCommands.findCommand("test"));

    verify(command1);
  }
}
