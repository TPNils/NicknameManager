package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitPlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class DisplayFormatsCmd extends AbstractCmd implements IPermissionCheck
{
    public DisplayFormatsCmd(MessageController messageController, IBukkitPlayerController playerController, String... keys)
    {
        super(messageController, playerController, keys);
    }

    private String getColour(ChatColor color)
    {
        return color.toString() + "&" + color.getChar() + ChatColor.RESET;
    }

    @Override
    public void sendHelpMsg(CommandSender src)
    {
        src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.NICK_FORMAT));
    }

    @Override
    public void execute(CommandSender src, String s, String[] strings)
    {
        checkPermission(src, Reference.Permissions.GENERIC_PLAYER_COMMANDS);

        StringBuilder builder = new StringBuilder();

        builder.append(getColour(ChatColor.BLACK));
        builder.append(" ");
        builder.append(getColour(ChatColor.DARK_BLUE));
        builder.append(" ");
        builder.append(getColour(ChatColor.DARK_GREEN));
        builder.append(" ");
        builder.append(getColour(ChatColor.DARK_AQUA));
        builder.append("\n");
        builder.append(getColour(ChatColor.DARK_RED));
        builder.append(" ");
        builder.append(getColour(ChatColor.DARK_PURPLE));
        builder.append(" ");
        builder.append(getColour(ChatColor.GOLD));
        builder.append(" ");
        builder.append(getColour(ChatColor.GRAY));
        builder.append("\n");
        builder.append(getColour(ChatColor.DARK_GRAY));
        builder.append(" ");
        builder.append(getColour(ChatColor.BLUE));
        builder.append(" ");
        builder.append(getColour(ChatColor.GREEN));
        builder.append(" ");
        builder.append(getColour(ChatColor.AQUA));
        builder.append("\n");
        builder.append(getColour(ChatColor.RED));
        builder.append(" ");
        builder.append(getColour(ChatColor.LIGHT_PURPLE));
        builder.append(" ");
        builder.append(getColour(ChatColor.YELLOW));
        builder.append(" ");
        builder.append(getColour(ChatColor.WHITE));

        builder.append("\n").append("\n");

        builder.append(getColour(ChatColor.BOLD));
        builder.append(" ");
        builder.append(getColour(ChatColor.ITALIC));
        builder.append("\n");
        builder.append(getColour(ChatColor.STRIKETHROUGH));
        builder.append(" ");
        builder.append(getColour(ChatColor.UNDERLINE));
        builder.append("\n");
        builder.append("&k > ").append(ChatColor.MAGIC).append("Obfuscated");

        src.sendMessage(builder.toString().split("\\n"));
    }
}
