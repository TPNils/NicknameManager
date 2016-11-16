package be.spyproof.nickmanager.controller;

import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.model.PlayerData;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class BukkitPlayerController extends CachedPlayerController implements IBukkitPlayerController
{
    public BukkitPlayerController(IPlayerStorage storage)
    {
        super(storage);
    }

    @Override
    public PlayerData wrapPlayer(Player player)
    {
        PlayerData playerData;
        Optional<PlayerData> optional = getCachedPlayer(player.getUniqueId());

        if (optional.isPresent())
        {
            playerData = optional.get();
        }else
        {
            optional = getStoredPlayer(player.getUniqueId());
            if (optional.isPresent())
                playerData = optional.get();
            else
                playerData = wrap(player.getUniqueId(), player.getName());
            cachePlayer(playerData);
        }

        if (!playerData.getName().equals(player.getName()))
        {
            playerData.setName(player.getName());
            savePlayer(playerData);
        }

        return playerData;
    }
}
