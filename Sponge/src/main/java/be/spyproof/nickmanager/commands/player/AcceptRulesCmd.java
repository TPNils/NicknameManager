package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Spyproof on 30/10/2016.
 */
public class AcceptRulesCmd extends AbstractCmd
{
    private AcceptRulesCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if (!(src instanceof Player))
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.PLAYER_ONLY).toText());
            return CommandResult.success();
        }

        PlayerData playerData = this.playerController.wrapPlayer((Player) src);

        if (!playerData.readRules())
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.MUST_READ_RULES).apply(TemplateUtils.getParameters("command", "/nick " + Reference.CommandKeys.PLAYER_RULES[0])).build());
            return CommandResult.success();
        }

        playerData.setAcceptedRules(true);
        this.playerController.savePlayer(playerData);
        src.sendMessage(this.messageController.getMessage(Reference.SuccessMessages.ACCEPTED_RULES).toText());

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .executor(new AcceptRulesCmd(messageController, playerController))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
