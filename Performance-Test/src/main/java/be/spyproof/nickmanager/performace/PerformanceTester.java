package be.spyproof.nickmanager.performace;

import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.DateUtil;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Spyproof on 03/11/2016.
 */
public class PerformanceTester implements Closeable {

  private IPlayerStorage playerStorage;
  private SecureRandom random = new SecureRandom();
  private NicknameData reference = TestReference.getPlayerData();
  private List<NicknameData> generatedPlayers = new ArrayList<>();
  private long timestamp;

  public PerformanceTester(IPlayerStorage playerStorage) {
    this.playerStorage = playerStorage;
    playerStorage.savePlayer(reference);
  }

  public void setPlayerStorage(IPlayerStorage playerStorage) {
    this.playerStorage = playerStorage;
  }

  public void createPlayers(int amount) throws Exception {
    startTimer();
    System.out.println("Creating " + amount + " players");
    for (int i = 0; i < amount; i++) {
      this.playerStorage.savePlayer(getRandomPlayer());
    }
    stopTimer();
  }

  public void createAndUpdatePlayers(int amount) {
    startTimer();
    System.out.println("Creating and updating " + amount + " players");
    for (int i = 0; i < amount; i++) {
      NicknameData player = getRandomPlayer();
      player.setNickname(getRandomString());
      player.setTokensRemaining(1);
      this.playerStorage.savePlayer(player);
      player.setNickname(getRandomString());
      player.setTokensRemaining(0);
      player.setLastChanged();
      this.playerStorage.savePlayer(player);
    }
    stopTimer();
  }

  public void getByName(int amount) {
    startTimer();
    System.out.println("getting " + amount + " players by name");
    for (int i = 0; i < amount; i++) {
      this.playerStorage.getPlayer(this.generatedPlayers.get(random.nextInt(this.generatedPlayers.size())).getName());
    }
    stopTimer();
  }

  public void getByUuid(int amount) {
    Random random = new Random();
    System.out.println("getting " + amount + " players by uuid");
    startTimer();
    for (int i = 0; i < amount; i++) {
      this.playerStorage.getPlayer(this.generatedPlayers.get(random.nextInt(this.generatedPlayers.size())).getUuid());
    }
    stopTimer();
  }

  public void getByNickname(int amount) {
    System.out.println("getting " + amount + " players by nickname");
    startTimer();
    for (int i = 0; i < amount; i++) {
      this.playerStorage.getPlayerByNickname(getRandomString(), 10);
    }
    stopTimer();
  }

  private NicknameData getRandomPlayer() {
    UUID uuid = UUID.randomUUID();
    NicknameData nicknameData = new NicknameData(getRandomString(), uuid);
    if (this.generatedPlayers.size() < 1000) {
      this.generatedPlayers.add(nicknameData);
    }
    return nicknameData;
  }

  private String getRandomString() {
    return new BigInteger(130, random).toString(32).substring(0, 15);
  }

  private void startTimer() {
    this.timestamp = System.currentTimeMillis();
  }

  private void stopTimer() {
    System.out.print("Completed in: ");
    System.out.println(DateUtil.timeformat(System.currentTimeMillis() - timestamp));
  }

  @Override
  public void close() throws IOException {
    System.out.println("Closing storage");
    startTimer();
    this.playerStorage.close();
    stopTimer();
  }

}
