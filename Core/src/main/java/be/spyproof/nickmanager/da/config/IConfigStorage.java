package be.spyproof.nickmanager.da.config;

import be.spyproof.nickmanager.da.player.IPlayerStorage;

import java.util.List;
import java.util.Map;

/**
 * Created by Spyproof on 31/10/2016.
 */
public interface IConfigStorage {

  /**
   * Load the config from a file/data storage.
   * Should be able to be used to reload as well
   * <p>
   * If you obtain these values from a storage system that
   * updates its values in real time, this can be empty
   *
   * @throws Exception When the loading fails for any reason
   */
  void load() throws Exception;

  /**
   * @return The language used for sending messages or other settings
   */
  String getLanguage();

  /**
   * @return If players need to accept the rules or not
   */
  boolean mustAcceptRules();

  /**
   * @return The amount of colours a player is allowed to have in their nickname
   *
   * @see be.spyproof.nickmanager.util.Colour
   */
  int maxColours();

  /**
   * @return The amount of styles a player is allowed to have in their nickname
   *
   * @see be.spyproof.nickmanager.util.Style
   */
  int maxStyles();

  /**
   * @return The maximum amount of character a player is allowed to have in their nickname with colour codes
   */
  int maxNickLengthWithColour();

  /**
   * @return The maximum amount of character a player is allowed to have in their nickname without colour codes
   */
  int maxNickLengthWithoutColour();

  /**
   * @return The player storage as specified in the config
   *
   * @throws Exception When the loading fails for any reason
   */
  IPlayerStorage getPlayerStorage() throws Exception;

  /**
   * @return All cooldown categories on how long a player will have to wait before they can change their nickname again.
   * This must contain a key 'default'
   */
  Map<String, Long> getCooldowns();

  /**
   * @return A List of regex strings. A non formatted nickname can not match any of these values
   */
  List<String> getBlacklist();

  /**
   * @return If players nicknames will also be set in the tab list
   */
  boolean setTabListName();

}
