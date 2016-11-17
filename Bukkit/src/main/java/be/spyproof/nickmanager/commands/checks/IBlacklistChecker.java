package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.BukkitUtils;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface IBlacklistChecker extends IMessageControllerHolder
{
    default void checkBlacklist(CommandSender src, String nickname) throws CommandException
    {
        Optional<String> blacklistRegex = BukkitUtils.INSTANCE.isBlacklisted(src, nickname);
        if (blacklistRegex.isPresent())
        {
            throw new CommandException(this.getMessageController().getFormattedMessage(Reference.ErrorMessages.BLACKLIST)
                                           .replace("{regex}", blacklistRegex.get()));
        }
    }
}
