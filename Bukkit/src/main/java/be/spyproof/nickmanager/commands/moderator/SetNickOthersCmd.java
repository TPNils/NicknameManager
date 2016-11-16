package be.spyproof.nickmanager.commands.moderator;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.*;
import org.bukkit.Bukkit;
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
 * Created by Spyproof on 15/11/2016.
 */
public class SetNickOthersCmd extends AbstractCmd implements TabCompleter
{
    private static final String[] ARGS = new String[]{"player", "nickname"};

    public SetNickOthersCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.ADMIN_NICK_SET));
    }

    @Override
    public void execute(CommandSender src, String cmd, String[] args)
    {
        if (!src.hasPermission(Reference.Permissions.ADMIN_SET))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.NO_PERMISSION).replace("{permission}", Reference.Permissions.ADMIN_SET).split("\\n"));
            return;
        }

        if (args.length == 0 || args[0] == null)
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARGS[0]).split("\\n"));
            return;
        }

        if (args.length == 1)
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARGS[1]).split("\\n"));
            return;
        }

        Optional<? extends PlayerData> player = this.playerController.getPlayer(args[0]);
        String nick = args[1];

        if (!player.isPresent())
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.WRONG_ARGUMENT).replace("{argument}", args[0]).split("\\n"));
            return;
        }

        if (src instanceof Player && player.get().getUuid().equals(((Player) src).getUniqueId()))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.CANT_FORCE_CHANGE_OWN_NICK));
            return;
        }

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
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.ILLEGAL_FORMAT).replace("{style}", illegalStyles));
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

        // Apply
        player.get().setNickname(nick);
        player.get().setLastChanged();
        this.playerController.savePlayer(player.get());
        src.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.ADMIN_NICK_SET), player.get()));
        Player receiver = Bukkit.getPlayer(player.get().getUuid());
        if (receiver != null)
        {
            receiver.setDisplayName(ChatColor.translateAlternateColorCodes('&', nick) + ChatColor.RESET);
            receiver.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_SET), player.get()));
        }
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
