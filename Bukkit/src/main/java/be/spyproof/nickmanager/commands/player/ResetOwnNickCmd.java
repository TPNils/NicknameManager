package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.BukkitUtils;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class ResetOwnNickCmd extends AbstractPlayerCmd implements IPermissionCheck
{
    public ResetOwnNickCmd(MessageController messageController, IBukkitNicknameController playerController, String... keys)
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

        NicknameData nicknameData = this.playerController.wrapPlayer(src);
        nicknameData.setNickname(null);
        this.playerController.savePlayer(nicknameData);

        BukkitUtils.INSTANCE.applyNickname(nicknameData, src);
        src.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_RESET).split("\\n"));
    }
}
