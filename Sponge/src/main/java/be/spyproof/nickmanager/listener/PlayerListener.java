package be.spyproof.nickmanager.listener;

import be.spyproof.nickmanager.controller.ISpongePlayerController;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

/**
 * Created by Spyproof on 30/10/2016.
 *
 * Listen to players logging in and out.
 */
public class PlayerListener
{
    private ISpongePlayerController playerController;

    public PlayerListener(ISpongePlayerController playerController)
    {
        this.playerController = playerController;
    }

    /**
     * Wrap the player with the player controller, which should save the player to the underlying storage.
     * @param event The fired event
     */
    @Listener
    public void onJoin(ClientConnectionEvent.Join event)
    {
        this.playerController.wrap(event.getTargetEntity().getUniqueId(), event.getTargetEntity().getName());
    }

    /**
     * Trigger the logout method from the player controller.
     * @param event The fired event
     */
    @Listener
    public void onDisconnect(ClientConnectionEvent.Disconnect event)
    {
        this.playerController.logout(event.getTargetEntity().getUniqueId());
    }
}
