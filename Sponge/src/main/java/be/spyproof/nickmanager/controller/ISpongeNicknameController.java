package be.spyproof.nickmanager.controller;

import be.spyproof.nickmanager.model.NicknameData;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Spyproof on 28/10/2016.
 */
public interface ISpongeNicknameController extends INicknameController {

  /**
   * Wrapping a player will attempt to get a player from the storage.
   * If one is found and the name is different, the name will automatically update
   * If none is found, it will create a new NicknameData instance and save it to the storage.
   *
   * @param player The player of who you wish to retreive the data from
   *
   * @return The player data of that player
   */
  NicknameData wrapPlayer(Player player);

}
