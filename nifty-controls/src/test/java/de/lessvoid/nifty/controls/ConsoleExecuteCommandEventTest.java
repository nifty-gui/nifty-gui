package de.lessvoid.nifty.controls;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ConsoleExecuteCommandEventTest {
  @Test
  public void testCommandLineNull() {
    ConsoleExecuteCommandEvent command = new ConsoleExecuteCommandEvent(null, null);
    assertEquals("", command.getCommandLine());
    assertEquals("", command.getCommand());
    assertEquals(0, command.getArgumentCount());
    assertEquals(0, command.getArguments().length);
  }

  @Test
  public void testCommandLineEmpty() {
    ConsoleExecuteCommandEvent command = new ConsoleExecuteCommandEvent(null, "");
    assertEquals("", command.getCommandLine());
    assertEquals("", command.getCommand());
    assertEquals(0, command.getArgumentCount());
    assertEquals(0, command.getArguments().length);
  }

  @Test
  public void testSimpleCommandLine() {
    ConsoleExecuteCommandEvent command = new ConsoleExecuteCommandEvent(null, "exec");
    assertEquals("exec", command.getCommandLine());
    assertEquals("exec", command.getCommand());
    assertEquals(0, command.getArgumentCount());
    assertEquals(0, command.getArguments().length);
  }

  @Test
  public void testOneParameterCommandLine() {
    ConsoleExecuteCommandEvent command = new ConsoleExecuteCommandEvent(null, "exec hello");
    assertEquals("exec hello", command.getCommandLine());
    assertEquals("exec", command.getCommand());
    assertEquals(1, command.getArgumentCount());
    assertEquals(1, command.getArguments().length);
    assertEquals("hello", command.getArguments()[0]);
  }

  @Test
  public void testMultipleParameterCommandLine() {
    ConsoleExecuteCommandEvent command = new ConsoleExecuteCommandEvent(null, "exec hello 42 43 tra");
    assertEquals("exec hello 42 43 tra", command.getCommandLine());
    assertEquals("exec", command.getCommand());
    assertEquals(4, command.getArgumentCount());
    assertEquals(4, command.getArguments().length);
    assertEquals("hello", command.getArguments()[0]);
    assertEquals("42", command.getArguments()[1]);
    assertEquals("43", command.getArguments()[2]);
    assertEquals("tra", command.getArguments()[3]);
  }

  @Test
  public void testMultipleStringParameterCommandLine() {
    ConsoleExecuteCommandEvent command = new ConsoleExecuteCommandEvent(null, "exec 'hello escaped whitespace' 42 43 " +
        "\"hello quoted parameter\"");
    assertEquals("exec 'hello escaped whitespace' 42 43 \"hello quoted parameter\"", command.getCommandLine());
    assertEquals("exec", command.getCommand());
    assertEquals(4, command.getArgumentCount());
    assertEquals(4, command.getArguments().length);
    assertEquals("hello escaped whitespace", command.getArguments()[0]);
    assertEquals("42", command.getArguments()[1]);
    assertEquals("43", command.getArguments()[2]);
    assertEquals("hello quoted parameter", command.getArguments()[3]);
  }

}
