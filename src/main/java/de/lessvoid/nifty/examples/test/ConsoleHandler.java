package de.lessvoid.nifty.examples.test;

import java.util.ArrayList;
import java.util.Arrays;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.console.controller.ConsoleCommandHandler;
import de.lessvoid.nifty.controls.console.controller.ConsoleControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ConsoleHandler implements ScreenController, KeyInputHandler {
  private Nifty nifty;
   private Screen screen;
   private boolean consoleVisible = false;
   private boolean allowConsoleToggle = true;

 ArrayList<String> commands = new ArrayList<String>(Arrays.asList(

 "testnetwork"));
  public void bind(final Nifty newNifty, final Screen newScreen) {
       nifty = newNifty;
       screen = newScreen;
       screen.addKeyboardInputHandler(new DefaultInputMapping(), this);
     }

     public void onStartScreen() {
     }

     public void onEndScreen() {
     }

     public void back() {
    
     }

     public boolean keyEvent(final NiftyInputEvent inputEvent) {
       System.exit(0);
       System.out.println("keyEvent ConsoleHandler");
       if (inputEvent == NiftyInputEvent.ConsoleToggle) {
         toggleConsole();
         return true;
       } else {
         return false;
       }
     }

     private void toggleConsole() {
       if (allowConsoleToggle) {
         allowConsoleToggle = false;
         if (consoleVisible) {
           closeConsole();
         } else {
           openConsole();
         }
       }
     }

     private void openConsole() {
       Element popup = nifty.createPopup("consolePopup");

// FIXME new controls
//       final ConsoleControl control = popup.findControl("console", ConsoleControl.class);
//       control.output("Nifty Console Demo\nVersion: 1.0");
//       control.addCommandHandler(new ConsoleCommandHandler() {
//         public void execute(final String line) {
//           // just echo to the console
//           control.output("your input was: " + line);
//           if ("exit".equals(line.toLowerCase())) {
//             back();
//           }
//         }
//       });
//
//       nifty.showPopup(screen, "consolePopup", null);
//       consoleVisible = true;
//       allowConsoleToggle = true;
     }

     private void closeConsole() {
       nifty.closePopup("consolePopup", new EndNotify() {
         @Override
         public void perform() {
           consoleVisible = false;
           allowConsoleToggle = true;
         }
       });
     }
 

 private void executeCommand(ConsoleControl control, String command) {

   if (command.contains("testnetwork")) {
     // test network connectivity

   }

 }

 private boolean checkValidCommand(String commandIn) {
   if (commands.contains(commandIn)) {
     return true;
   }

   return false;
 }

 



 
}