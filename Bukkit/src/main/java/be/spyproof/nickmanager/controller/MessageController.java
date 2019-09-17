package be.spyproof.nickmanager.controller;

import be.spyproof.nickmanager.util.Reference.CommandKeys;
import be.spyproof.nickmanager.util.Reference.ErrorMessages;
import be.spyproof.nickmanager.util.Reference.HelpMessages;
import be.spyproof.nickmanager.util.Reference.SuccessMessages;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Spyproof on 14/11/2016.
 */
public class MessageController {

  private File file;
  private String language;
  private YamlConfiguration configuration;

  public MessageController(File file, String language) {
    this.file = file;
    setLanguage(language);
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void load() throws Exception {
    if (!this.file.exists()) {
      File parent = this.file.getParentFile();
      if (parent != null && !parent.exists()) {
        parent.mkdirs();
      }
      this.file.createNewFile();
    }

    this.configuration = YamlConfiguration.loadConfiguration(this.file);
    saveDefaults();
    if (this.language == null) {
      this.language = this.configuration.getKeys(false).iterator().next();
    }
  }

  public void save() throws IOException {
    this.configuration.save(this.file);
  }

  public void saveDefaults() throws IOException {
    // All failure messages
    addDefault("english", ErrorMessages.BLACKLIST, "&cYour nickname is now allowed because it matches: &o{regex}");
    addDefault("english", ErrorMessages.CANT_FORCE_CHANGE_OWN_NICK, "&cYou can't force change your own nickname");
    addDefault("english", ErrorMessages.ILLEGAL_FORMAT, "&cYou are not allows to use the following styles: &o{style}");
    addDefault("english", ErrorMessages.MISSING_TOKENS, "&cYou do not own any nickname tokens");
    addDefault("english", ErrorMessages.MISSING_ARGUMENT, "&cMissing argument: &o{argument}");
    addDefault("english", ErrorMessages.MUST_ACCEPT_RULES, "&cYou must accept the rules first with &o{command}");
    addDefault("english", ErrorMessages.MUST_READ_RULES, "&cYou must read the rules first with &o{command}");
    addDefault("english", ErrorMessages.NICKNAME_TO_LONG, "&cYour nickname can only be &o{length-with-colour}&c characters long with color codes and &o{length-without-colour}&c characters long without color codes");
    addDefault("english", ErrorMessages.ON_COOLDOWN, "&cYou need to wait &o{time}&c before you can change your nickname");
    addDefault("english", ErrorMessages.NO_PERMISSION, "&cYou dont have permission for this");
    addDefault("english", ErrorMessages.PLAYER_ONLY, "&cThis command can only be used by players");
    addDefault("english", ErrorMessages.TO_MANY_COLOURS, "&cYour nickname can only have &o{amount}&c color(s)");
    addDefault("english", ErrorMessages.TO_MANY_STYLES, "&cYour nickname can only have &o{amount}&c style(s)");
    addDefault("english", ErrorMessages.UNLOCK_TOKENS, "&cUse &o{command}&c for more information");

    // All successful /IAcceptTheNicknameRules messages
    addDefault("english", SuccessMessages.ACCEPTED_RULES, "&aYou have accepted the rules");

    // All successful /admnick messages
    addDefault("english", SuccessMessages.ADMIN_NICK_GIVE, "&aYou have given {tokens} nickname tokens to &r{player.name}");
    addDefault("english", SuccessMessages.ADMIN_NICK_GIVE_RECEIVED, "&aYou have received {tokens} nickname tokens. Use /nick for more information");
    addDefault("english", SuccessMessages.ADMIN_NICK_SET, "&aYou have changed the nickname of &r{player.name}&a to &r{player.nickname}");
    addDefault("english", SuccessMessages.ADMIN_NICK_RESET_NICKNAME, "&aReset the nickname of &r{player.name}");
    addDefault("english", SuccessMessages.ADMIN_NICK_RESET_TOKENS, "&aRemoved all nickname tokens of &r{player.name}");
    addDefault("english", SuccessMessages.ADMIN_NICK_RESET_TOKENS_RECEIVED, "&cYour nickname tokens has been removed");
    addDefault("english", SuccessMessages.ADMIN_NICK_RESET_COOLDOWN, "&aReset the nickname cooldown of &r{player.name}");
    addDefault("english", SuccessMessages.ADMIN_NICK_RESET_COOLDOWN_RECEIVED, "&aYour nickname cooldown has been reset");
    addDefault("english", SuccessMessages.ADMIN_NICK_RESET_RULES, "&r{player.name}&a needs to accept the rules again");
    addDefault("english", SuccessMessages.ADMIN_NICK_RESET_RULES_RECEIVED, "&cYou are now forced to accept the nickname rules again");
    addDefault("english", SuccessMessages.ADMIN_NICK_CHECK,
      "&aCurrent nickname: &r{player.nickname}",
      "&aUUID: &3{player.uuid}",
      "&aTokens remaining: &3{player.tokens}",
      "&aPast nicknames: &r{player.pastNicknames}");

    // All successful /nick messages
    addDefault("english", SuccessMessages.NICK_SET, "&aYour nickname has been changed to &r{player.nickname}");
    addDefault("english", SuccessMessages.NICK_CHECK,
      "&aYour current nickname: &r{player.nickname}",
      "&aNickname tokens remaining: &3{player.tokens}");
    addDefault("english", SuccessMessages.NICK_REAL_NAME, "&aFound the following players matching the nickname &r{nickname}", "{players}");
    addDefault("english", SuccessMessages.NICK_RESET, "&cYour nickname has been reset");
    addDefault("english", SuccessMessages.NICK_RULES,
      "&6==========> &a&lRules&6 <==========",
      "&6&l1. &eDon't use any offensive/racist/sexist names",
      "&6&l2. &eDon't impersonate players or staff",
      "&6&l3. &eUse common sense",
      "&6&l4. &eStaff will have the final judgment",
      "&cIf you break these rules, actions will be taken!",
      "&cUse {command}  to accept these rules");
    addDefault("english", SuccessMessages.NICK_PREVIEW, "&aYour nickname: &r{nickname}");
    addDefault("english", SuccessMessages.NICK_UNLOCK,
      "&6==========> &a&lUnlock&6 <==========",
      "&eNickname tokens allow you to change your nickname",
      "&eThat nickname will be synced on all our servers",
      "&eYou can buy nickname tokens @",
      "&ehttp://omfgdogs.com/");


    // All help messages
    addDefault("english", HelpMessages.NICK_SET, "&b/nick&a " + CommandKeys.PLAYER_SET[0] + " <nickname> &r-&e Change your current nickname at the cost of 1 nickname token");
    addDefault("english", HelpMessages.NICK_PREVIEW, "&b/nick&a " + CommandKeys.PLAYER_PREVIEW[0] + " <nickname> &r-&e Preview your nickname");
    addDefault("english", HelpMessages.NICK_CHECK, "&b/nick&a " + CommandKeys.PLAYER_CHECK[0] + " &r-&e See how many nickname tokens you have");
    addDefault("english", HelpMessages.NICK_FORMAT, "&b/nick&a " + CommandKeys.PLAYER_FORMAT[0] + " &r-&e Check how to apply colors and styles to your nickname");
    addDefault("english", HelpMessages.NICK_REAL_NAME, "&b/nick&a " + CommandKeys.PLAYER_REAL_NAME[0] + " <player> &r-&e Try to find the real name of a player's nickname");
    addDefault("english", HelpMessages.NICK_RESET, "&b/nick&a " + CommandKeys.PLAYER_RESET[0] + " &r-&e Remove your current nickname, does not cost any nickname tokens");
    addDefault("english", HelpMessages.NICK_RULES, "&b/nick&a " + CommandKeys.PLAYER_RULES[0] + " &r-&e Read the rules regarding choosing a nickname");
    addDefault("english", HelpMessages.NICK_UNLOCK, "&b/nick&a " + CommandKeys.PLAYER_UNLOCK[0] + " &r-&e Information on how to obtain a nickname token");
    addDefault("english", HelpMessages.ADMIN_NICK_CHECK, "&b/admnick&a " + CommandKeys.ADMIN_CHECK[0] + " <player> &r-&e See information about the player");
    addDefault("english", HelpMessages.ADMIN_NICK_GIVE, "&b/admnick&a " + CommandKeys.ADMIN_GIVE[0] + " <player> <amount> &r-&e Give the player a nickname token");
    addDefault("english", HelpMessages.ADMIN_NICK_SET, "&b/admnick&a " + CommandKeys.ADMIN_SET[0] + " <player> <nickname> &r-&e Change the player's nickname");
    addDefault("english", HelpMessages.ADMIN_NICK_RESET_COOLDOWN, "&b/admnick&a reset " + CommandKeys.ADMIN_RESET_COOLDOWN[0] + " <player> &r-&e Reset the player's cooldown");
    addDefault("english", HelpMessages.ADMIN_NICK_RESET_NICKNAME, "&b/admnick&a reset " + CommandKeys.ADMIN_RESET_NICKNAME[0] + " <player> &r-&e Reset the player's nickname");
    addDefault("english", HelpMessages.ADMIN_NICK_RESET_RULES, "&b/admnick&a reset " + CommandKeys.ADMIN_RESET_RULES[0] + " <player> &r-&e Force the player to accept the rules again");
    addDefault("english", HelpMessages.ADMIN_NICK_RESET_TOKENS, "&b/admnick&a reset " + CommandKeys.ADMIN_RESET_TOKENS[0] + " <player> &r-&e Reset the player's tokens");
    save();
  }

  public String getFormattedMessage(String... path) {
    return ChatColor.translateAlternateColorCodes('&', getRawMessage(path));
  }

  public String getRawMessage(String... path) {
    String pathString = getPath(this.language, path);
    if (this.configuration.isList(pathString)) {
      StringBuilder builder = new StringBuilder();
      List<String> messages = this.configuration.getStringList(pathString);
      for (int i = 0; i < messages.size(); i++) {
        builder.append(messages.get(i));
        if (i + 1 < messages.size()) {
          builder.append("\n");
        }
      }
      return builder.toString();
    } else {
      return this.configuration.getString(pathString, '{' + pathString + '}');
    }
  }

  protected void addDefault(String language, String[] path, Object value) {
    String pathString = getPath(language, path);

    if (!this.configuration.isSet(pathString)) {
      this.configuration.set(pathString, value);
    }
  }

  protected void addDefault(String language, String[] path, Object... value) {
    String pathString = getPath(language, path);

    if (!this.configuration.isSet(pathString)) {
      this.configuration.set(pathString, Arrays.asList(value));
    }
  }

  protected String getPath(String language, String[] path) {
    String pathString = language;
    for (String aPath : path) {
      pathString += '.' + aPath;
    }
    return pathString;
  }

}
