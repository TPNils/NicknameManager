package be.spyproof.nickmanager.listener;

import be.spyproof.nickmanager.controller.ISpongeNicknameController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.SpongeUtils;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

/**
 * Created by Spyproof on 30/10/2016.
 * <p>
 * Listen to players logging in and out.
 */
public class PlayerListener {

  private ISpongeNicknameController playerController;

  public PlayerListener(ISpongeNicknameController playerController) {
    this.playerController = playerController;
  }

  /**
   * Wrap the player with the player controller, which should save the player to the underlying storage.
   *
   * @param event The fired event
   */
  @Listener
  public void onJoin(ClientConnectionEvent.Join event) {
    NicknameData nicknameData = this.playerController.wrap(event.getTargetEntity().getUniqueId(), event.getTargetEntity().getName());
    SpongeUtils.INSTANCE.applyNicknameToTabList(nicknameData, event.getTargetEntity());
  }

  /**
   * Trigger the logout method from the player controller.
   *
   * @param event The fired event
   */
  @Listener
  public void onDisconnect(ClientConnectionEvent.Disconnect event) {
    this.playerController.logout(event.getTargetEntity().getUniqueId());
  }

}
