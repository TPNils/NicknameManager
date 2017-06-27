package be.spyproof.nickmanager;

import be.spyproof.nickmanager.commands.ParentCmd;
import be.spyproof.nickmanager.commands.moderator.*;
import be.spyproof.nickmanager.commands.player.*;
import be.spyproof.nickmanager.controller.BukkitNicknameController;
import be.spyproof.nickmanager.controller.ConfigController;
import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.da.config.IConfigStorage;
import be.spyproof.nickmanager.listeners.PlayerListener;
import be.spyproof.nickmanager.util.BukkitUtils;
import be.spyproof.nickmanager.util.Reference;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Spyproof on 09/11/2016.
 */
public class Main extends JavaPlugin
{
    private IConfigStorage configStorage;
    private IBukkitNicknameController playerController;
    private MessageController messageController;

    @Override
    public void onLoad()
    {
        saveDefaultConfig();
    }

    @Override
    public void onEnable()
    {
        this.configStorage = new ConfigController(new File(this.getDataFolder(), "config.yml"));
        try
        {
            this.configStorage.load();
            this.playerController = new BukkitNicknameController(this.configStorage.getPlayerStorage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.messageController = new MessageController(new File(this.getDataFolder(), "lang.yml"), this.configStorage.getLanguage());
        try
        {
            this.messageController.load();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        BukkitUtils.initInstance(this.playerController, this.configStorage);
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable()
    {
        try
        {
            this.playerController.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Register all commands
     */
    private void registerCommands()
    {
        ParentCmd playerCmd = new ParentCmd("nick", "nickname");
        ParentCmd adminCmd = new ParentCmd("admnick", "adminnick", "adminnickname");
        ParentCmd resetCmd = new ParentCmd("reset");

        playerCmd.addChild(new CheckSelfCmd(this.messageController, this.playerController, Reference.CommandKeys.PLAYER_CHECK));
        playerCmd.addChild(new DisplayFormatsCmd(this.messageController, this.playerController, Reference.CommandKeys.PLAYER_FORMAT));
        playerCmd.addChild(new RealNameCmd(this.messageController, this.playerController, Reference.CommandKeys.PLAYER_REAL_NAME));
        playerCmd.addChild(new ResetOwnNickCmd(this.messageController, this.playerController, Reference.CommandKeys.PLAYER_RESET));
        playerCmd.addChild(new RulesCmd(this.messageController, this.playerController, Reference.CommandKeys.PLAYER_RULES));
        playerCmd.addChild(new SetNickCmd(this.messageController, this.playerController, Reference.CommandKeys.PLAYER_SET));
        playerCmd.addChild(new TestNickCmd(this.messageController, this.playerController, Reference.CommandKeys.PLAYER_PREVIEW));
        playerCmd.addChild(new UnlockCmd(this.messageController, this.playerController, Reference.CommandKeys.PLAYER_UNLOCK));

        resetCmd.addChild(new ResetOtherCooldownCmd(this.messageController, this.playerController, Reference.CommandKeys.ADMIN_RESET_COOLDOWN));
        resetCmd.addChild(new ResetOtherNickCmd(this.messageController, this.playerController, Reference.CommandKeys.ADMIN_RESET_NICKNAME));
        resetCmd.addChild(new ResetOtherRulesCmd(this.messageController, this.playerController, Reference.CommandKeys.ADMIN_RESET_RULES));
        resetCmd.addChild(new ResetOtherTokensCmd(this.messageController, this.playerController, Reference.CommandKeys.ADMIN_RESET_TOKENS));

        adminCmd.addChild(resetCmd);
        adminCmd.addChild(new CheckOtherCmd(this.messageController, this.playerController, Reference.CommandKeys.ADMIN_CHECK));
        adminCmd.addChild(new GiveNickChangesCmd(this.messageController, this.playerController, Reference.CommandKeys.ADMIN_GIVE));
        adminCmd.addChild(new SetNickOthersCmd(this.messageController, this.playerController, Reference.CommandKeys.ADMIN_SET));

        playerCmd.register(this);
        adminCmd.register(this);

        for (String s : Reference.CommandKeys.ACCEPT_RULES)
        {
            AcceptRulesCmd acceptRulesCmd = new AcceptRulesCmd(this.messageController, this.playerController);
            PluginCommand cmd = this.getCommand(s);
            if (cmd != null)
                cmd.setExecutor(acceptRulesCmd);
        }
    }

    /**
     * Register all events
     */
    private void registerListeners()
    {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this.playerController), this);
    }
}
