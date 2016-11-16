package be.spyproof.nickmanager.commands;

import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * Created by Spyproof on 01/11/2016.
 */
public abstract class AbstractCmd implements CommandExecutor
{
    protected MessageController messageController;
    protected ISpongePlayerController playerController;

    protected AbstractCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        this.messageController = messageController;
        this.playerController = playerController;
    }
}
