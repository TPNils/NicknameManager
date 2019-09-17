package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface IPermissionCheck extends IMessageControllerHolder {

  default void checkPermission(CommandSender source, String permission) throws CommandException {
    if (!source.hasPermission(permission)) {
      throw new CommandException(this.getMessageController().getFormattedMessage(Reference.ErrorMessages.NO_PERMISSION).replace("{permission}", Reference.Permissions.GENERIC_PLAYER_COMMANDS));
    }
  }

}
