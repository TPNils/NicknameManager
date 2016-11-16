package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.Reference;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Spyproof on 31/10/2016.
 */
public class ResetOwnNickCmd extends AbstractPlayerCmd
{
    private ResetOwnNickCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(Player src, CommandContext args) throws CommandException
    {
        PlayerData playerData = this.playerController.wrapPlayer(src);
        playerData.setNickname(null);
        this.playerController.savePlayer(playerData);

        src.sendMessage(this.messageController.getMessage(Reference.SuccessMessages.NICK_RESET).apply().build());
        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .executor(new ResetOwnNickCmd(messageController, playerController))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
