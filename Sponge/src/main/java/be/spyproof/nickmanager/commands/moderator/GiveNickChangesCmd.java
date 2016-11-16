package be.spyproof.nickmanager.commands.moderator;


import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.argument.PlayerDataArg;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
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
 * Created by Spyproof.
 */
public class GiveNickChangesCmd extends AbstractCmd
{
    private static final String[] ARGS = new String[]{"player", "amount"};

    private GiveNickChangesCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        Optional<PlayerData> playerData = args.getOne(ARGS[0]);
        Optional<Integer> amount = args.getOne(ARGS[1]);

        if (!playerData.isPresent())
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.MISSING_ARGUMENT).apply(TemplateUtils.getParameters("argument", ARGS[0])).build());
            return CommandResult.success();
        }

        if (!amount.isPresent())
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.MISSING_ARGUMENT).apply(TemplateUtils.getParameters("argument", ARGS[1])).build());
            return CommandResult.success();
        }

        playerData.get().setTokensRemaining(amount.get() + playerData.get().getTokensRemaining());
        this.playerController.savePlayer(playerData.get());

        Optional<Player> player = Sponge.getServer().getPlayer(playerData.get().getUuid());
        if (player.isPresent())
        {
            player.get().sendMessage(this.messageController.getMessage(Reference.SuccessMessages.ADMIN_NICK_GIVE_RECEIVED).apply(TemplateUtils.getParameters("tokens", amount.get())).build());
        }

        Map<String, Text> params = TemplateUtils.getParameters(playerData.get());
        params.putAll(TemplateUtils.getParameters("tokens", Text.of(amount.get())));

        src.sendMessage(this.messageController.getMessage(Reference.SuccessMessages.ADMIN_NICK_GIVE).apply(params).build());

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .arguments(new PlayerDataArg(ARGS[0], playerController), GenericArguments.integer(Text.of(ARGS[1])))
                          .executor(new GiveNickChangesCmd(messageController, playerController))
                          .permission(Reference.Permissions.ADMIN_GIVE)
                          .build();
    }
}
