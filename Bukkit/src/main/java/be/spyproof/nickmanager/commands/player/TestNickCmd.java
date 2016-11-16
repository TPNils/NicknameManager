package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class TestNickCmd extends AbstractPlayerCmd implements TabCompleter
{
    private static final String ARG = "nickname";

    public TestNickCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
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
        if (!src.hasPermission(Reference.Permissions.GENERIC_PLAYER_COMMANDS))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.NO_PERMISSION).replace("{permission}", Reference.Permissions.GENERIC_PLAYER_COMMANDS).split("\\n"));
            return;
        }

        if (args.length == 0 || args[0] == null)
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARG).split("\\n"));
            return;
        }

        String nick = args[0];

        // blacklist
        {
            Optional<String> blacklistRegex = BukkitUtils.INSTANCE.isBlacklisted(src, nick);
            if (blacklistRegex.isPresent())
            {
                src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.BLACKLIST).replace("{regex}", blacklistRegex.get()).split("\\n"));
                return;
            }
        }

        List<Colour> colours = StringUtils.getPresentColours(nick);
        List<Style> styles = StringUtils.getPresentStyles(nick);

        // colour amount
        if (colours.size() > BukkitUtils.INSTANCE.getConfigController().maxColours())
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.TO_MANY_COLOURS).replace("{amount}", "" + BukkitUtils.INSTANCE.getConfigController().maxColours()).split("\\n"));
            return;
        }

        if (styles.size() > BukkitUtils.INSTANCE.getConfigController().maxStyles())
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.TO_MANY_STYLES) .replace("{amount}", "" + BukkitUtils.INSTANCE.getConfigController().maxColours()).split("\\n"));
            return;
        }

        // colour permissions
        {   // Verify the player is allowed to use the colours
            Iterator<Colour> iterator = colours.iterator();
            while (iterator.hasNext())
                if (src.hasPermission(Reference.Permissions.COLOURS_PREFIX + iterator.next().name().toLowerCase()))
                    iterator.remove();
        }

        {   // Verify the player is allowed to use the styles
            Iterator<Style> iterator = styles.iterator();
            while (iterator.hasNext())
                if (src.hasPermission(Reference.Permissions.STYLE_PREFIX + iterator.next().name().toLowerCase()))
                    iterator.remove();
        }

        if (colours.size() + styles.size() > 0)
        {
            String illegalStyles = "";
            for (Colour colour : colours)
                illegalStyles += " &" + colour.getColourChar();
            for (Style style : styles)
                illegalStyles += " &" + style.getColourChar();
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.ILLEGAL_FORMAT).replace("{style}", illegalStyles).split("\\n"));
            return;
        }

        // nick length
        if (!BukkitUtils.INSTANCE.lengthCheck(nick))
        {
            String msg = this.messageController.getFormattedMessage(Reference.ErrorMessages.NICKNAME_TO_LONG);
            msg = msg.replace("{length-with-colour}", BukkitUtils.INSTANCE.getConfigController().maxNickLengthWithColor() + "");
            msg = msg.replace("{length-without-colour}", BukkitUtils.INSTANCE.getConfigController().maxNickLengthWithoutColor() + "");
            src.sendMessage(msg.split("\\n"));
            return;
        }

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
