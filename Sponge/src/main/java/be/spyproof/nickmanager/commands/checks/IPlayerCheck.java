package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.Reference;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface IPlayerCheck extends IMessageControllerHolder {

  default void checkIsPlayer(CommandSource source) throws CommandException {
    if (!(source instanceof Player)) {
      throw new CommandException(this.getMessageController().getMessage(Reference.ErrorMessages.PLAYER_ONLY).toText());
    }
  }

}
