package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.SpongeUtils;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface ILengthChecker extends IMessageControllerHolder {

  default void checkLength(String nickname) throws CommandException {
    if (!SpongeUtils.INSTANCE.lengthCheck(nickname)) {
      Map<String, Integer> placeholders = new HashMap<>();
      placeholders.putAll(TemplateUtils.getParameters("length-with-colour", SpongeUtils.INSTANCE.getConfigController().maxNickLengthWithColour()));
      placeholders.putAll(TemplateUtils.getParameters("length-without-colour", SpongeUtils.INSTANCE.getConfigController().maxNickLengthWithoutColour()));
      throw new CommandException(this.getMessageController().getMessage(Reference.ErrorMessages.NICKNAME_TO_LONG).apply(placeholders).build());
    }
  }

}
