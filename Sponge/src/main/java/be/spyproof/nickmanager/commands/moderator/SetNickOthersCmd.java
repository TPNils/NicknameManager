package be.spyproof.nickmanager.commands.moderator;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.argument.PlayerDataArg;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.PlayerData;
import be.spyproof.nickmanager.util.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.*;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class SetNickOthersCmd extends AbstractCmd
{
    private static final String[] ARGS = new String[]{"player", "nickname"};

    private SetNickOthersCmd(MessageController messageController, ISpongePlayerController playerController)
    {
        super(messageController, playerController);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        Optional<PlayerData> player = args.getOne(ARGS[0]);
        if (!player.isPresent())
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.MISSING_ARGUMENT).apply(TemplateUtils.getParameters("argument", ARGS[0])).build());
            return CommandResult.success();
        }

        if (src instanceof Player && player.get().getUuid().equals(((Player) src).getUniqueId()))
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.CANT_FORCE_CHANGE_OWN_NICK).apply().build());
            return CommandResult.success();
        }

        Optional<String> nick = args.getOne(ARGS[1]);
        if (!nick.isPresent())
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.MISSING_ARGUMENT).apply(TemplateUtils.getParameters("argument", ARGS[1])).build());
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
        if (colours.size() > SpongeUtils.INSTANCE.getConfigController().maxColours())
        {
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.TO_MANY_COLOURS).apply(TemplateUtils.getParameters("amount", SpongeUtils.INSTANCE.getConfigController().maxColours())).build());
            return CommandResult.success();
        }

        if (styles.size() > SpongeUtils.INSTANCE.getConfigController().maxStyles())
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
            placeholders.putAll(TemplateUtils.getParameters("length-with-colour", SpongeUtils.INSTANCE.getConfigController().maxNickLengthWithColour()));
            placeholders.putAll(TemplateUtils.getParameters("length-without-colour", SpongeUtils.INSTANCE.getConfigController().maxNickLengthWithoutColour()));
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.NICKNAME_TO_LONG).apply(placeholders).build());
            return CommandResult.success();
        }

        // Apply
        player.get().setNickname(nick.get());
        player.get().setLastChanged();
        this.playerController.savePlayer(player.get());
        Map<String, Text> placeholders = TemplateUtils.getParameters(player.get());
        src.sendMessage(this.messageController.getMessage(Reference.SuccessMessages.ADMIN_NICK_SET).apply(placeholders).build());
        Optional<Player> receiver = Sponge.getServer().getPlayer(player.get().getUuid());
        if (receiver.isPresent())
            receiver.get().sendMessage(this.messageController.getMessage(Reference.SuccessMessages.NICK_SET).apply(placeholders).build());

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .arguments(new PlayerDataArg(ARGS[0], playerController), GenericArguments.string(Text.of(ARGS[1])))
                          .executor(new SetNickOthersCmd(messageController, playerController))
                          .permission(Reference.Permissions.ADMIN_SET)
                          .build();
    }
}
