package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class SetNickCmd extends AbstractPlayerCmd implements TabCompleter
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
        PlayerData playerData = this.playerController.wrapPlayer(src);

        // Has tokens
        if (!BukkitUtils.INSTANCE.canChangeNickname(playerData, src))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_TOKENS).split("\\n"));
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.UNLOCK_TOKENS).replace("{command}", "/nick " + Reference.CommandKeys.PLAYER_UNLOCK[0]).split("\\n"));
            return;
        }

        // cooldowns
        long cooldown;
        if (src.hasPermission(Reference.Permissions.BYPASS_COOLDOWN))
        {
            cooldown = 0;
        }else
        {
            cooldown = BukkitUtils.INSTANCE.getDefaultCooldown();
            for (Map.Entry<String, Long> entry : BukkitUtils.INSTANCE.getExtraCooldowns().entrySet())
            {
                if (src.hasPermission(Reference.Permissions.COOLDOWN_PREFIX + entry.getKey()) && entry.getValue() < cooldown)
                    cooldown = entry.getValue();
            }
        }

        long timeDiff = playerData.getLastChanged() + cooldown - System.currentTimeMillis();
        if (timeDiff > 0)
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.ON_COOLDOWN).replace("{time}", DateUtil.timeformat(timeDiff)).split("\\n"));
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
        if (colours.size() > BukkitUtils.INSTANCE.getConfigController().maxColours() && !src.hasPermission(Reference.Permissions.BYPASS_COLOUR_LIMIT))
        {
            src.sendMessage(this.messageController.getFormattedMessage(Reference.ErrorMessages.TO_MANY_COLOURS).replace("{amount}", "" + BukkitUtils.INSTANCE.getConfigController().maxColours()).split("\\n"));
            return;
        }

        if (styles.size() > BukkitUtils.INSTANCE.getConfigController().maxStyles() && !src.hasPermission(Reference.Permissions.BYPASS_STYLE_LIMIT))
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
            msg = msg.replace("{length-with-colour}", BukkitUtils.INSTANCE.getConfigController().maxNickLengthWithColour() + "");
            msg = msg.replace("{length-without-colour}", BukkitUtils.INSTANCE.getConfigController().maxNickLengthWithoutColour() + "");
            src.sendMessage(msg.split("\\n"));
            return;
        }

        // Apply
        playerData.setNickname(nick);
        playerData.setLastChanged();
        src.setDisplayName(ChatColor.translateAlternateColorCodes('&', nick) + ChatColor.RESET);
        src.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_SET), playerData).split("\\n"));
        if (!src.hasPermission(Reference.Permissions.BYPASS_CHANGE_LIMIT))
            playerData.setTokensRemaining(playerData.getTokensRemaining()-1);
        this.playerController.savePlayer(playerData);
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
