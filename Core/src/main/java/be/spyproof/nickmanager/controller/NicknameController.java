package be.spyproof.nickmanager.controller;


import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.model.NicknameData;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class NicknameController implements INicknameController
{
    protected IPlayerStorage storage;

    public NicknameController(IPlayerStorage storage)
    {
        this.storage = storage;
    }

    @Override
    public void savePlayer(NicknameData player)
    {
        this.storage.savePlayer(player);
    }

    @Override
    public Optional<NicknameData> getPlayer(String name)
    {
        return getStoredPlayer(name);
    }

    @Override
    public Optional<NicknameData> getPlayer(UUID uuid)
    {
        return getStoredPlayer(uuid);
    }

    @Override
    public List<NicknameData> getPlayerByNickname(String nickname)
    {
        return getPlayerByNickname(nickname, 10);
    }

    @Override
    public List<NicknameData> getPlayerByNickname(String nickname, int limit)
    {
        return this.storage.getPlayerByNickname(nickname, limit);
    }

    public void removePlayer(NicknameData player)
    {
        this.storage.removePlayer(player);
    }

    @Override
    public NicknameData wrap(UUID uuid, String name)
    {
        NicknameData nicknameData;
        Optional<NicknameData> optional = getStoredPlayer(uuid);
        if (optional.isPresent())
            nicknameData = optional.get();
        else
        {
            nicknameData = new NicknameData(name, uuid);
            savePlayer(nicknameData);
        }

        if (!nicknameData.getName().equals(name))
        {
            nicknameData.setName(name);
            savePlayer(nicknameData);
        }

        return nicknameData;
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

    protected Optional<NicknameData> getStoredPlayer(String name)
    {
        Optional<NicknameData> player;
        Optional<NicknameData> stored = this.storage.getPlayer(name);

        if (stored.isPresent())
            player = Optional.of(stored.get());
        else
            player = Optional.empty();

        return player;
    }

    protected Optional<NicknameData> getStoredPlayer(UUID uuid)
    {
        Optional<NicknameData> player;
        Optional<NicknameData> stored = this.storage.getPlayer(uuid);

        if (stored.isPresent())
            player = Optional.of(stored.get());
        else
            player = Optional.empty();

        return player;
    }
}
