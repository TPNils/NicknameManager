package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.checks.IBlacklistChecker;
import be.spyproof.nickmanager.commands.checks.IFormatChecker;
import be.spyproof.nickmanager.commands.checks.ILengthChecker;
import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TabCompleteUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class TestNickCmd extends AbstractPlayerCmd implements TabCompleter, IBlacklistChecker, IFormatChecker, ILengthChecker, IPermissionCheck
{
    private static final String ARG = "nickname";

    public TestNickCmd(MessageController messageController, IBukkitNicknameController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.NICK_PREVIEW));
    }

    @Override
    public void execute(Player src, String cmd, String[] args)
    {
        checkPermission(src, Reference.Permissions.GENERIC_PLAYER_COMMANDS);

        if (args.length == 0 || args[0] == null)
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARG).split("\\n"));
            return;
        }

        String nick = args[0];

        checkBlacklist(src, nick);
        checkFormat(src, nick);
        checkLength(nick);

        src.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_PREVIEW).replace("{nickname}", ChatColor.translateAlternateColorCodes('&', nick)).split("\\n"));
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
