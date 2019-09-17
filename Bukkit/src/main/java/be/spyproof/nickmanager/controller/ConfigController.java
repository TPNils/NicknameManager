package be.spyproof.nickmanager.controller;

import be.spyproof.nickmanager.da.config.IConfigStorage;
import be.spyproof.nickmanager.da.player.GsonPlayerStorage;
import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.da.player.MySqlPlayerStorage;
import be.spyproof.nickmanager.util.DateUtil;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Spyproof on 09/11/2016.
 */
public class ConfigController implements IConfigStorage {

  private File file;
  private YamlConfiguration configuration;

  public ConfigController(File file) {
    this.file = file;
  }

  @Override
  public void load() throws Exception {
    this.configuration = YamlConfiguration.loadConfiguration(this.file);
    boolean saveConfig = false;
    if (!this.configuration.contains("setTabListName")) {
      this.configuration.set("setTabListName", true);
      saveConfig = true;
    }

    // Fixing an issue i caused
    if (this.configuration.contains("setTablistName")) {
      this.configuration.set("setTablistName", null);
      saveConfig = true;
    }


    if (saveConfig) {
      this.configuration.save(this.file);
    }
  }

  public String getLanguage() {
    return this.configuration.getString("language", "english");
  }

  @Override
  public boolean mustAcceptRules() {
    return this.configuration.getBoolean("mustAcceptRules", true);
  }

  @Override
  public int maxColours() {
    return this.configuration.getInt("maxcolors", 4);
  }

  @Override
  public int maxStyles() {
    return this.configuration.getInt("maxeffects", 2);
  }

  @Override
  public int maxNickLengthWithColour() {
    return this.configuration.getInt("maxlength.withColour", 48);
  }

  @Override
  public int maxNickLengthWithoutColour() {
    return this.configuration.getInt("maxlength.withoutColour", 24);
  }

  @Override
  public IPlayerStorage getPlayerStorage() throws Exception {
    String storageType = this.configuration.getString("storage", "yml");
    if (storageType.equalsIgnoreCase("mysql")) {
      try {
        return new MySqlPlayerStorage(this.configuration.getString("mysql.host", "127.0.0.1"),
          this.configuration.getInt("mysql.port", 3306),
          this.configuration.getString("mysql.database", "nicknames"),
          this.configuration.getString("mysql.user", "root"),
          this.configuration.getString("mysql.password", ""));
      } catch (SQLException | IOException e) {
        System.out.println("Could not load MySQL storage for " + Reference.MetaData.PLUGIN_ID + "! Check if you entered the right information in the config file.");
        throw e;
      }
    }

    return new GsonPlayerStorage(new File(this.file.getParentFile(), "players"));
  }

  @Override
  public Map<String, Long> getCooldowns() {
    Map<String, Long> cooldowns = new HashMap<>();

    for (Map.Entry<String, Object> entry : this.configuration.getConfigurationSection("cooldown").getValues(true).entrySet()) {
      try {
        Object value = entry.getValue();
        if (value == null) {
          value = "1mo";
        }
        cooldowns.put(entry.getKey(), DateUtil.parseDateDiff(value.toString(), true) - System.currentTimeMillis());
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      }
    }

    return cooldowns;
  }

  @Override
  public List<String> getBlacklist() {
    return this.configuration.getStringList("blacklist");
  }

  @Override
  public boolean setTabListName() {
    return this.configuration.getBoolean("setTabListName", true);
  }

}
