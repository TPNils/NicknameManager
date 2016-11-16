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
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

/**
 * Created by Spyproof on 01/11/2016.
 */
public class ResetOtherRulesCmd extends AbstractCmd
{
    private static final String ARG = "player";

    private ResetOtherRulesCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        Optional<PlayerData> playerData = args.getOne(ARG);

        if (!playerData.isPresent())
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.MISSING_ARGUMENT).apply(TemplateUtils.getParameters("argument", ARG)).build());
            return CommandResult.success();
        }

        playerData.get().setAcceptedRules(false);
        playerData.get().setReadRules(false);
        this.playerController.savePlayer(playerData.get());

        Optional<Player> player = Sponge.getServer().getPlayer(playerData.get().getUuid());
        if (player.isPresent())
        {
            player.get().sendMessage(this.messageController.getMessage(Reference.SuccessMessages.ADMIN_NICK_RESET_RULES_RECEIVED).toText());
        }

        src.sendMessage(this.messageController.getMessage(Reference.SuccessMessages.ADMIN_NICK_RESET_RULES).apply(TemplateUtils.getParameters(playerData.get())).build());
        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .arguments(new PlayerDataArg(ARG, playerController))
                          .executor(new ResetOtherRulesCmd(messageController, playerController))
                          .permission(Reference.Permissions.ADMIN_RESET)
                          .build();
    }
}
