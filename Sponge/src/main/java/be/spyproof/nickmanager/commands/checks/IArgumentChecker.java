package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.args.CommandContext;

import java.util.Optional;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface IArgumentChecker extends IMessageControllerHolder
{
    default <T> T getArgument(CommandContext args, String key) throws CommandException
    {
        Optional<T> arg = args.getOne(key);
        if (arg.isPresent())
            return arg.get();
        else
            throw new CommandException(this.getMessageController()
                                           .getMessage(Reference.ErrorMessages.MISSING_ARGUMENT)
                                           .apply(TemplateUtils.getParameters("argument", key))
                                           .build());
    }
}
