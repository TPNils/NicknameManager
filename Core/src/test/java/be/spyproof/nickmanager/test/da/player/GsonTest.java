package be.spyproof.nickmanager.test.da.player;

import be.spyproof.nickmanager.test.TestReference;
import be.spyproof.nickmanager.test.da.DaTester;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by Spyproof on 04/11/2016.
 */
public class GsonTest extends DaTester {

  public GsonTest() throws Exception {
    super(TestReference.getGsonPlayerStorage(), TestReference.getPlayerData());
  }

  @Test(priority = 0)
  @Override
  public void insert() {
    super.insert();
  }

  @Test(priority = 1)
  @Override
  public void update() {
    super.update();
  }

  @Test(priority = 2)
  @Override
  public void getByUuid() {
    super.getByUuid();
  }

  @Test(priority = 2)
  @Override
  public void getByName() {
    super.getByName();
  }

  @Test(priority = 2)
  @Override
  public void getByNickname() {
    super.getByNickname();
  }

  @Test(priority = 3)
  @Override
  public void remove() throws IOException {
    super.remove();
  }

}
