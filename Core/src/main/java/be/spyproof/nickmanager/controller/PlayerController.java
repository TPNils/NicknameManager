package be.spyproof.nickmanager.controller;


import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.model.PlayerData;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class PlayerController implements IPlayerController
{
    protected IPlayerStorage storage;

    public PlayerController(IPlayerStorage storage)
    {
        this.storage = storage;
    }

    @Override
    public void savePlayer(PlayerData player)
    {
        this.storage.savePlayer(player);
    }

    @Override
    public Optional<PlayerData> getPlayer(String name)
    {
        return getStoredPlayer(name);
    }

    @Override
    public Optional<PlayerData> getPlayer(UUID uuid)
    {
        return getStoredPlayer(uuid);
    }

    @Override
    public List<PlayerData> getPlayerByNickname(String nickname)
    {
        return getPlayerByNickname(nickname, 10);
    }

    @Override
    public List<PlayerData> getPlayerByNickname(String nickname, int limit)
    {
        return this.storage.getPlayerByNickname(nickname, limit);
    }

    public void removePlayer(PlayerData player)
    {
        this.storage.removePlayer(player);
    }

    @Override
    public PlayerData wrap(UUID uuid, String name)
    {
        PlayerData playerData;
        Optional<PlayerData> optional = getStoredPlayer(uuid);
        if (optional.isPresent())
            playerData = optional.get();
        else
        {
            playerData = new PlayerData(name, uuid);
        }

        if (!playerData.getName().equals(name))
        {
            playerData.setName(name);
            savePlayer(playerData);
        }

        return playerData;
    }

    @Override
    public void logout(UUID uuid)
    {

    }

    @Override
    public void close() throws IOException
    {
        this.storage.close();
    }

    protected Optional<PlayerData> getStoredPlayer(String name)
    {
        Optional<PlayerData> player;
        Optional<PlayerData> stored = this.storage.getPlayer(name);

        if (stored.isPresent())
            player = Optional.of(stored.get());
        else
            player = Optional.empty();

        return player;
    }

    protected Optional<PlayerData> getStoredPlayer(UUID uuid)
    {
        Optional<PlayerData> player;
        Optional<PlayerData> stored = this.storage.getPlayer(uuid);

        if (stored.isPresent())
            player = Optional.of(stored.get());
        else
            player = Optional.empty();

        return player;
    }
}
