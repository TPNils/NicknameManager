package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.commands.checks.IPlayerCheck;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.SpongeUtils;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Spyproof on 30/10/2016.
 */
public interface IPlayerCmd extends IMessageControllerHolder, IPlayerCheck, CommandExecutor
{
    default CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        checkIsPlayer(src);

        if (!SpongeUtils.INSTANCE.acceptedRules((Player) src))
        {
            src.sendMessage(this.getMessageController().getMessage(Reference.ErrorMessages.MUST_ACCEPT_RULES).apply(TemplateUtils.getParameters("command", "/" + Reference.CommandKeys.ACCEPT_RULES[0])).build());
            return CommandResult.success();
        }

        return execute((Player) src, args);
    }

    CommandResult execute(Player src, CommandContext args) throws CommandException;
}
