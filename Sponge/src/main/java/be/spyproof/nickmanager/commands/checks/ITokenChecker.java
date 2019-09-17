package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.SpongeUtils;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface ITokenChecker extends IMessageControllerHolder {

  default void checkTokens(NicknameData nicknameData, Player src) throws CommandException {
    if (!SpongeUtils.INSTANCE.canChangeNickname(nicknameData, src)) {
      throw new CommandException(this.getMessageController()
                                     .getMessage(Reference.ErrorMessages.MISSING_TOKENS)
                                     .toText()
                                     .concat(Text.NEW_LINE)
                                     .concat(this.getMessageController()
                                                 .getMessage(Reference.ErrorMessages.UNLOCK_TOKENS)
                                                 .apply(TemplateUtils.getParameters("command", "/nick " + Reference.CommandKeys.PLAYER_UNLOCK[0]))
                                                 .toText()));
    }
  }

}
