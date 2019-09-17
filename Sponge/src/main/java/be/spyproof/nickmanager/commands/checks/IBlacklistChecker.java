package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.SpongeUtils;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;

import java.util.Optional;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface IBlacklistChecker extends IMessageControllerHolder {

  default void checkBlacklist(CommandSource src, String nickname) throws CommandException {
    Optional<String> blacklistRegex = SpongeUtils.INSTANCE.isBlacklisted(src, nickname);
    if (blacklistRegex.isPresent()) {
      throw new CommandException(this.getMessageController()
                                     .getMessage(Reference.ErrorMessages.BLACKLIST)
                                     .apply(TemplateUtils.getParameters("regex", blacklistRegex.get()))
                                     .build());
    }
  }

}
