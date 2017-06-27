package be.spyproof.nickmanager.controller;

import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.Reference.ErrorMessages;
import be.spyproof.nickmanager.util.Reference.HelpMessages;
import be.spyproof.nickmanager.util.Reference.SuccessMessages;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class MessageController
{
    private File dir;

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode root;

    public MessageController(File dir)
    {
        this.dir = dir;
        if (!this.dir.exists())
        {
            this.dir.mkdirs();
        }else if (this.dir.isFile())
        {
            this.dir.delete();
            this.dir.mkdirs();
        }
    }

    public void load(String language) throws IOException, ObjectMappingException
    {
        File langFile = new File(this.dir, language + ".conf");
        if (!langFile.exists())
            langFile.createNewFile();

        this.loader = HoconConfigurationLoader.builder().setFile(langFile).build();
        this.root = this.loader.load();

        if (language.equalsIgnoreCase("english"))
            saveEnglishDefaults();
    }

    public void saveEnglishDefaults() throws IOException, ObjectMappingException
    {
        CommentedConfigurationNode n = this.root;

        // All failure messages
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "Your nickname is now allowed because it matches: "),
                                           getArg("regex").color(TextColors.RED).style(TextStyles.ITALIC).build()),
                        ErrorMessages.BLACKLIST);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "You can't force change your own nickname")),
                        ErrorMessages.CANT_FORCE_CHANGE_OWN_NICK);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "You are not allows to use the following styles: "),
                                           getArg("style").color(TextColors.RED).style(TextStyles.ITALIC).build()),
                        ErrorMessages.ILLEGAL_FORMAT);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "You do not own any nickname tokens")),
                        ErrorMessages.MISSING_TOKENS);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "Missing argument: "),
                                           getArg("argument").color(TextColors.RED).style(TextStyles.ITALIC).build()),
                        ErrorMessages.MISSING_ARGUMENT);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "You must accept the rules first with "),
                                           getArg("command").color(TextColors.DARK_AQUA).build()),
                        ErrorMessages.MUST_ACCEPT_RULES);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "You must read the rules first with "),
                                           getArg("command").color(TextColors.DARK_AQUA).build()),
                        ErrorMessages.MUST_READ_RULES);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "Your nickname can only be "),
                                           getArg("length-with-colour").color(TextColors.RED).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.RED, " characters long with colour codes and "),
                                           getArg("length-without-colour").color(TextColors.RED).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.RED, " characters long without colour codes")),
                        ErrorMessages.NICKNAME_TO_LONG);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "You need to wait "),
                                           getArg("time").color(TextColors.RED).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.RED, " before you can change your nickname")),
                        ErrorMessages.ON_COOLDOWN);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "You dont have permission for this")),
                        ErrorMessages.NO_PERMISSION);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "This command can only be used by players")),
                        ErrorMessages.PLAYER_ONLY);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "Your nickname can only have "),
                                           getArg("amount").color(TextColors.RED).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.RED, " colour(s)")),
                        ErrorMessages.TO_MANY_COLOURS);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "Your nickname can only have "),
                                           getArg("amount").color(TextColors.RED).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.RED, " style(s)")),
                        ErrorMessages.TO_MANY_STYLES);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Use "),
                                           getArg("command").color(TextColors.GREEN).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.GREEN, " for more information")),
                        ErrorMessages.UNLOCK_TOKENS);

        // All successful /IAcceptTheNicknameRules messages
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "You have accepted the rules")),
                        SuccessMessages.ACCEPTED_RULES);

        // All successful /admnick messages
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "You have given "),
                                           getArg("tokens").color(TextColors.GREEN).build(),
                                           Text.of(TextColors.GREEN, " nickname tokens to "),
                                           getArg("player.name").color(TextColors.WHITE).style(TextStyles.ITALIC).build()),
                        SuccessMessages.ADMIN_NICK_GIVE);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "You have received "),
                                           getArg("tokens").color(TextColors.GREEN).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.GREEN, " nickname tokens. Use /nick for more information")),
                        SuccessMessages.ADMIN_NICK_GIVE_RECEIVED);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "You have changed the nickname of "),
                                           getArg("player.name").color(TextColors.GREEN).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.GREEN, " to "),
                                           getArg("player.nickname").build()),
                        SuccessMessages.ADMIN_NICK_SET);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Reset the nickname of "),
                                           getArg("player.name").color(TextColors.GREEN).style(TextStyles.ITALIC).build()),
                        SuccessMessages.ADMIN_NICK_RESET_NICKNAME);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "Your nickname tokens has been removed")),
                        SuccessMessages.ADMIN_NICK_RESET_TOKENS_RECEIVED);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Removed all nickname tokens of "),
                                           getArg("player.name").color(TextColors.GREEN).style(TextStyles.ITALIC).build()),
                        SuccessMessages.ADMIN_NICK_RESET_TOKENS);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Reset the nickname cooldown of "),
                                           getArg("player.name").color(TextColors.GREEN).style(TextStyles.ITALIC).build()),
                        SuccessMessages.ADMIN_NICK_RESET_COOLDOWN);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Your nickname cooldown has been reset")),
                        SuccessMessages.ADMIN_NICK_RESET_COOLDOWN_RECEIVED);
        setDefaultValue(n, TextTemplate.of(getArg("player.name").color(TextColors.GREEN).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.GREEN, " needs to accept the rules again")),
                        SuccessMessages.ADMIN_NICK_RESET_RULES);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "You are now forced to accept the nickname rules again")),
                        SuccessMessages.ADMIN_NICK_RESET_RULES_RECEIVED);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Current nickname: "),
                                           getArg("player.nickname").build(),
                                           Text.NEW_LINE,
                                           Text.of(TextColors.GREEN, "UUID: "),
                                           getArg("player.uuid").color(TextColors.DARK_AQUA).build(),
                                           Text.NEW_LINE,
                                           Text.of(TextColors.GREEN, "Tokens remaining: "),
                                           getArg("player.tokens").color(TextColors.DARK_AQUA).build(),
                                           Text.NEW_LINE,
                                           Text.of(TextColors.GREEN, "Past nicknames: "),
                                           Text.NEW_LINE,
                                           getArg("player.pastNicknames").build()
                                           ),
                        SuccessMessages.ADMIN_NICK_CHECK);

        // All successful /nick messages
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Your nickname has been changed to "),
                                           getArg("player.nickname").build()),
                        SuccessMessages.NICK_SET);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Your current nickname: "),
                                           getArg("player.nickname").build(),
                                           Text.NEW_LINE,
                                           Text.of(TextColors.GREEN, "Nickname tokens remaining: "),
                                           getArg("player.tokens").color(TextColors.DARK_AQUA).build()),
                        SuccessMessages.NICK_CHECK);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Found the following players matching the nickname "),
                                           getArg("nickname").color(TextColors.WHITE).build(),
                                           Text.of(TextColors.GREEN, ":", Text.NEW_LINE),
                                           getArg("players").color(TextColors.WHITE).style(TextStyles.ITALIC).build()),
                        SuccessMessages.NICK_REAL_NAME);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.RED, "Your nickname has been reset")),
                        SuccessMessages.NICK_RESET);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GOLD, " ==========> ", TextColors.GREEN, TextStyles.BOLD, "Rules", TextColors.GOLD, " <=========="), Text.NEW_LINE,
                                           Text.of(TextColors.GOLD, TextStyles.BOLD, "1. ", TextColors.YELLOW, "Don't use any offensive/racist/sexist names"), Text.NEW_LINE,
                                           Text.of(TextColors.GOLD, TextStyles.BOLD, "2. ", TextColors.YELLOW, "Don't impersonate players or staff"), Text.NEW_LINE,
                                           Text.of(TextColors.GOLD, TextStyles.BOLD, "3. ", TextColors.YELLOW, "Use common sense"), Text.NEW_LINE,
                                           Text.of(TextColors.GOLD, TextStyles.BOLD, "4. ", TextColors.YELLOW, "Staff will have the final judgment"), Text.NEW_LINE,
                                           Text.of(TextColors.RED, "If you break these rules, actions will be taken!"), Text.NEW_LINE,
                                           Text.of(TextColors.RED, "Use "),
                                           getArg("command").color(TextColors.RED).style(TextStyles.ITALIC).build(),
                                           Text.of(TextColors.RED, " to accept these rules")),
                        SuccessMessages.NICK_RULES);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GREEN, "Your nickname: "),
                                           getArg("nickname").color(TextColors.WHITE).build()),
                        SuccessMessages.NICK_PREVIEW);
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.GOLD, " ==========> ", TextColors.GREEN, TextStyles.BOLD, "Unlock", TextColors.GOLD, " <=========="), Text.NEW_LINE,
                                           Text.of(TextColors.YELLOW, "Nickname tokens allow you to change your nickname"), Text.NEW_LINE,
                                           Text.of(TextColors.YELLOW, "That nickname will be synced on all our servers"), Text.NEW_LINE,
                                           Text.of(TextColors.YELLOW, "You can buy nickname tokens @"), Text.NEW_LINE,
                                           getArg("website").defaultValue(Text.of("http://omfgdogs.com/").toBuilder().onClick(TextActions.openUrl(new URL("http://omfgdogs.com/"))).build()).optional().color(TextColors.GOLD).style(TextStyles.BOLD).build()),
                        SuccessMessages.NICK_UNLOCK);

        // All help messages
        Text info = Text.of(TextColors.WHITE, TextStyles.BOLD, " [", TextColors.RED, TextStyles.BOLD, "INFO", TextColors.WHITE, TextStyles.BOLD, "]");
        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/nick ", TextColors.GREEN, Reference.CommandKeys.PLAYER_SET[0] + " <nickname>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/nick " + Reference.CommandKeys.PLAYER_SET[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Change your current nickname at the cost of 1 nickname token", Text.NEW_LINE, "Preview your nickname before spending a token with", Text.NEW_LINE, "/nick " + Reference.CommandKeys.PLAYER_PREVIEW[0] + " <nickname>")))
                                               .build()),
                        HelpMessages.NICK_SET);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/nick ", TextColors.GREEN, Reference.CommandKeys.PLAYER_PREVIEW[0] + " <nickname>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/nick " + Reference.CommandKeys.PLAYER_PREVIEW[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Preview your nickname")))
                                               .build()),
                        HelpMessages.NICK_PREVIEW);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/nick ", TextColors.GREEN, Reference.CommandKeys.PLAYER_CHECK[0], info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/nick " + Reference.CommandKeys.PLAYER_CHECK[0]))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "See how many nickname tokens you have")))
                                               .build()),
                        HelpMessages.NICK_CHECK);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/nick ", TextColors.GREEN, Reference.CommandKeys.PLAYER_FORMAT[0], info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/nick " + Reference.CommandKeys.PLAYER_FORMAT[0]))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Check how to apply colours and styles to your nickname")))
                                               .build()),
                        HelpMessages.NICK_FORMAT);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/nick ", TextColors.GREEN, Reference.CommandKeys.PLAYER_REAL_NAME[0] + " <player>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/nick " + Reference.CommandKeys.PLAYER_REAL_NAME[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Try to find the real name of a player's nickname")))
                                               .build()),
                        HelpMessages.NICK_REAL_NAME);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/nick ", TextColors.GREEN, Reference.CommandKeys.PLAYER_RESET[0], info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/nick " + Reference.CommandKeys.PLAYER_RESET[0]))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Remove your current nickname, does not cost any nickname tokens")))
                                               .build()),
                        HelpMessages.NICK_RESET);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/nick ", TextColors.GREEN, Reference.CommandKeys.PLAYER_RULES[0], info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/nick " + Reference.CommandKeys.PLAYER_RULES[0]))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Read the rules regarding choosing a nickname")))
                                               .build()),
                        HelpMessages.NICK_RULES);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/nick ", TextColors.GREEN, Reference.CommandKeys.PLAYER_UNLOCK[0], info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/nick " + Reference.CommandKeys.PLAYER_UNLOCK[0]))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Information on how to obtain a nickname token")))
                                               .build()),
                        HelpMessages.NICK_UNLOCK);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/admnick ", TextColors.GREEN, Reference.CommandKeys.ADMIN_CHECK[0], " <player>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/admnick " + Reference.CommandKeys.ADMIN_CHECK[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "See information about the player")))
                                               .build()),
                        HelpMessages.ADMIN_NICK_CHECK);


        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/admnick ", TextColors.GREEN, Reference.CommandKeys.ADMIN_SET[0], " <player> <amount>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/admnick " + Reference.CommandKeys.ADMIN_SET[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Give the player a nickname token")))
                                               .build()),
                        HelpMessages.ADMIN_NICK_GIVE);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/admnick ", TextColors.GREEN, Reference.CommandKeys.ADMIN_SET[0], " <player> <nickname>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/admnick " + Reference.CommandKeys.ADMIN_SET[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Change the player's nickname")))
                                               .build()),
                        HelpMessages.ADMIN_NICK_SET);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/admnick ", TextColors.GREEN, "reset " + Reference.CommandKeys.ADMIN_RESET_NICKNAME[0], " <player>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/admnick reset " + Reference.CommandKeys.ADMIN_RESET_NICKNAME[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Reset the player's nickname")))
                                               .build()),
                        HelpMessages.ADMIN_NICK_RESET_NICKNAME);

        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/admnick ", TextColors.GREEN, "reset " + Reference.CommandKeys.ADMIN_RESET_TOKENS[0], " <player>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/admnick reset " + Reference.CommandKeys.ADMIN_RESET_TOKENS[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Reset the player's tokens")))
                                               .build()),
                        HelpMessages.ADMIN_NICK_RESET_TOKENS);


        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/admnick ", TextColors.GREEN, "reset " + Reference.CommandKeys.ADMIN_RESET_RULES[0], " <player>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/admnick reset " + Reference.CommandKeys.ADMIN_RESET_RULES[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Force the player to accept the rules again before they can use any /nick commands")))
                                               .build()),
                        HelpMessages.ADMIN_NICK_RESET_RULES);


        setDefaultValue(n, TextTemplate.of(Text.of(TextColors.AQUA, "/admnick ", TextColors.GREEN, "reset " + Reference.CommandKeys.ADMIN_RESET_COOLDOWN[0], " <player>", info)
                                               .toBuilder()
                                               .onClick(TextActions.suggestCommand("/admnick reset " + Reference.CommandKeys.ADMIN_RESET_COOLDOWN[0] + " "))
                                               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, "Reset the player's cooldown")))
                                               .build()),
                        HelpMessages.ADMIN_NICK_RESET_COOLDOWN);

        this.loader.save(this.root);
    }

    public TextTemplate getMessage(String... path)
    {
        try
        {
            return root.getNode(path).getValue(TypeToken.of(TextTemplate.class));
        }
        catch (ObjectMappingException e)
        {
            String p = "";
            for (int i = 0; i < path.length; i++)
            {
                p += path[i];
                if (path.length - 1 < path.length)
                    p += '.';
            }
            return TextTemplate.of(Text.of(TextColors.RED, "Missing message: " + p));
        }
    }

    private TextTemplate.Arg.Builder getArg(String text)
    {
        return TextTemplate.arg(Reference.MetaData.PLUGIN_ID + ":" + text);
    }

    private <T> void setDefaultValue(CommentedConfigurationNode root, TypeToken<T> token, T def, String... path) throws ObjectMappingException
    {
        CommentedConfigurationNode node = root.getNode(path);
        T storedValue = node.getValue(token);

        if (storedValue == null)
            node.setValue(token, def);
    }

    private <T> void setDefaultValue(CommentedConfigurationNode root, T def, String... path) throws ObjectMappingException
    {
        setDefaultValue(root, (TypeToken<T>) TypeToken.of(def.getClass()), def, path);
    }
}
