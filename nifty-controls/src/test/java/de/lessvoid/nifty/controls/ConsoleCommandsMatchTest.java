package de.lessvoid.nifty.controls;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.controls.console.ConsoleControl;
import de.lessvoid.nifty.elements.Element;

public class ConsoleCommandsMatchTest {
  private ConsoleCommands consoleCommands;

  @Before
  public void before() {
    ConsoleCommands.ConsoleCommand command = new ConsoleCommands.ConsoleCommand() {
      @Override
      public void execute(@Nonnull final String[] args) {
      }
    };
    ConsoleControl console = new ConsoleControl() {
      @Override
      public TextField getTextField() {
        final Element elementMock = createNiceMock(Element.class);
        replay(elementMock);
        final TextField textfieldMock = createNiceMock(TextField.class);
        expect(textfieldMock.getElement()).andReturn(elementMock);
        replay(textfieldMock);
        return textfieldMock;
      }
    };
    consoleCommands = new ConsoleCommands(null, console);
    consoleCommands.registerCommand("abc", command);
    consoleCommands.registerCommand("abcde", command);
    consoleCommands.registerCommand("def a", command);
    consoleCommands.registerCommand("def b", command);
  }

  @Test
  public void testEmpty() {
    List<String> matches = consoleCommands.findMatches("");
    assertEquals(0, matches.size());
  }

  @Test
  public void testNotFound() {
    assertTrue(consoleCommands.findMatches("zzz").isEmpty());
  }

  @Test
  public void testDirectHit() {
    List<String> matches = consoleCommands.findMatches("abcde");
    assertEquals(1, matches.size());
    assertEquals("abcde", matches.get(0));
  }

  @Test
  public void testMultipleHits() {
    List<String> matches = consoleCommands.findMatches("a");
    assertEquals(2, matches.size());
    assertEquals("abc", matches.get(0));
    assertEquals("abcde", matches.get(1));
  }

  @Test
  public void testParameter() {
    List<String> matches = consoleCommands.findMatches("def");
    assertEquals(2, matches.size());
    assertEquals("def a", matches.get(0));
    assertEquals("def b", matches.get(1));
  }

  @Test
  public void testFindShortest() {
    List<String> bla = new ArrayList<String>();
    bla.add("abc d");
    bla.add("abc e");
    bla.add("abc efg");
    assertEquals("abc ", consoleCommands.findShortestMatch(bla));
  }

  @Test
  public void testFindShortestShort() {
    List<String> bla = new ArrayList<String>();
    bla.add("a");
    bla.add("abc e");
    bla.add("abc efg");
    assertEquals("a", consoleCommands.findShortestMatch(bla));
  }
}
