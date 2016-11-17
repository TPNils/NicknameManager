package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.checks.*;
import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class SetNickCmd extends AbstractPlayerCmd implements TabCompleter, IBlacklistChecker, ICooldownChecker, IFormatChecker, ILengthChecker, ITokenChecker, IPermissionCheck
{
    private static final String ARG = "nickname";

    public SetNickCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.NICK_SET));
    }

    @Override
    public void execute(Player src, String cmd, String[] args)
    {
        checkPermission(src, Reference.Permissions.GENERIC_PLAYER_COMMANDS);

        if (args.length == 0 || args[0] == null)
            throw new CommandException(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARG));

        String nick = args[0];
        PlayerData playerData = this.playerController.wrapPlayer(src);

        checkTokens(playerData, src);
        checkCooldown(playerData, src);
        checkBlacklist(src, nick);
        checkFormat(src, nick);
        checkLength(nick);

        // Apply
        playerData.setNickname(nick);
        playerData.setLastChanged();
        if (!src.hasPermission(Reference.Permissions.BYPASS_CHANGE_LIMIT))
            playerData.setTokensRemaining(playerData.getTokensRemaining()-1);
        this.playerController.savePlayer(playerData);

        src.setDisplayName(ChatColor.translateAlternateColorCodes('&', nick) + ChatColor.RESET);
        src.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_SET), playerData).split("\\n"));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (strings.length == 1 && strings[0] != null && commandSender instanceof Player)
            return TabCompleteUtil.getOldNicknames(this.playerController.wrapPlayer((Player) commandSender), strings[0]);
        else
            return new ArrayList<>();
    }
}
