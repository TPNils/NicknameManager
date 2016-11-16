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
public class ResetOtherTokensCmd extends AbstractCmd implements TabCompleter
{
    private static final String ARG = "player";

    public ResetOtherTokensCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.ADMIN_NICK_RESET_TOKENS));
    }

    @Override
    public void execute(CommandSender src, String cmd, String[] args)
    {
        if (!src.hasPermission(Reference.Permissions.ADMIN_RESET))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.NO_PERMISSION).replace("{permission}", Reference.Permissions.ADMIN_RESET).split("\\n"));
            return;
        }

        if (args.length == 0 || args[0] == null)
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARG).split("\\n"));
            return;
        }

        Optional<? extends PlayerData> playerData = this.playerController.getPlayer(args[0]);

        if (!playerData.isPresent())
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.WRONG_ARGUMENT).replace("{argument}", args[0]).split("\\n"));
            return;
        }

        playerData.get().setTokensRemaining(0);
        this.playerController.savePlayer(playerData.get());

        Player player = Bukkit.getPlayer(playerData.get().getUuid());
        if (player != null)
        {
            player.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.ADMIN_NICK_RESET_TOKENS_RECEIVED));
        }

        src.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.ADMIN_NICK_RESET_TOKENS), playerData.get()));
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
