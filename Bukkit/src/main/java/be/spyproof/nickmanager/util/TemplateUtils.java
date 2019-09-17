package be.spyproof.nickmanager.util;

import be.spyproof.nickmanager.model.NicknameData;
import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Spyproof on 05/11/2016.
 */
public class TemplateUtils {

  /**
   * Get all placeholders that can be used for a player.
   * <p>
   * nick_manager:player
   * see getFormattedPlayer(NicknameData)
   * nick_manager:player.name
   * The name of the player
   * nick_manager:player.uuid
   * The uuid of the player
   * nick_manager:player.tokens
   * The amount of token the player holds
   * nick_manager:player.lastchanged
   * The last time the player changed their nickname
   * nick_manager:player.nickname
   * The nickname of the player
   * nick_manager:player.pastNicknames
   * All nicknames the player had beforehand
   *
   * @param player All placeholder values will be read from the player
   *
   * @return A map with all the placeholder values associated with a player
   */
  public static Map<String, String> getParameters(NicknameData player) {
    Map<String, String> params = new HashMap<>();

    params.put("{player.name}", player.getName());
    params.put("{player.tokens}", player.getTokensRemaining() + "");
    params.put("{player.uuid}", player.getUuid().toString());
    params.put("{player.lastchanged}", SimpleDateFormat.getDateInstance().format(new Date(player.getLastChanged())));
    params.put("{player}", getFormattedPlayer(player));

    if (player.getNickname().isPresent()) {
      params.put("{player.nickname}", ChatColor.translateAlternateColorCodes('&', player.getNickname().get()));
    } else {
      params.put("{player.nickname}", "None");
    }

    if (player.getPastNicknames().size() > 0) {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < player.getPastNicknames().size(); i++) {
        builder.append(player.getPastNicknames().get(i));
        if (i + 1 < player.getPastNicknames().size()) {
          builder.append("\n");
        }
      }
      params.put("{player.pastNicknames}", ChatColor.translateAlternateColorCodes('&', builder.toString()));
    } else {
      params.put("{player.pastNicknames}", "None");
    }

    return params;
  }

  /**
   * Apply all placeholders that can be used for a player.
   * see getParameters(NicknameData)
   *
   * @param text   A String that contains placeholders that need to be filled
   * @param player All placeholder values will be read from the player
   *
   * @return A new String with filled text placeholders
   */
  public static String apply(String text, NicknameData player) {
    for (Map.Entry<String, String> entry : getParameters(player).entrySet()) {
      text = text.replace(entry.getKey(), entry.getValue());
    }

    return text;
  }

  /**
   * If the player has a nickname, return that, otherwise return his regular name
   *
   * @param player To get the nickname and normal name from
   *
   * @return The displayed name for the player
   */
  public static String getFormattedPlayer(NicknameData player) {
    if (player.getNickname().isPresent()) {
      return ChatColor.translateAlternateColorCodes('&', player.getNickname().get());
    } else {
      return player.getName();
    }
  }

}
