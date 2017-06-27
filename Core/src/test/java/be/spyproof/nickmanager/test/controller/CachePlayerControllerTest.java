package be.spyproof.nickmanager.test.controller;

import be.spyproof.nickmanager.controller.CachedNicknameController;
import be.spyproof.nickmanager.test.da.player.DummyPlayerStorage;
import org.testng.annotations.Test;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class CachePlayerControllerTest extends PlayerControllerTester
{
    public CachePlayerControllerTest()
    {
        super(new CachedNicknameController(new DummyPlayerStorage()));
    }

    @Test
    @Override
    public void wrapNewPlayer()
    {
        super.wrapNewPlayer();
    }

    @Test(priority = 1)
    @Override
    public void updatePlayer()
    {
        super.updatePlayer();
    }

    @Test(priority = 2)
    @Override
    public void getPlayerByName()
    {
        super.getPlayerByName();
    }

    @Test(priority = 2)
    @Override
    public void getPlayerByUuid()
    {
        super.getPlayerByUuid();
    }

    @Test(priority = 2)
    @Override
    public void getPlayerByNickname()
    {
        super.getPlayerByNickname();
    }

    @Test(priority = 3)
    @Override
    public void removePlayer()
    {
        super.removePlayer();
    }
}