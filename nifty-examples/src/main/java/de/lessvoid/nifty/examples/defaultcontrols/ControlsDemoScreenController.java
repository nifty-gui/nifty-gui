package de.lessvoid.nifty.examples.defaultcontrols;

import java.util.*;
import java.util.logging.Logger;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleCommands;
import de.lessvoid.nifty.controls.ConsoleCommands.ConsoleCommand;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.effects.Effect;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.impl.Move;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.examples.resolution.ResolutionControl;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;

public class ControlsDemoScreenController<T> implements ScreenController, KeyInputHandler {
  private static Logger logger = Logger.getLogger(ControlsDemoScreenController.class.getName());
  private static final Color HELP_COLOR = new Color("#aaaf");

  private Nifty nifty;
  private Screen screen;
  private Element consolePopup;
  private Element creditsPopup;
  private Console console;
  private ConsoleCommands consoleCommands;

  private final ResolutionControl<T> resControl;

  // This simply maps the IDs of the MenuButton elements to the corresponding Dialog elements we'd
  // like to show with the given MenuButton. This map will make our life a little bit easier when
  // switching between the menus.
  private Map<String, String> buttonToDialogMap = new Hashtable<String, String>();

  // We keep all the button IDs in this list so that we can later decide if an index is before or
  // after the current button.
  private List<String> buttonIdList = new ArrayList<String>();

  // This keeps the current menu button
  private String currentMenuButtonId;

  public ControlsDemoScreenController(final ResolutionControl<T> resolutionControl, final String ... mapping) {
    resControl = resolutionControl;
    if (mapping == null || mapping.length == 0 || mapping.length % 2 != 0) {
      logger.warning("expecting pairs of values that map menuButton IDs to dialog IDs");
    } else {
      for (int i=0; i<mapping.length/2; i++) {
        String menuButtonId = mapping[i*2+0];
        String dialogId = mapping[i*2+1];
        buttonToDialogMap.put(menuButtonId, dialogId);
        buttonIdList.add(menuButtonId);
        if (i == 0) {
          currentMenuButtonId = menuButtonId;
        }
      }
    }
  }

  @Override
  public void bind(final Nifty nifty, final Screen screen) {
    this.nifty = nifty;
    this.screen = screen;

    this.creditsPopup = nifty.createPopup("creditsPopup");
    this.consolePopup = nifty.createPopup("consolePopup");
    this.console = this.consolePopup.findNiftyControl("console", Console.class);
    this.console.output("Humble Nifty Console Demonstration :)\nYou can toggle the console on/off with the F1 key\nEnter 'help' to show all available commands");

    // this is not required when you only want to use the simple console
    // but when you want some support for commands this is how
    consoleCommands = new ConsoleCommands(nifty, console);

    ConsoleCommand showCommand = new ShowCommand();
    consoleCommands.registerCommand("show ListBox", showCommand);
    consoleCommands.registerCommand("show DropDown", showCommand);
    consoleCommands.registerCommand("show TextField", showCommand);
    consoleCommands.registerCommand("show Slider", showCommand);
    consoleCommands.registerCommand("show ScrollPanel", showCommand);
    consoleCommands.registerCommand("show ChatControl", showCommand);
    consoleCommands.registerCommand("show DragAndDrop", showCommand);
    consoleCommands.registerCommand("show EventConsume", showCommand);

    NiftyCommand niftyCommand = new NiftyCommand();
    consoleCommands.registerCommand("nifty screen", niftyCommand);

    ConsoleCommand helpCommand = new HelpCommand();
    consoleCommands.registerCommand("help", helpCommand);

    ConsoleCommand clearCommand = new ClearCommand();
    consoleCommands.registerCommand("clear", clearCommand);

    ConsoleCommand exitCommand = new ExitCommand();
    consoleCommands.registerCommand("exit", exitCommand);

    // enable the nifty command line completion
    consoleCommands.enableCommandCompletion(true);

    // get all resolutions available into the resolutions drop down
    fillResolutionDropDown(screen);
  }

  @Override
  public void onStartScreen() {
    screen.findElementByName(buttonToDialogMap.get(currentMenuButtonId)).show();
    screen.findElementByName(currentMenuButtonId).startEffect(EffectEventId.onCustom, null, "selected");
  }

  @Override
  public void onEndScreen() {
  }

  @Override
  public boolean keyEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyStandardInputEvent.ConsoleToggle) {
      if (screen.isActivePopup(consolePopup)) {
        nifty.closePopup(consolePopup.getId());
      } else {
        nifty.showPopup(screen, consolePopup.getId(), null);
      }
      return true;
    }
    return false;
  }

  public void openLink(final String url) {
    if (!java.awt.Desktop.isDesktopSupported()) {
      System.err.println("Desktop is not supported (Can't open link)");
      return;
    }

    java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
    if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
      System.err.println("Desktop (BROWSE) is not supported (Can't open link)");
      return;
    }

    try {
      java.net.URI uri = new java.net.URI(url);
      desktop.browse(uri);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void modifyMoveEffect(final EffectEventId effectEventId, final Element element, final String direction) {
    List<Effect> moveEffects = element.findElementByName("#effectPanel").getEffects(effectEventId, Move.class);
    if (!moveEffects.isEmpty()) {
      moveEffects.get(0).getParameters().put("direction", direction);
    }
  }

  @NiftyEventSubscriber(pattern="menuButton.*")
  public void onMenuButtonListBoxClick(final String id, final NiftyMousePrimaryClickedEvent clickedEvent) {
    if ("menuButtonCredits".equals(id)) {
      showCredits();
      return;
    }
    changeDialogTo(id);
  }

  private void showCredits() {
    nifty.showPopup(screen, creditsPopup.getId(), null);
  }

  @NiftyEventSubscriber(id="creditsBack")
  public void onCreditsBackClick(final String id, final ButtonClickedEvent event) {
    nifty.closePopup(creditsPopup.getId());
  }

  @NiftyEventSubscriber(id="resetScreenButton")
  public void onTestButtonClick(final String id, final ButtonClickedEvent clickedEvent) {
    screen.findElementByName(buttonToDialogMap.get(currentMenuButtonId)).hide(new EndNotify() {
      @Override
      public void perform() {
        nifty.gotoScreen("demo");
      }
    });
  }

  private void changeDialogTo(final String id) {
    if (!id.equals(currentMenuButtonId)) {
      int currentIndex = buttonIdList.indexOf(currentMenuButtonId);
      int nextIndex = buttonIdList.indexOf(id);

      Element nextElement = screen.findElementByName(buttonToDialogMap.get(id));
      modifyMoveEffect(EffectEventId.onShow, nextElement, currentIndex < nextIndex ? "right" : "left");
      nextElement.show();

      Element currentElement = screen.findElementByName(buttonToDialogMap.get(currentMenuButtonId));
      modifyMoveEffect(EffectEventId.onHide, currentElement, currentIndex < nextIndex ? "left" : "right");
      currentElement.hide();

      screen.findElementByName(currentMenuButtonId).stopEffect(EffectEventId.onCustom);
      screen.findElementByName(id).startEffect(EffectEventId.onCustom, null, "selected");
      currentMenuButtonId = id;
    }
  }

  @NiftyEventSubscriber(id="console")
  public void onConsoleEvent(final String id, final ConsoleExecuteCommandEvent executeCommandEvent) {
    System.out.println(executeCommandEvent.getCommandLine());
  }

  @NiftyEventSubscriber(id="resolutions")
  public void onResolution(final String id, final DropDownSelectionChangedEvent<T> event) {
    screen.findElementByName("whiteOverlay").startEffect(EffectEventId.onCustom, new EndNotify() {
      @Override
      public void perform() {
        resControl.setResolution(event.getSelection());
        nifty.enableAutoScaling(1024, 768);
        nifty.resolutionChanged();
      }
    }, "onResolutionStart");
  }

  @NiftyEventSubscriber(id="scale-resolution")
  public void onSliderChanged(final String id, final SliderChangedEvent sliderChangedEvent) {
    nifty.enableAutoScaling(1024, 768, sliderChangedEvent.getValue(), sliderChangedEvent.getValue());
  }

  /**
   * Get all LWJGL DisplayModes into the DropDown
   * @param screen
   */
  private void fillResolutionDropDown(final Screen screen) {
    DropDown<T> dropDown = screen.findNiftyControl("resolutions", DropDown.class);
    final Collection<T> resolutions = resControl.getResolutions();
    for (T mode : resolutions) {
      dropDown.addItem(mode);
    }
  }

  private class ShowCommand implements ConsoleCommand {
    @Override
    public void execute(final String[] args) {
      if (args.length != 2) {
        console.outputError("command argument error");
        return;
      }
      // this really is a hack to get from the command argument, like: "ListBox" to the matching "menuButtonId" 
      String menuButtonId = "menuButton" + args[1];
      if (!buttonToDialogMap.containsKey(menuButtonId)) {
        console.outputError("'" + menuButtonId + "' is not a registered dialog.");
        return;
      }

      // just a gimmick
      if (menuButtonId.equals(currentMenuButtonId)) {
        console.outputError("Hah! Already there! I'm smart... :>");
        return;
      }

      // finally switch
      changeDialogTo(menuButtonId);
    }
  }

  private class NiftyCommand implements ConsoleCommand {
    @Override
    public void execute(final String[] args) {
      if (args.length != 2) {
        console.outputError("command argument error");
        return;
      }
      String param = args[1];
      if ("screen".equals(param)) {
        String screenDebugOutput = nifty.getCurrentScreen().debugOutput();
        console.output(screenDebugOutput);
        System.out.println(screenDebugOutput);
      } else {
        console.outputError("unknown parameter [" + args[1] + "]");
      }
    }
  }

  private class HelpCommand implements ConsoleCommand {
    @Override
    public void execute(final String[] args) {
      console.output("---------------------------", HELP_COLOR);
      console.output("Supported commands", HELP_COLOR);
      console.output("---------------------------", HELP_COLOR);
      for (String command : consoleCommands.getRegisteredCommands()) {
        console.output(command, HELP_COLOR);
      }
    }
  }

  private class ExitCommand implements ConsoleCommand {
    @Override
    public void execute(final String[] args) {
      console.output("good bye");
      nifty.closePopup(consolePopup.getId());
    }
  }

  private class ClearCommand implements ConsoleCommand {
    @Override
    public void execute(final String[] args) {
      console.clear();
    }
  }

}
