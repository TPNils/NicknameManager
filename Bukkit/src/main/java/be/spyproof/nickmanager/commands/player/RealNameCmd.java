package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TabCompleteUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class RealNameCmd extends AbstractCmd implements TabCompleter, IPermissionCheck
{
    private static final String ARG = "nickname";

    public RealNameCmd(MessageController messageController, IBukkitNicknameController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.NICK_REAL_NAME));
    }

    @Override
    public void execute(CommandSender src, String cmd, String[] args)
    {
        checkPermission(src, Reference.Permissions.GENERIC_PLAYER_COMMANDS);

        if (args.length == 0 || args[0] == null)
            throw new ClassCastException(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARG));

        String nickname = ChatColor.translateAlternateColorCodes('&', args[0]);
        List<NicknameData> matches = this.playerController.getPlayerByNickname(ChatColor.stripColor(nickname));

        StringBuilder players = new StringBuilder();
        if (matches.size() > 0)
        {
            for (int i = 0; i < matches.size(); i++)
            {
                players.append(matches.get(i).getName());
                if (i + 1 < matches.size())
                    players.append("\n");

            }
        } else
        {
            players.append("None");
        }

        src.sendMessage(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_REAL_NAME).replace("{players}", players.toString()).replace("{nickname}", nickname).split("\\n"));
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
