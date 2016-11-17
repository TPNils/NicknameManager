package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface IPlayerCheck extends IMessageControllerHolder
{
    default void checkIsPlayer(CommandSender source) throws CommandException
    {
        if (!(source instanceof Player))
            throw new CommandException(this.getMessageController().getFormattedMessage(Reference.ErrorMessages.PLAYER_ONLY));
    }
}
