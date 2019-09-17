package be.spyproof.nickmanager.test.da;

import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.test.TestReference;
import org.testng.Assert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class DaTester {

  private IPlayerStorage storage;
  private NicknameData testPlayer;

  public DaTester(IPlayerStorage storage, NicknameData testPlayer) {
    this.storage = storage;
    this.testPlayer = testPlayer;
    try (Connection connection = TestReference.getSqlConnection()) {
      connection.prepareStatement("DELETE FROM player;").execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void insert() {
    this.storage.savePlayer(this.testPlayer);

    Optional<NicknameData> storedData = this.storage.getPlayer(this.testPlayer.getUuid());

    if (storedData.isPresent()) {
      Assert.assertEquals(storedData.get(), this.testPlayer);
    } else {
      Assert.fail();
    }
  }

  public void update() {
    this.testPlayer.setNickname("MahArchivedNickname");
    this.storage.savePlayer(this.testPlayer);
    this.testPlayer.setNickname("MahActiveNickname");
    this.storage.savePlayer(this.testPlayer);

    Optional<NicknameData> storedData = this.storage.getPlayer(this.testPlayer.getUuid());

    if (storedData.isPresent()) {
      Assert.assertEquals(storedData.get(), this.testPlayer);
    } else {
      Assert.fail();
    }
  }

  public void getByUuid() {
    Optional<NicknameData> storedData = this.storage.getPlayer(this.testPlayer.getUuid());

    if (storedData.isPresent()) {
      Assert.assertEquals(storedData.get(), this.testPlayer);
    } else {
      Assert.fail();
    }
  }

  public void getByName() {
    Optional<NicknameData> storedData = this.storage.getPlayer(this.testPlayer.getName());

    if (storedData.isPresent()) {
      Assert.assertEquals(storedData.get(), this.testPlayer);
    } else {
      Assert.fail();
    }
  }

  public void getByNickname() {
    List<NicknameData> storedPlayers = this.storage.getPlayerByNickname(this.testPlayer.getNickname().get(), 10);
    for (NicknameData nicknameData : storedPlayers) {
      if (nicknameData.equals(this.testPlayer)) {
        return;
      }
    }

    Assert.fail();
  }

  public void remove() throws IOException {
    this.storage.removePlayer(testPlayer);

    Optional<NicknameData> storedData = this.storage.getPlayer(this.testPlayer.getUuid());
    if (storedData.isPresent()) {
      Assert.assertNull(storedData.get());
    }

    this.storage.close();
  }

}
