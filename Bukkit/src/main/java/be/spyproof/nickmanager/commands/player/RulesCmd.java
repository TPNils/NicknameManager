package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class RulesCmd extends AbstractCmd implements IPermissionCheck
{
    public RulesCmd(MessageController messageController, IBukkitNicknameController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.NICK_RULES));
    }

    @Override
    public void execute(CommandSender src, String cmd, String[] args)
    {
        checkPermission(src, Reference.Permissions.GENERIC_PLAYER_COMMANDS);

        NicknameData nicknameData = this.playerController.wrapPlayer((Player) src);
        src.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_RULES).replace("{command}", "/" + Reference.CommandKeys.ACCEPT_RULES[0]).split("\\n"));
        nicknameData.setReadRules(true);
    }
}
