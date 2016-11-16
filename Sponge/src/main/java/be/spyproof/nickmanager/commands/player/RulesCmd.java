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
public class RulesCmd extends AbstractCmd
{
    private RulesCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        PlayerData playerData = this.playerController.wrapPlayer((Player) src);
        src.sendMessage(this.messageController.getMessage(Reference.SuccessMessages.NICK_RULES).apply(TemplateUtils.getParameters("command", "/" + Reference.CommandKeys.ACCEPT_RULES[0])).build());
        playerData.setReadRules(true);

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .executor(new RulesCmd(messageController, playerController))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
