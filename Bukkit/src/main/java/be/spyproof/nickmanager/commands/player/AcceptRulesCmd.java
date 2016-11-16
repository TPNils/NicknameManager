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
public class AcceptRulesCmd extends AbstractCmd
{
    public AcceptRulesCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.ACCEPT_NICKNAME));
    }

    @Override
    public void execute(CommandSender src, String cmd, String[] args)
    {
        if (!(src instanceof Player))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.PLAYER_ONLY).split("\\n"));
            return;
        }

        if (!src.hasPermission(Reference.Permissions.GENERIC_PLAYER_COMMANDS))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.NO_PERMISSION).replace("{permission}", Reference.Permissions.GENERIC_PLAYER_COMMANDS).split("\\n"));
            return;
        }

        PlayerData playerData = this.playerController.wrapPlayer((Player) src);

        if (!playerData.readRules())
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MUST_READ_RULES).replace("{command}", "/nick " + Reference.CommandKeys.PLAYER_RULES[0]).split("\\n"));
            return;
        }

        playerData.setAcceptedRules(true);
        this.playerController.savePlayer(playerData);
        src.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.ACCEPTED_RULES).split("\\n"));
    }
}
