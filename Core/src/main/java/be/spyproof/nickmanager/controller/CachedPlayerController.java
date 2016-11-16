package be.spyproof.nickmanager.controller;


import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.model.PlayerData;

import java.io.IOException;
import java.util.*;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class CachedPlayerController extends PlayerController
{
    private Set<PlayerData> cachedPlayers;

    public CachedPlayerController(IPlayerStorage storage)
    {
        super(storage);
        this.cachedPlayers = new HashSet<>();
    }

    @Override
    public Optional<PlayerData> getPlayer(String name)
    {
        Optional<PlayerData> player = getCachedOrStoredPlayer(name);

        if (player.isPresent() && !this.cachedPlayers.contains(player.get()))
            this.cachedPlayers.add(player.get());

        return player;
    }

    @Override
    public Optional<PlayerData> getPlayer(UUID uuid)
    {
        Optional<PlayerData> player = getCachedOrStoredPlayer(uuid);

        if (player.isPresent() && !this.cachedPlayers.contains(player.get()))
            this.cachedPlayers.add(player.get());

        return player;
    }

    @Override
    public void removePlayer(PlayerData player)
    {
        super.removePlayer(player);
        removeCachedPlayer(player.getUuid());
    }

    @Override
    public void logout(UUID uuid)
    {
        removeCachedPlayer(uuid);
    }

    @Override
    public void close() throws IOException
    {
        this.storage.close();
        this.cachedPlayers.clear();
    }

    protected void cachePlayer(PlayerData playerData)
    {
        this.cachedPlayers.add(playerData);
    }

    protected void removeCachedPlayer(UUID... uuids)
    {
        Iterator<PlayerData> iterator = this.cachedPlayers.iterator();
        while (iterator.hasNext())
        {
            PlayerData data = iterator.next();
            for (UUID uuid : uuids)
            {
                if (data.getUuid().equals(uuid))
                {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    protected Optional<PlayerData> getCachedOrStoredPlayer(String name)
    {
        Optional<PlayerData> player = getCachedPlayer(name);
        if (player.isPresent())
            return player;

        Optional<PlayerData> stored = super.getStoredPlayer(name);
        if (stored.isPresent())
            return stored;

        return Optional.empty();
    }

    protected Optional<PlayerData> getCachedOrStoredPlayer(UUID uuid)
    {
        Optional<PlayerData> player = getCachedPlayer(uuid);
        if (player.isPresent())
            return player;

        Optional<PlayerData> stored = super.getStoredPlayer(uuid);
        if (stored.isPresent())
            return stored;

        return Optional.empty();
    }

    protected Optional<PlayerData> getCachedPlayer(String name)
    {
        for (PlayerData player : this.cachedPlayers)
            if (player.getName().equalsIgnoreCase(name))
                return Optional.of(player);

        return Optional.empty();
    }

    protected Optional<PlayerData> getCachedPlayer(UUID uuid)
    {
        for (PlayerData player : this.cachedPlayers)
            if (player.getUuid().equals(uuid))
                return Optional.of(player);

        return Optional.empty();
    }


}
