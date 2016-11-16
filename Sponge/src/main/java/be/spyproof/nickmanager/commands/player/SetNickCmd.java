package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.argument.NicknameArg;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class SetNickCmd extends AbstractPlayerCmd
{
    private static final String ARG = "nickname";

    private SetNickCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(Player src, CommandContext args) throws CommandException
    {
        Optional<String> nick = args.getOne(ARG);
        if (!nick.isPresent())
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.MISSING_ARGUMENT).apply(TemplateUtils.getParameters("argument", ARG)).build());
            return CommandResult.success();
        }

        PlayerData playerData = this.playerController.wrapPlayer(src);

        // Has tokens
        if (!SpongeUtils.INSTANCE.canChangeNickname(playerData, src))
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.MISSING_TOKENS).toText());
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.UNLOCK_TOKENS).apply(TemplateUtils.getParameters("command", "/nick " + Reference.CommandKeys.PLAYER_UNLOCK[0])).toText());
            return CommandResult.success();
        }

        // cooldowns
        long cooldown;
        if (src.hasPermission(Reference.Permissions.BYPASS_COOLDOWN))
        {
            cooldown = 0;
        }else
        {
            cooldown = SpongeUtils.INSTANCE.getDefaultCooldown();
            for (Map.Entry<String, Long> entry : SpongeUtils.INSTANCE.getExtraCooldowns().entrySet())
            {
                if (src.hasPermission(Reference.Permissions.COOLDOWN_PREFIX + entry.getKey()) && entry
                        .getValue() < cooldown)
                    cooldown = entry.getValue();
            }
        }

        long timeDiff = playerData.getLastChanged() + cooldown - System.currentTimeMillis();
        if (timeDiff > 0)
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.ON_COOLDOWN).apply(
                    TemplateUtils.getParameters("time", DateUtil.timeformat(timeDiff))).build());
            return CommandResult.success();
        }

        // blacklist
        {
            Optional<String> blacklistRegex = SpongeUtils.INSTANCE.isBlacklisted(src, nick.get());
            if (blacklistRegex.isPresent())
            {
                src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.BLACKLIST).apply(TemplateUtils.getParameters("regex", blacklistRegex.get())).build());
                return CommandResult.success();
            }
        }

        List<Colour> colours = StringUtils.getPresentColours(nick.get());
        List<Style> styles = StringUtils.getPresentStyles(nick.get());

        // colour amount
        if (colours.size() > SpongeUtils.INSTANCE.getConfigController().maxColours() && !src.hasPermission(Reference.Permissions.BYPASS_COLOUR_LIMIT))
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.TO_MANY_COLOURS).apply(TemplateUtils.getParameters("amount", SpongeUtils.INSTANCE.getConfigController().maxColours())).build());
            return CommandResult.success();
        }

        if (styles.size() > SpongeUtils.INSTANCE.getConfigController().maxStyles() && !src.hasPermission(Reference.Permissions.BYPASS_STYLE_LIMIT))
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.TO_MANY_STYLES).apply(TemplateUtils.getParameters("amount", SpongeUtils.INSTANCE.getConfigController().maxColours())).build());
            return CommandResult.success();
        }

        // colour permissions
        {   // Verify the player is allowed to use the colours
            Iterator<Colour> iterator = colours.iterator();
            while (iterator.hasNext())
                if (src.hasPermission(Reference.Permissions.COLOURS_PREFIX + iterator.next().name().toLowerCase()))
                    iterator.remove();
        }

        {   // Verify the player is allowed to use the styles
            Iterator<Style> iterator = styles.iterator();
            while (iterator.hasNext())
                if (src.hasPermission(Reference.Permissions.STYLE_PREFIX + iterator.next().name().toLowerCase()))
                    iterator.remove();
        }

        if (colours.size() + styles.size() > 0)
        {
            String illegalStyles = "";
            for (Colour colour : colours)
                illegalStyles += " &" + colour.getColourChar();
            for (Style style : styles)
                illegalStyles += " &" + style.getColourChar();
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.ILLEGAL_FORMAT).apply(TemplateUtils.getParameters("style", illegalStyles)).build());
            return CommandResult.success();
        }

        // nick length
        if (!SpongeUtils.INSTANCE.lengthCheck(nick.get()))
        {
            Map<String, Integer> placeholders = new HashMap<>();
            placeholders.putAll(TemplateUtils.getParameters("length-with-colour", SpongeUtils.INSTANCE.getConfigController().maxNickLengthWithColor()));
            placeholders.putAll(TemplateUtils.getParameters("length-without-colour", SpongeUtils.INSTANCE.getConfigController().maxNickLengthWithoutColor()));
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.NICKNAME_TO_LONG).apply(placeholders).build());
            return CommandResult.success();
        }

        // Apply
        playerData.setNickname(nick.get());
        playerData.setLastChanged();
        src.sendMessage(this.messageController.getMessage(Reference.SuccessMessages.NICK_SET).apply(TemplateUtils.getParameters(playerData)).build());
        if (!src.hasPermission(Reference.Permissions.BYPASS_CHANGE_LIMIT))
            playerData.setTokensRemaining(playerData.getTokensRemaining()-1);
        this.playerController.savePlayer(playerData);

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .arguments(new NicknameArg(ARG, playerController))
                          .executor(new SetNickCmd(messageController, playerController))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
