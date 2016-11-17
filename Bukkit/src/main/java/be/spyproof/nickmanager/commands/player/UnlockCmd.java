package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandSender;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class UnlockCmd extends AbstractCmd implements IPermissionCheck
{
    public UnlockCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.NICK_UNLOCK));
    }

    @Override
    public void execute(CommandSender src, String cmd, String[] args)
    {
        checkPermission(src, Reference.Permissions.GENERIC_PLAYER_COMMANDS);

        src.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_UNLOCK).split("\\n"));
    }
}
