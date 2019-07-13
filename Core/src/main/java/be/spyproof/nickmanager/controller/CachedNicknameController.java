package be.spyproof.nickmanager.controller;


import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.model.NicknameData;

import java.io.IOException;
import java.util.*;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class CachedNicknameController extends NicknameController {
  private Set<NicknameData> cachedPlayers;

  public CachedNicknameController(IPlayerStorage storage) {
    super(storage);
    this.cachedPlayers = new HashSet<>();
  }

  @Override
  public Optional<NicknameData> getPlayer(String name) {
    return getCachedOrStoredPlayer(name);
  }

  @Override
  public Optional<NicknameData> getPlayer(UUID uuid) {
    return getCachedOrStoredPlayer(uuid);
  }

  @Override
  public void removePlayer(NicknameData player) {
    super.removePlayer(player);
    removeCachedPlayer(player.getUuid());
  }

  @Override
  public void logout(UUID uuid) {
    removeCachedPlayer(uuid);
  }

  @Override
  public void close() throws IOException {
    this.storage.close();
    this.cachedPlayers.clear();
  }

  protected void cachePlayer(NicknameData nicknameData) {
    this.cachedPlayers.add(nicknameData);
  }

  protected void removeCachedPlayer(UUID... uuids) {
    Iterator<NicknameData> iterator = this.cachedPlayers.iterator();
    while (iterator.hasNext()) {
      NicknameData data = iterator.next();
      for (UUID uuid : uuids) {
        if (data.getUuid().equals(uuid)) {
          iterator.remove();
          break;
        }
      }
    }
  }

  protected Optional<NicknameData> getCachedOrStoredPlayer(String name) {
    Optional<NicknameData> player = getCachedPlayer(name);
    if (player.isPresent()) {
      return player;
    }

    Optional<NicknameData> stored = super.getStoredPlayer(name);
    if (stored.isPresent()) {
      this.cachePlayer(stored.get());
      return stored;
    }

    return Optional.empty();
  }

  protected Optional<NicknameData> getCachedOrStoredPlayer(UUID uuid) {
    Optional<NicknameData> player = getCachedPlayer(uuid);
    if (player.isPresent()) {
      return player;
    }

    Optional<NicknameData> stored = super.getStoredPlayer(uuid);
    if (stored.isPresent()) {
      this.cachePlayer(stored.get());
      return stored;
    }

    return Optional.empty();
  }

  protected Optional<NicknameData> getCachedPlayer(String name) {
    for (NicknameData player : this.cachedPlayers) {
      if (player.getName().equalsIgnoreCase(name)) {
        return Optional.of(player);
      }
    }

    return Optional.empty();
  }

  protected Optional<NicknameData> getCachedPlayer(UUID uuid) {
    for (NicknameData player : this.cachedPlayers) {
      if (player.getUuid().equals(uuid)) {
        return Optional.of(player);
      }
    }

    return Optional.empty();
  }


}
