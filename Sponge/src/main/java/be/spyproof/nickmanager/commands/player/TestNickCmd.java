package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.*;
import be.spyproof.nickmanager.commands.argument.NicknameArg;
import be.spyproof.nickmanager.commands.checks.IArgumentChecker;
import be.spyproof.nickmanager.commands.checks.IBlacklistChecker;
import be.spyproof.nickmanager.commands.checks.IFormatChecker;
import be.spyproof.nickmanager.commands.checks.ILengthChecker;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.*;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class TestNickCmd extends AbstractCmd implements IPlayerCmd, IBlacklistChecker, IFormatChecker, ILengthChecker, IArgumentChecker
{
    private static final String ARG = "nickname";

    private TestNickCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(Player src, CommandContext args) throws CommandException
    {
        String nick = getArgument(args, ARG);

        checkBlacklist(src, nick);
        checkFormat(src, nick);
        checkLength(nick);

        src.sendMessage(this.getMessageController().getMessage(Reference.SuccessMessages.NICK_PREVIEW).apply(TemplateUtils.getParameters("nickname", TextSerializers.FORMATTING_CODE.deserialize(nick))).build());

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .arguments(new NicknameArg(ARG, playerController))
                          .executor(new TestNickCmd(messageController, playerController))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
