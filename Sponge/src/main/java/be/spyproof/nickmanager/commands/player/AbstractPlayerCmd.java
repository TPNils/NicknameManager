package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.SpongeUtils;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Spyproof on 30/10/2016.
 */
public abstract class AbstractPlayerCmd extends AbstractCmd
{
    protected AbstractPlayerCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if (!(src instanceof Player))
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.PLAYER_ONLY).toText());
            return CommandResult.success();
        }

        if (!SpongeUtils.INSTANCE.acceptedRules((Player) src))
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.MUST_ACCEPT_RULES).apply(TemplateUtils.getParameters("command", "/" + Reference.CommandKeys.ACCEPT_RULES[0])).build());
            return CommandResult.success();
        }

        return execute((Player) src, args);
    }

    public abstract CommandResult execute(Player src, CommandContext args) throws CommandException;


}
