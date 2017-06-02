package be.spyproof.nickmanager;

import be.spyproof.nickmanager.commands.moderator.*;
import be.spyproof.nickmanager.commands.player.*;
import be.spyproof.nickmanager.controller.ConfigController;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.controller.SpongePlayerController;
import be.spyproof.nickmanager.da.config.IConfigStorage;
import be.spyproof.nickmanager.listener.PlayerListener;
import be.spyproof.nickmanager.listener.UltimateChatListener;
import be.spyproof.nickmanager.listener.VanillaNicknameApplier;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.SpongeUtils;
import com.google.inject.Inject;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Spyproof on 28/10/2016.
 */
@Plugin(
        id = Reference.MetaData.PLUGIN_ID,
        name = Reference.MetaData.PLUGIN_NAME,
        version = Reference.MetaData.VERSION,
        description = Reference.MetaData.DESCRIPTIONS,
        authors = {"TPNils"},
        dependencies = @Dependency(id="ultimatechat", optional=true))
public class Main
{
    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    private ISpongePlayerController playerController;
    private IConfigStorage configController;
    private MessageController messageController;

    /**
     * During this event, all configs are are being read and initialised all local variables
     * @param event the fired event
     */
    @Listener
    public void onPreInit(GamePreInitializationEvent event)
    {
        if (!this.configDir.exists())
            this.configDir.mkdirs();

        try
        {
            this.configController = new ConfigController(new File(configDir, "config.conf"));
            this.configController.load();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            this.messageController = new MessageController(new File(configDir, "lang"));
            this.messageController.load(this.configController.getLanguage());
        }
        catch (IOException | ObjectMappingException e)
        {
            e.printStackTrace();
        }

        try {
            this.playerController = new SpongePlayerController(this.configController.getPlayerStorage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        SpongeUtils.initInstance(playerController, configController);
    }

    /**
     * During this event, all event listeners are registered
     * @param event the fired event
     */
    @Listener
    public void onInit(GameInitializationEvent event)
    {
        registerListeners();
    }

    /**
     * During this event, all commands are registered
     * @param event the fired event
     */
    @Listener
    public void onServerStart(GameStartingServerEvent event)
    {
        registerCommands();
    }

    /**
     * During this event, everything that needs to be closed will do so.
     * @param event the fired event
     */
    @Listener
    public void onServerStop(GameStoppingEvent event)
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
     * Register all events
     */
    private void registerListeners()
    {
        Sponge.getGame().getEventManager().registerListeners(this, new VanillaNicknameApplier(this.playerController));
        Sponge.getGame().getEventManager().registerListeners(this, new PlayerListener(this.playerController));

        try
        {
            Class.forName("br.net.fabiozumbi12.UltimateChat.API.SendChannelMessageEvent");
            Sponge.getEventManager().registerListener(this, br.net.fabiozumbi12.UltimateChat.API.SendChannelMessageEvent.class, new UltimateChatListener(this.playerController));
        }
        catch (ClassNotFoundException ignored)
        {}
    }

    /**
     * Register all commands
     */
    private void registerCommands()
    {
        Sponge.getCommandManager().register(this, AcceptRulesCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.ACCEPT_RULES);

        CommandSpec playerHelp = PlayerHelpCmd.getCommandSpec(this.messageController);
        CommandSpec nickCmd =
                CommandSpec.builder()
                           .executor(playerHelp.getExecutor())
                           .child(SetNickCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.PLAYER_SET)
                           .child(TestNickCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.PLAYER_PREVIEW)
                           .child(CheckSelfCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.PLAYER_CHECK)
                           .child(ResetOwnNickCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.PLAYER_RESET)
                           .child(RulesCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.PLAYER_RULES)
                           .child(RealNameCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.PLAYER_REAL_NAME)
                           .child(DisplayFormatsCmd.getCommandSpec(), Reference.CommandKeys.PLAYER_FORMAT)
                           .child(UnlockCmd.getCommandSpec(this.messageController), Reference.CommandKeys.PLAYER_UNLOCK)
                           .child(playerHelp, "help", "hep")
                           .build();
        Sponge.getCommandManager().register(this, nickCmd, "nick", "nickname");

        CommandSpec adminHelp = AdminHelpCmd.getCommandSpec(this.messageController);
        CommandSpec resetCmd = CommandSpec.builder()
                                          .executor(adminHelp.getExecutor())
                                          .child(adminHelp, "help", "hep")
                                          .child(ResetOtherCooldownCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.ADMIN_RESET_COOLDOWN)
                                          .child(ResetOtherNickCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.ADMIN_RESET_NICKNAME)
                                          .child(ResetOtherRulesCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.ADMIN_RESET_RULES)
                                          .child(ResetOtherTokensCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.ADMIN_RESET_TOKENS)
                                          .build();

        CommandSpec adminCmd =
                CommandSpec.builder()
                           .executor(adminHelp.getExecutor())
                           .child(adminHelp, "help", "hep")
                           .child(SetNickOthersCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.ADMIN_SET)
                           .child(CheckOtherCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.ADMIN_CHECK)
                           .child(GiveNickChangesCmd.getCommandSpec(this.messageController, this.playerController), Reference.CommandKeys.ADMIN_GIVE)
                           .child(resetCmd, "reset")
                           .build();
        Sponge.getCommandManager().register(this, adminCmd, "admnick", "adminnick", "adminnickname");
    }

}
