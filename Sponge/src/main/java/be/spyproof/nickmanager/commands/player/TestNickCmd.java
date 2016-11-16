package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.commands.argument.NicknameArg;
import be.spyproof.nickmanager.controller.ISpongePlayerController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.*;

/**
 * Created by Spyproof on 28/10/2016.
 */
public class TestNickCmd extends AbstractPlayerCmd
{
    private static final String ARG = "nickname";

    private TestNickCmd(MessageController messageController, ISpongePlayerController playerController)
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
            placeholders.putAll(TemplateUtils.getParameters("length-without-colour", SpongeUtils.INSTANCE.getConfigController().maxNickLengthWithoutColuor()));
            src.sendMessage(this.messageController.getMessage(Reference.ErrorMessages.NICKNAME_TO_LONG).apply(placeholders).build());
            return CommandResult.success();
        }

        src.sendMessage(this.messageController.getMessage(Reference.SuccessMessages.NICK_PREVIEW).apply(TemplateUtils.getParameters("nickname", TextSerializers.FORMATTING_CODE.deserialize(nick.get()))).build());

        return CommandResult.success();
    }

    public static CommandSpec getCommandSpec(MessageController messageController, ISpongePlayerController playerController)
    {
        return CommandSpec.builder()
                          .arguments(new NicknameArg(ARG, playerController))
                          .executor(new TestNickCmd(messageController, playerController))
                          .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                          .build();
    }
}
