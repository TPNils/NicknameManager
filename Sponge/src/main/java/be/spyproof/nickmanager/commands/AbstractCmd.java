package be.spyproof.nickmanager.commands;

import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * Created by Spyproof on 01/11/2016.
 */
public abstract class AbstractCmd implements IMessageControllerHolder, IPlayerControllerHolder, CommandExecutor
{
    private MessageController messageController;
    private ISpongePlayerController playerController;

    protected AbstractCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        this.messageController = messageController;
        this.playerController = playerController;
    }

    @Override
    public ISpongePlayerController getPlayerController()
    {
        return this.playerController;
    }

    @Override
    public MessageController getMessageController()
    {
        return this.messageController;
    }
}
