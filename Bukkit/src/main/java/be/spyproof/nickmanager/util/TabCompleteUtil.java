package be.spyproof.nickmanager.util;

import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.model.NicknameData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Spyproof on 15/11/2016.
 */
public class TabCompleteUtil {

  private static final Pattern COLOUR_CODES_PATTERN = Pattern.compile("&[0-9A-FK-OR]");

  public static List<String> getPlayers(String name) {
    List<String> names = new ArrayList<>();
    name = name.toLowerCase();

    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player.getName().toLowerCase().startsWith(name) || ChatColor.translateAlternateColorCodes('&', player.getDisplayName().toLowerCase()).contains(name)) {
        names.add(player.getName());
      }
    }

    return names;
  }

  public static List<String> getPlayersByNickname(String nickname, IBukkitNicknameController playerController) {
    nickname = nickname.toLowerCase();
    List<String> matchingNicknames = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      NicknameData nicknameData = playerController.wrapPlayer(player);
      String playerNickname = nicknameData.getNickname().orElse(nicknameData.getName());
      String strippedPlayerNickname = COLOUR_CODES_PATTERN.matcher(playerNickname).replaceAll("");

      if (nickname.equals("") || strippedPlayerNickname.toLowerCase().startsWith(nickname) || playerNickname.toLowerCase().startsWith(nickname)) {
        matchingNicknames.add(strippedPlayerNickname);
      }
    }
    return matchingNicknames;
  }

  public static List<String> getOldNicknames(NicknameData nicknameData, String nick) {
    List<String> names = new ArrayList<>();
    if (!nick.isEmpty()) {
      names.add(nick);
    }

    nick = ChatColor.translateAlternateColorCodes('&', nick.toLowerCase());

    for (String oldNick : nicknameData.getPastNicknames()) {
      if (oldNick.replaceAll(Reference.COLOUR_AND_STYLE_PATTERN, "").startsWith(nick) && !names.contains(oldNick)) {
        names.add(oldNick);
      }
    }

    return names;
  }

}
