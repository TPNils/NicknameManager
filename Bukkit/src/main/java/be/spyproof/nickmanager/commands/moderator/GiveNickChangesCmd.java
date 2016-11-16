package be.spyproof.nickmanager.commands.moderator;


import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TabCompleteUtil;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Spyproof on 15/11/2016.
 */
public class GiveNickChangesCmd extends AbstractCmd implements TabCompleter
{
    private static final String[] ARGS = new String[]{"player", "amount"};

    public GiveNickChangesCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.ADMIN_NICK_GIVE));
    }

    @Override
    public void execute(CommandSender src, String cmd, String[] args)
    {
        if (!src.hasPermission(Reference.Permissions.ADMIN_GIVE))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.NO_PERMISSION).replace("{permission}", Reference.Permissions.ADMIN_GIVE).split("\\n"));
            return;
        }

        if (args.length == 0 || args[0] == null)
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARGS[0]).split("\\n"));
            return;
        }

        if (args.length == 1)
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.WRONG_ARGUMENT).replace("{argument}", args[0]).split("\\n"));
            return;
        }

        Optional<? extends PlayerData> playerData = this.playerController.getPlayer(args[0]);
        int amount;
        try{
            amount = Integer.parseInt(args[1]);
        }catch (NumberFormatException e) {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.WRONG_ARGUMENT).replace("{argument}", args[1]).split("\\n"));
            return;
        }

        if (!playerData.isPresent())
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARGS[0]).split("\\n"));
            return;
        }

        playerData.get().setTokensRemaining(amount + playerData.get().getTokensRemaining());
        this.playerController.savePlayer(playerData.get());

        Player player = Bukkit.getPlayer(playerData.get().getUuid());
        if (player != null)
        {
            player.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.ADMIN_NICK_GIVE_RECEIVED).replace("{tokens}", amount + ""));
        }

        src.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.ADMIN_NICK_GIVE), playerData.get()).replace("{tokens}", "" + amount));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (strings.length == 1 && strings[0] != null)
            return TabCompleteUtil.getPlayers(strings[0]);
        else
            return new ArrayList<>();
    }
}
