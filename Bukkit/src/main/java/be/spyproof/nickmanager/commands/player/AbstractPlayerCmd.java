package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IPlayerCheck;
import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.BukkitUtils;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Spyproof on 14/11/2016.
 */
public abstract class AbstractPlayerCmd extends AbstractCmd implements IPlayerCheck
{
    protected AbstractPlayerCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void execute(CommandSender src, String cmd, String[] args)
    {
        if (!(src instanceof Player))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.PLAYER_ONLY).split("\\n"));
            return;
        }

        if (!BukkitUtils.INSTANCE.acceptedRules((Player) src))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MUST_ACCEPT_RULES).replace("{command}", Reference.CommandKeys.ACCEPT_RULES[0]).split("\\n"));
            return;
        }

        execute((Player) src, cmd, args);
    }

    public abstract void execute(Player src, String cmd, String[] args);
}
