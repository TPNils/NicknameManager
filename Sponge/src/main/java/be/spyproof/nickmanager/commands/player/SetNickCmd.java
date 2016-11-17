package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.*;
import be.spyproof.nickmanager.commands.argument.NicknameArg;
import be.spyproof.nickmanager.commands.checks.*;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class SetNickCmd extends AbstractCmd implements IPlayerCmd, IArgumentChecker, IBlacklistChecker, ICooldownChecker, IFormatChecker, ILengthChecker, ITokenChecker
{
    private static final String ARG = "nickname";

    private SetNickCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(Player src, CommandContext args) throws CommandException
    {
        String nick = getArgument(args, ARG);
        PlayerData playerData = this.getPlayerController().wrapPlayer(src);

        checkTokens(playerData, src);
        checkCooldown(playerData, src);
        checkBlacklist(src, nick);
        checkFormat(src, nick);
        checkLength(nick);

        // Apply
        playerData.setNickname(nick);
        playerData.setLastChanged();
        if (!src.hasPermission(Reference.Permissions.BYPASS_CHANGE_LIMIT))
            playerData.setTokensRemaining(playerData.getTokensRemaining()-1);
        this.getPlayerController().savePlayer(playerData);

        src.sendMessage(this.getMessageController().getMessage(Reference.SuccessMessages.NICK_SET).apply(TemplateUtils.getParameters(playerData)).build());
        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .arguments(new NicknameArg(ARG, playerController))
                          .executor(new SetNickCmd(messageController, playerController))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
