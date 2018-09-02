package be.spyproof.nickmanager.controller;

import be.spyproof.nickmanager.da.config.IConfigStorage;
import be.spyproof.nickmanager.da.player.GsonPlayerStorage;
import be.spyproof.nickmanager.da.player.IPlayerStorage;
import be.spyproof.nickmanager.da.player.MySqlPlayerStorage;
import be.spyproof.nickmanager.util.DateUtil;
import be.spyproof.nickmanager.util.Reference;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class ConfigController implements IConfigStorage
{
    private static final int VERSION = 2;
    private File file;

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode root;

    public ConfigController(File file)
    {
        this.file = file;
    }

    public void load() throws IOException
    {
        boolean saveDefaults = true;
        if (this.file.exists())
            saveDefaults = false;
        else
            this.file.createNewFile();

        this.loader = HoconConfigurationLoader.builder().setFile(this.file).build();
        this.root = this.loader.load();

        if (saveDefaults)
            saveDefaults();

        {
            boolean requiresSave = false;
            CommentedConfigurationNode configVersion = this.root.getNode("config version");
            if (!configVersion.getComment().isPresent())
            {
                configVersion.setComment("Do not change this number!");
                requiresSave = true;
            }
            if (configVersion.getInt() == 1) {
                this.root.getNode("Set tab list name").setValue(true).setComment("Player nicknames will also appear in the tab list.");
                requiresSave = true;
            }
            if (configVersion.getInt() != VERSION) {
                configVersion.setValue(VERSION);
                requiresSave = true;
            }

            if (requiresSave) {
                this.loader.save(this.root);
            }
        }
    }

    @Override
    public String getLanguage()
    {
        return this.root.getNode("Language").getString("english");
    }

    public void saveDefaults() throws IOException
    {
        this.root.getNode("Set tab list name").setValue(true).setComment("Player nicknames will also appear in the tab list.");
        this.root.getNode("Must accept rules").setValue(true).setComment("Decide if players need to accept the rules first before getting access to the /nick commands");
        this.root.getNode("Max colours").setValue(4).setComment("Maximum colour combinations per nickname, there are 16 colours");
        this.root.getNode("Max styles").setValue(2).setComment("Maximum styles combinations per nickname, there are 5 colours\n(bold, strikethrough, underline, italic, obfuscated)");
        this.root.getNode("Storage").setValue("json").setComment("Choose to save player data in json files (local) or in MySQL (database)");
        this.root.getNode("Language").setValue("english").setComment("Choose which language should be used.");
        {
            CommentedConfigurationNode node = this.root.getNode("Nickname length");
            node.setComment("The absolute max length is 255 characters, including colour characters!\nSpecifying any larger number in this category will be ignored.");
            node.getNode("With colour").setValue(48).setComment("The max length of a nickname including the colour characters\nExample: &1&lNotch = 9");
            node.getNode("Without colour").setValue(24).setComment("The max length of a nickname excluding the colour characters\nExample: &1&lNotch = 5");
        }

        {
            CommentedConfigurationNode node = this.root.getNode("config version");
            node.setComment("Do not change this number!");
            node.setValue(VERSION);
        }

        {
            CommentedConfigurationNode node = this.root.getNode("Blacklist");
            List<String> regex = new ArrayList<>();

            regex.add("[o0]wn[e3]r");
            regex.add("f[o0]und[e3]r");
            regex.add("adm[i1]n");
            regex.add("m[o0]d([e3]r[a4]t[o0]r)?");
            regex.add("h[e3]lp[e3]r");
            regex.add("n[i1]g+[e3]r");
            regex.add("n[i1]g+[a4]r*");
            regex.add("f[a4]g+[o0]t");
            regex.add("r[a4]p[i1]st");
            regex.add("n[a4]z[i1]");
            regex.add("sh[i1]t");
            regex.add("y[o0]utub[e3]r?");
            regex.add("str[e3][a4]m([e3]r)?");
            regex.add("n[o0]tch");

            node.setValue(regex).setComment("Banned nicknames with regex syntax (not case sensitive)");
        }

        {
            CommentedConfigurationNode node = this.root.getNode("Cooldown");
            node.setComment("Cooldowns for changing your nickname\nFormat: years(y), months(mo), weeks(w), days(d), hours(h), minutes(m), seconds(s)\nRequires a default!\nOverride defaults with " + Reference.Permissions.COOLDOWN_PREFIX + "cooldown_name");
            node.getNode("default").setValue("1mo");
            node.getNode("VIP").setValue("1w");
            node.getNode("Donator").setValue("1d");
        }

        {
            CommentedConfigurationNode node = this.root.getNode("MySQL");
            node.setComment("Database to store the synced nicknames, if the storage is MySQL");
            node.getNode("Host").setValue("127.0.0.1");
            node.getNode("Database").setValue("nicknames");
            node.getNode("Port").setValue(3306);
            node.getNode("User").setValue("Derp");
            node.getNode("Password").setValue("MyUberPassWord");
        }

        this.loader.save(this.root);
    }

    @Override
    public boolean mustAcceptRules()
    {
        return this.root.getNode("Must accept rules").getBoolean(true);
    }

    @Override
    public int maxColours()
    {
        return this.root.getNode("Max colours").getInt(4);
    }

    @Override
    public int maxStyles()
    {
        return this.root.getNode("Max styles").getInt(2);
    }

    @Override
    public int maxNickLengthWithColour()
    {
        return this.root.getNode("Nickname length", "With colour").getInt(48);
    }

    @Override
    public int maxNickLengthWithoutColour()
    {
        return this.root.getNode("Nickname length", "Without colour").getInt(24);
    }

    @Override
    public IPlayerStorage getPlayerStorage() throws Exception
    {
        String storageType = this.root.getNode("Storage").getString("json");
        if (storageType.equalsIgnoreCase("nickname_manager/mysql"))
        {
            try
            {
                return new MySqlPlayerStorage(this.root.getNode("MySQL", "Host").getString("127.0.0.1"),
                                              this.root.getNode("MySQL", "Port").getInt(3306),
                                              this.root.getNode("MySQL", "Database").getString("nicknames"),
                                              this.root.getNode("MySQL", "User").getString("Derp"),
                                              this.root.getNode("MySQL", "Password").getString("MyUberPassWord"));
            }
            catch (SQLException | IOException e)
            {
                System.out.println("Could not load MySQL storage for " + Reference.MetaData.PLUGIN_ID + "! Check if you entered the right information in the config file.");
                throw e;
            }
        }

        return new GsonPlayerStorage(new File(this.file.getParentFile(), "players"));
    }

    @Override
    public Map<String, Long> getCooldowns()
    {
        Map<String, Long> cooldowns = new HashMap<>();

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : this.root.getNode("Cooldown").getChildrenMap().entrySet())
        {
            try{
                cooldowns.put(entry.getKey().toString(), DateUtil
                        .parseDateDiff(entry.getValue().getString("1mo"), true) - System.currentTimeMillis());
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }

        return cooldowns;
    }

    @Override
    public List<String> getBlacklist()
    {
        try
        {
            return this.root.getNode("Blacklist").getList(TypeToken.of(String.class));
        }
        catch (ObjectMappingException e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean setTabListName() {
        return this.root.getNode("Set tab list name").getBoolean(true);
    }
}
