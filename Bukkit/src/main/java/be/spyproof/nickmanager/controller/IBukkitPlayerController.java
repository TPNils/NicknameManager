package be.spyproof.nickmanager.controller;

import be.spyproof.nickmanager.model.PlayerData;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 09/11/2016.
 */
public interface IBukkitPlayerController extends IPlayerController
{
    /**
     * Wrapping a player will attempt to get a player from the storage.
     * If one is found and the name is different, the name will automatically update
     * If none is found, it will create a new PlayerData instance and save it to the storage.
     *
     * @param player The player of who you wish to retreive the data from
     * @return The player data of that player
     */
    PlayerData wrapPlayer(Player player);
}
