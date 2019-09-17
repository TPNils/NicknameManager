package be.spyproof.nickmanager.test.da.player;

import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Spyproof on 04/11/2016.
 */
public class DummyPlayerStorage implements IPlayerStorage {

  public List<NicknameData> players = new ArrayList<>();

  @Override
  public void savePlayer(NicknameData player) {
    for (int i = 0; i < this.players.size(); i++) {
      if (this.players.get(i).getUuid().equals(player.getUuid())) {
        this.players.set(i, player);
        return;
      }
    }

    this.players.add(player);
  }

  @Override
  public void removePlayer(NicknameData player) {
    for (int i = 0; i < this.players.size(); i++) {
      if (this.players.get(i).getUuid().equals(player.getUuid())) {
        this.players.remove(i);
        return;
      }
    }
  }

  @Override
  public Optional<NicknameData> getPlayer(String name) {
    for (NicknameData player : this.players) {
      if (player.getName().equals(name)) {
        return Optional.of(player);
      }
    }

    return Optional.empty();
  }

  @Override
  public Optional<NicknameData> getPlayer(UUID uuid) {
    for (NicknameData player : this.players) {
      if (player.getUuid().equals(uuid)) {
        return Optional.of(player);
      }
    }

    return Optional.empty();
  }

  @Override
  public List<NicknameData> getPlayerByNickname(String nickname, int limit) {
    List<NicknameData> players = new ArrayList<>();

    for (NicknameData player : this.players) {
      if (player.getNickname().isPresent() && player.getNickname().get().replaceAll(Reference.COLOUR_AND_STYLE_PATTERN, "").contains(nickname)) {
        players.add(player);
      }
    }

    return players;
  }

  @Override
  public void close() throws IOException {

  }

}
