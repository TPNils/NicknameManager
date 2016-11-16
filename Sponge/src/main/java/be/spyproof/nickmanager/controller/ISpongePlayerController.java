package be.spyproof.nickmanager.controller;

import be.spyproof.nickmanager.model.PlayerData;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Spyproof on 28/10/2016.
 */
public interface ISpongePlayerController extends IPlayerController
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
