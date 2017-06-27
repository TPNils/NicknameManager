package be.spyproof.nickmanager.util;

import be.spyproof.nickmanager.model.NicknameData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spyproof on 15/11/2016.
 */
public class TabCompleteUtil
{
    public static List<String> getPlayers(String name)
    {
        List<String> names = new ArrayList<>();
        name = name.toLowerCase();

        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getName().toLowerCase().startsWith(name) || ChatColor.translateAlternateColorCodes('&', player.getDisplayName().toLowerCase()).contains(name))
                names.add(player.getName());

        return names;
    }

    public static List<String> getOldNicknames(NicknameData nicknameData, String nick)
    {
        List<String> names = new ArrayList<>();
        if (!nick.isEmpty())
            names.add(nick);

        nick = ChatColor.translateAlternateColorCodes('&', nick.toLowerCase());

        for (String oldNick : nicknameData.getPastNicknames())
            if (oldNick.replaceAll(Reference.COLOUR_AND_STYLE_PATTERN, "").startsWith(nick) && !names.contains(oldNick))
                names.add(oldNick);

        return names;
    }
}
