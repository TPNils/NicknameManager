package be.spyproof.nickmanager.listeners;

import be.spyproof.nickmanager.controller.IPlayerController;
import be.spyproof.nickmanager.model.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Spyproof on 13/11/2016.
 *
 * Listen to players logging in and out.
 */
public class PlayerListener implements Listener
{
    private IPlayerController playerController;

    public PlayerListener(IPlayerController playerController)
    {
        this.playerController = playerController;
    }

    /**
     * Wrap the player with the player controller, which should save the player to the underlying storage.
     *
     * If the player has a nickname, apply it to the player
     * @param event The fired event
     */
    @EventHandler
    public void onLogin(PlayerLoginEvent event)
    {
        PlayerData playerData = this.playerController.wrap(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        if (playerData.getNickname().isPresent())
            event.getPlayer().setDisplayName(ChatColor.translateAlternateColorCodes('&', playerData.getNickname().get()) + ChatColor.RESET);
    }

    /**
     * Trigger the logout method from the player controller.
     * @param event The fired event
     */
    @EventHandler
    public void onLogin(PlayerQuitEvent event)
    {
        this.playerController.logout(event.getPlayer().getUniqueId());
    }
}
