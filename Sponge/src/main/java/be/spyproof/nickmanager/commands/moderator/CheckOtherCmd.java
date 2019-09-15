package be.spyproof.nickmanager.commands.moderator;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.argument.PlayerDataArg;
import be.spyproof.nickmanager.commands.checks.IArgumentChecker;
import be.spyproof.nickmanager.controller.ISpongeNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TemplateUtils;
import be.spyproof.nickmanager.util.TextUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;

/**
 * Created by Spyproof on 01/11/2016.
 */
public class CheckOtherCmd extends AbstractCmd implements IArgumentChecker
{
    private static final String ARG = "player";

    private CheckOtherCmd(MessageController messageController, ISpongeNicknameController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        NicknameData player = getArgument(args, ARG);

        src.sendMessages(
          TextUtils.splitLines(
            this.getMessageController()
                .getMessage(Reference.SuccessMessages.ADMIN_NICK_CHECK)
                .apply(TemplateUtils.getParameters(player))
                .build()
          )
        );

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongeNicknameController playerController)
    {
        return CommandSpec.builder()
                          .arguments(new PlayerDataArg(ARG, playerController))
                          .executor(new CheckOtherCmd(messageController, playerController))
                          .permission(Reference.Permissions.ADMIN_CHECK)
                          .build();
    }
}
