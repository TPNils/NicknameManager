package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class ResetOwnNickCmd extends AbstractPlayerCmd implements IPermissionCheck
{
    public ResetOwnNickCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.NICK_RESET));
    }

    @Override
    public void execute(Player src, String cmd, String[] args)
    {
        checkPermission(src, Reference.Permissions.GENERIC_PLAYER_COMMANDS);

        PlayerData playerData = this.playerController.wrapPlayer(src);
        playerData.setNickname(null);
        this.playerController.savePlayer(playerData);

        src.setDisplayName(src.getName());
        src.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_RESET).split("\\n"));
    }
}
