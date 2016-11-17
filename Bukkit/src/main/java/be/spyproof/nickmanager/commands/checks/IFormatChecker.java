package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.*;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface IFormatChecker extends IMessageControllerHolder
{
    default void checkFormat(CommandSender src, String nickname) throws CommandException
    {
        List<Colour> colours = StringUtils.getPresentColours(nickname);
        List<Style> styles = StringUtils.getPresentStyles(nickname);

        // colour amount
        if (colours.size() > BukkitUtils.INSTANCE.getConfigController().maxColours() && !src.hasPermission(Reference.Permissions.BYPASS_COLOUR_LIMIT))
            throw new CommandException(this.getMessageController().getFormattedMessage(Reference.ErrorMessages.TO_MANY_COLOURS)
                                         .replace("{amount}", BukkitUtils.INSTANCE.getConfigController().maxColours() + ""));

        if (styles.size() > BukkitUtils.INSTANCE.getConfigController().maxStyles() && !src.hasPermission(Reference.Permissions.BYPASS_STYLE_LIMIT))
            throw new CommandException(this.getMessageController().getFormattedMessage(Reference.ErrorMessages.TO_MANY_STYLES)
                                         .replace("{amount}", BukkitUtils.INSTANCE.getConfigController().maxColours() + ""));

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
            throw new CommandException(this.getMessageController().getFormattedMessage(Reference.ErrorMessages.ILLEGAL_FORMAT)
                                         .replace("{style}", illegalStyles));
        }
    }
}
