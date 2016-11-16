package be.spyproof.nickmanager.util;

/**
 * Created by Spyproof on 28/10/2016.
 *
 * This contains all references that may be used in other modules.
 * Having this centralised allows layouts to stay the same in the other modules
 * while also preventing accidental typos
 */
public class Reference
{
    public static final String COLOUR_AND_STYLE_PATTERN = "[§&][0-9a-fA-Fk-oK-OrR]";

    /**
     * Contains all permissions
     */
    public final static class Permissions
    {
        public static final String BYPASS_RULES = "nickmanager.bypass.acceptrules";
        public static final String BYPASS_CHANGE_LIMIT = "nickmanager.bypass.nochangelimit";
        public static final String BYPASS_BLACKLIST = "nickmanager.bypass.blacklist";
        public static final String BYPASS_COOLDOWN = "nickmanager.bypass.cooldown";

        public static final String COOLDOWN_PREFIX = "nickmanager.cooldown.";

        public static final String COLOURS_PREFIX = "nickmanager.colours.";
        public static final String STYLE_PREFIX = "nickmanager.style.";
        public static final String BYPASS_COLOUR_LIMIT = "nickmanager.bypass.colourlimit";
        public static final String BYPASS_STYLE_LIMIT = "nickmanager.bypass.stylelimit";

        public static final String ADMIN_SET = "nickmanager.admin.set";
        public static final String ADMIN_GIVE = "nickmanager.admin.give";
        public static final String ADMIN_CHECK = "nickmanager.admin.check";
        public static final String ADMIN_RESET = "nickmanager.admin.reset";

        public static final String GENERIC_PLAYER_COMMANDS = "nickmanager.player.generic";
    }

    /**
     * Contains the meta data
     */
    public static final class MetaData
    {
        public static final String PLUGIN_ID = "nickname_manager";
        public static final String PLUGIN_NAME = "Nickname Manager";
        public static final String VERSION = "1.0.0";
        public static final String DESCRIPTIONS = "Allows players to manage their own nicknames";
        public static final String[] AUTHORS = new String[]{"TPNils"};
    }

    public static final class CommandKeys
    {
        public static final String[] ACCEPT_RULES = new String[]{"IAcceptTheNicknameRules"};
        public static final String[] PLAYER_RULES = new String[]{"rules"};
        public static final String[] PLAYER_CHECK = new String[]{"check", "info"};
        public static final String[] PLAYER_FORMAT = new String[]{"format", "color", "colour", "colors", "colours", "style", "styles"};
        public static final String[] PLAYER_REAL_NAME = new String[]{"realname"};
        public static final String[] PLAYER_RESET = new String[]{"reset"};
        public static final String[] PLAYER_SET = new String[]{"set", "change"};
        public static final String[] PLAYER_PREVIEW = new String[]{"preview", "test"};
        public static final String[] PLAYER_UNLOCK = new String[]{"unlock"};

        public static final String[] ADMIN_CHECK = new String[]{"check", "info", "see"};
        public static final String[] ADMIN_GIVE = new String[]{"give"};
        public static final String[] ADMIN_RESET_COOLDOWN = new String[]{"cooldown"};
        public static final String[] ADMIN_RESET_NICKNAME = new String[]{"nick", "nickname"};
        public static final String[] ADMIN_RESET_RULES = new String[]{"rules", "accepted-rules"};
        public static final String[] ADMIN_RESET_TOKENS = new String[]{"tokens", "token"};
        public static final String[] ADMIN_SET = new String[]{"set", "change"};
    }

    /**
     * Contains all message sections regarding errors
     */
    public static final class ErrorMessages
    {
        public static final String[] BLACKLIST = new String[]{"command", "failure", "blacklist"};
        public static final String[] CANT_FORCE_CHANGE_OWN_NICK = new String[]{"command", "failure", "force change own nick"};
        public static final String[] ILLEGAL_FORMAT = new String[]{"command", "failure", "illegal format"};
        public static final String[] MISSING_TOKENS = new String[]{"command", "failure", "missing tokens"};
        public static final String[] MISSING_ARGUMENT = new String[]{"command", "failure", "missing argument"};
        public static final String[] WRONG_ARGUMENT = new String[]{"command", "failure", "wrong argument"};
        public static final String[] MUST_ACCEPT_RULES = new String[]{"command", "failure", "must accept rules"};
        public static final String[] MUST_READ_RULES = new String[]{"command", "failure", "must read rules"};
        public static final String[] NICKNAME_TO_LONG = new String[]{"command", "failure", "nickname to long"};
        public static final String[] ON_COOLDOWN = new String[]{"command", "failure", "on cooldown"};
        public static final String[] NO_PERMISSION = new String[]{"command", "failure", "no permission"};
        public static final String[] PLAYER_ONLY = new String[]{"command", "failure", "player only"};
        public static final String[] TO_MANY_COLOURS = new String[]{"command", "failure", "to many colours"};
        public static final String[] TO_MANY_STYLES = new String[]{"command", "failure", "to many styles"};
        public static final String[] UNLOCK_TOKENS = new String[]{"command", "failure", "unlock tokens"};
    }

    /**
     * Contains all message sections regarding successes
     */
    public static final class SuccessMessages
    {
        public static final String[] ACCEPTED_RULES = new String[]{"command", "success", "accept rules"};

        public static final String[] ADMIN_NICK_GIVE = new String[]{"command", "success", "admnick", "give"};
        public static final String[] ADMIN_NICK_GIVE_RECEIVED = new String[]{"command", "success", "admnick", "give received"};
        public static final String[] ADMIN_NICK_SET = new String[]{"command", "success", "admnick", "set"};
        public static final String[] ADMIN_NICK_CHECK = new String[]{"command", "success", "admnick", "check"};
        public static final String[] ADMIN_NICK_RESET_NICKNAME = new String[]{"command", "success", "admnick", "reset", "nickname"};
        public static final String[] ADMIN_NICK_RESET_TOKENS = new String[]{"command", "success", "admnick", "reset", "tokens"};
        public static final String[] ADMIN_NICK_RESET_COOLDOWN = new String[]{"command", "success", "admnick", "reset", "cooldown"};
        public static final String[] ADMIN_NICK_RESET_RULES = new String[]{"command", "success", "admnick", "reset", "rules"};
        public static final String[] ADMIN_NICK_RESET_TOKENS_RECEIVED = new String[]{"command", "success", "admnick", "reset", "tokens received"};
        public static final String[] ADMIN_NICK_RESET_COOLDOWN_RECEIVED = new String[]{"command", "success", "admnick", "reset", "cooldown received"};
        public static final String[] ADMIN_NICK_RESET_RULES_RECEIVED = new String[]{"command", "success", "admnick", "reset", "rules received"};

        public static final String[] NICK_SET = new String[]{"command", "success", "nick", "set"};
        public static final String[] NICK_CHECK = new String[]{"command", "success", "nick", "check"};
        public static final String[] NICK_REAL_NAME = new String[]{"command", "success", "nick", "real name"};
        public static final String[] NICK_RESET = new String[]{"command", "success", "nick", "reset"};
        public static final String[] NICK_RULES = new String[]{"command", "success", "nick", "rules"};
        public static final String[] NICK_PREVIEW = new String[]{"command", "success", "nick", "preview"};
        public static final String[] NICK_UNLOCK = new String[]{"command", "success", "nick", "unlock"};
    }

    /**
     * Contains all message sections regarding help
     */
    public static final class HelpMessages
    {
        public static final String[] ACCEPT_NICKNAME = new String[]{"help", "accept rules"};

        public static final String[] NICK_SET = new String[]{"help", "player", "set"};
        public static final String[] NICK_PREVIEW = new String[]{"help", "player", "preview"};
        public static final String[] NICK_CHECK = new String[]{"help", "player", "check"};
        public static final String[] NICK_FORMAT = new String[]{"help", "player", "format"};
        public static final String[] NICK_REAL_NAME = new String[]{"help", "player", "real name"};
        public static final String[] NICK_RESET = new String[]{"help", "player", "reset"};
        public static final String[] NICK_RULES = new String[]{"help", "player", "rules"};
        public static final String[] NICK_UNLOCK = new String[]{"help", "player", "unlock"};

        public static final String[] ADMIN_NICK_CHECK = new String[]{"help", "admin", "check"};
        public static final String[] ADMIN_NICK_GIVE = new String[]{"help", "admin", "give"};
        public static final String[] ADMIN_NICK_SET = new String[]{"help", "admin", "set"};
        public static final String[] ADMIN_NICK_RESET_NICKNAME = new String[]{"help", "admin", "reset", "nickname"};
        public static final String[] ADMIN_NICK_RESET_TOKENS = new String[]{"help", "admin", "reset", "tokens"};
        public static final String[] ADMIN_NICK_RESET_RULES = new String[]{"help", "admin", "reset", "rules"};
        public static final String[] ADMIN_NICK_RESET_COOLDOWN = new String[]{"help", "admin", "reset", "cooldown"};
    }
}
