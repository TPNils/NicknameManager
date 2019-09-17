package be.spyproof.nickmanager.controller;

import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.model.NicknameData;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class SpongeNicknameController extends CachedNicknameController implements ISpongeNicknameController {

  public SpongeNicknameController(IPlayerStorage storage) {
    super(storage);
  }

  @Override
  public NicknameData wrapPlayer(Player player) {
    NicknameData nicknameData;
    Optional<NicknameData> optional = getCachedPlayer(player.getUniqueId());

    if (optional.isPresent()) {
      nicknameData = optional.get();
    } else {
      optional = getStoredPlayer(player.getUniqueId());
      nicknameData = optional.orElseGet(() -> wrap(player.getUniqueId(), player.getName()));
      cachePlayer(nicknameData);
    }

    if (!nicknameData.getName().equals(player.getName())) {
      nicknameData.setName(player.getName());
      savePlayer(nicknameData);
    }

    return nicknameData;
  }

}
