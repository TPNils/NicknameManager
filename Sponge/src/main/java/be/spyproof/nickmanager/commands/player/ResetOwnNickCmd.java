package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.controller.ISpongeNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Spyproof on 31/10/2016.
 */
public class ResetOwnNickCmd extends AbstractCmd implements IPlayerCmd
{
    private ResetOwnNickCmd(MessageController messageController, ISpongeNicknameController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(Player src, CommandContext args) throws CommandException
    {
        NicknameData nicknameData = this.getPlayerController().wrapPlayer(src);
        nicknameData.setNickname(null);
        this.getPlayerController().savePlayer(nicknameData);

        src.sendMessage(this.getMessageController().getMessage(Reference.SuccessMessages.NICK_RESET).apply().build());
        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongeNicknameController playerController)
    {
        return CommandSpec.builder()
                          .executor(new ResetOwnNickCmd(messageController, playerController))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
