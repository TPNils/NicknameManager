package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.BukkitUtils;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandException;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface ILengthChecker extends IMessageControllerHolder {

  default void checkLength(String nickname) throws CommandException {
    if (!BukkitUtils.INSTANCE.lengthCheck(nickname)) {
      String msg = this.getMessageController().getFormattedMessage(Reference.ErrorMessages.NICKNAME_TO_LONG);
      msg = msg.replace("{length-with-colour}", BukkitUtils.INSTANCE.getConfigController().maxNickLengthWithColour() + "");
      msg = msg.replace("{length-without-colour}", BukkitUtils.INSTANCE.getConfigController().maxNickLengthWithoutColour() + "");
      throw new CommandException(msg);
    }
  }

}
