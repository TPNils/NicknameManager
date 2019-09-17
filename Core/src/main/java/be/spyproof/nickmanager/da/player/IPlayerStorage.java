package be.spyproof.nickmanager.da.player;

import be.spyproof.nickmanager.model.NicknameData;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Spyproof on 26/10/2016.
 */
public interface IPlayerStorage extends Closeable {

  /**
   * Save a player to the storage.
   * This will either update the already stored values or create a new entry for the player
   *
   * @param player The player data that needs to be saved.
   */
  void savePlayer(NicknameData player);

  /**
   * Remove the player completely from the storage system
   * <p>
   * NOTE: This does not reset the player, it actually removes everything.
   *
   * @param player The player data that needs to be removed.
   */
  void removePlayer(NicknameData player);

  /**
   * @param name The name a player might have that will be used to look for them
   *
   * @return The player found that matches the name, or empty if none were found
   */
  Optional<NicknameData> getPlayer(String name);

  /**
   * @param uuid The UUID a player might have that will be used to look for them
   *
   * @return The player found that matches the UUID, or empty if none were found
   *
   * @see UUID
   */
  Optional<NicknameData> getPlayer(UUID uuid);

  /**
   * @param nickname An unformatted string used to search for players
   * @param limit    Applies a limit of the amount of players that may be returned
   *
   * @return A List of all possible players where their nickname matches the given nickname.
   * The list will not be bigger than the given limit
   */
  List<NicknameData> getPlayerByNickname(String nickname, int limit);

}
