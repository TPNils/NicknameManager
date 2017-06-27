package be.spyproof.nickmanager.commands.moderator;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.argument.PlayerDataArg;
import be.spyproof.nickmanager.commands.checks.IArgumentChecker;
import be.spyproof.nickmanager.commands.checks.IBlacklistChecker;
import be.spyproof.nickmanager.commands.checks.IFormatChecker;
import be.spyproof.nickmanager.commands.checks.ILengthChecker;
import be.spyproof.nickmanager.controller.ISpongeNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Map;
import java.util.Optional;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class SetNickOthersCmd extends AbstractCmd implements IBlacklistChecker, IFormatChecker, ILengthChecker, IArgumentChecker
{
    private static final String[] ARGS = new String[]{"player", "nickname"};

    private SetNickOthersCmd(MessageController messageController, ISpongeNicknameController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        NicknameData nicknameData = getArgument(args, ARGS[0]);

        if (src instanceof Player && nicknameData.getUuid().equals(((Player) src).getUniqueId()))
            throw new CommandException(this.getMessageController().getMessage(Reference.ErrorMessages.CANT_FORCE_CHANGE_OWN_NICK).apply().build());

        String nick = getArgument(args, ARGS[1]);

        checkBlacklist(src, nick);
        checkFormat(src, nick);
        checkLength(nick);

        // Apply
        nicknameData.setNickname(nick);
        nicknameData.setLastChanged();
        this.getPlayerController().savePlayer(nicknameData);

        Map<String, Text> placeholders = TemplateUtils.getParameters(nicknameData);
        src.sendMessage(this.getMessageController().getMessage(Reference.SuccessMessages.ADMIN_NICK_SET).apply(placeholders).build());
        Optional<Player> receiver = Sponge.getServer().getPlayer(nicknameData.getUuid());
        if (receiver.isPresent())
            receiver.get().sendMessage(this.getMessageController().getMessage(Reference.SuccessMessages.NICK_SET).apply(placeholders).build());

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongeNicknameController playerController)
    {
        return CommandSpec.builder()
                          .arguments(new PlayerDataArg(ARGS[0], playerController), GenericArguments.string(Text.of(ARGS[1])))
                          .executor(new SetNickOthersCmd(messageController, playerController))
                          .permission(Reference.Permissions.ADMIN_SET)
                          .build();
    }
}
