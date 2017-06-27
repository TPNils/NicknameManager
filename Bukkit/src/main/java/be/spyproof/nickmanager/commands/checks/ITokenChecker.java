package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.BukkitUtils;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface ITokenChecker extends IMessageControllerHolder
{
    default void checkTokens(NicknameData nicknameData, Player src) throws CommandException
    {
        if (!BukkitUtils.INSTANCE.canChangeNickname(nicknameData, src))
        {
            throw new CommandException(this.getMessageController().getFormattedMessage(Reference.ErrorMessages.MISSING_TOKENS)
                                           + "\n"
                                           + (this.getMessageController().getFormattedMessage(Reference.ErrorMessages.UNLOCK_TOKENS))
                                                  .replace("{command}", "/nick " + Reference.CommandKeys.PLAYER_UNLOCK[0]));
        }
    }
}
