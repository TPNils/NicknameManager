package be.spyproof.nickmanager.commands;

import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;

import java.util.Optional;

/**
 * Created by Spyproof on 14/11/2016.
 */
public abstract class AbstractCmd implements ICommand
{
    protected MessageController messageController;
    protected IBukkitPlayerController playerController;
    protected String[] keys;
    protected ICommand parent;

    protected AbstractCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        this.messageController = messageController;
        this.playerController = playerController;
        this.keys = keys;
    }

    @Override
    public String[] getKeys()
    {
        return this.keys;
    }

    public Optional<ICommand> getParent()
    {
        return Optional.ofNullable(this.parent);
    }

    public void setParent(ICommand parent)
    {
        this.parent = parent;
    }
}
