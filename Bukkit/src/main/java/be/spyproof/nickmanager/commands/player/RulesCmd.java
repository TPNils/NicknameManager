package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class RulesCmd extends AbstractCmd
{
    public RulesCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
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
        if (!src.hasPermission(Reference.Permissions.GENERIC_PLAYER_COMMANDS))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.NO_PERMISSION).replace("{permission}", Reference.Permissions.GENERIC_PLAYER_COMMANDS).split("\\n"));
            return;
        }

        PlayerData playerData = this.playerController.wrapPlayer((Player) src);
        src.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_RULES).replace("{command}", "/" + Reference.CommandKeys.ACCEPT_RULES[0]).split("\\n"));
        playerData.setReadRules(true);
    }
}
