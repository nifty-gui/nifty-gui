/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.controls;

import de.lessvoid.nifty.controls.chatcontrol.ChatEntryModelClass;
import de.lessvoid.nifty.render.NiftyImage;
import java.util.List;

/**
 *
 * @author ractoc
 */
public interface Chat extends NiftyControl {
    
     /**
     * This method is called when a chat line is received which should be displayed in
     * the chat control.
     * 
     * @param text
     *            The text to display.
     * @param icon
     *            Optionally, an icon can be supplied which is then displayed at
     *            the start of the chat line.
     */
    void receivedChatLine(String text, NiftyImage icon);
    
    /**
     * This method is called when a chat line is received which should be displayed in
     * the chat control. This method has one extra parameter. This parameter allows 
     * for a custom style to be set per chat line.
     * 
     * @param text
     *            The text to display.
     * @param icon
     *            Optionally, an icon can be supplied which is then displayed at
     *            the start of the chat line.
     * @param style 
     *            The custom style for this particular chatline.
     */
    void receivedChatLine(String text, NiftyImage icon, String style);
    
    /**
     * This method is called when a new player enters the room. This adds that
     * player to the list of players already in the room. If more then one
     * player needs to be added, this method will have to be called multiple
     * times.
     * 
     * @param playerName
     *            The player to add.
     * @param playerIcon
     *            Optionally, an icon can be supplied which is then displayed in
     *            front of the player name.
     */
    void addPlayer(String playerName, NiftyImage playerIcon);
    
    /**
     * This method is called when a new player enters the room. This adds that
     * player to the list of players already in the room. If more then one
     * player needs to be added, this method will have to be called multiple
     * times. This method has an additional parameter which allows for the 
     * setting of a custom style per entry.
     * 
     * @param playerName
     *            The player to add.
     * @param playerIcon
     *            Optionally, an icon can be supplied which is then displayed in
     *            front of the player name.
     * @param style 
     *            The custom style for this player. This style is depicted in the 
     *            player list.
     */
    void addPlayer(String playerName, NiftyImage playerIcon, String style);
    
    /**
     * This method is called when a player leaves the rome and needs to be
     * removed from the list.
     * 
     * @param playerName
     *            The player name to remove.
     */
    void removePlayer(String playerName);
    
    /**
     * This method returns the current list of players in the chat.
     * @return The current list of players.
     */
    List<ChatEntryModelClass> getPlayers();
    
    /**
     * This method returns all the chatlines in the chat.
     * @return The current list of chatlines.
     */
    List<ChatEntryModelClass> getLines();
    
    /**
     * Updates the lists to reflecct any changes made to them,
     * outside of the addPlayer, removePlayer and 
     */
    void update();
    
}
