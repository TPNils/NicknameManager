package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.commands.checks.IPlayerCheck;
import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class AcceptRulesCmd extends AbstractCmd implements IPermissionCheck, IPlayerCheck
{
    public AcceptRulesCmd(MessageController messageController, IBukkitNicknameController playerController, String... keys)
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
        checkIsPlayer(src);
        checkPermission(src, Reference.Permissions.GENERIC_PLAYER_COMMANDS);

        NicknameData nicknameData = this.playerController.wrapPlayer((Player) src);

        if (!nicknameData.readRules())
            throw new CommandException(this.messageController.getFormattedMessage(Reference.ErrorMessages.MUST_READ_RULES).replace("{command}", "/nick " + Reference.CommandKeys.PLAYER_RULES[0]));

        nicknameData.setAcceptedRules(true);
        this.playerController.savePlayer(nicknameData);
        src.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.ACCEPTED_RULES).split("\\n"));
    }
}
