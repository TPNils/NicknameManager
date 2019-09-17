package be.spyproof.nickmanager.commands.checks;

import be.spyproof.nickmanager.commands.IMessageControllerHolder;
import be.spyproof.nickmanager.util.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Spyproof on 17/11/2016.
 */
public interface IFormatChecker extends IMessageControllerHolder {

  default void checkFormat(CommandSource src, String nickname) throws CommandException {
    List<Colour> colours = StringUtils.getPresentColours(nickname);
    List<Style> styles = StringUtils.getPresentStyles(nickname);

    // colour amount
    if (colours.size() > SpongeUtils.INSTANCE.getConfigController().maxColours() && !src.hasPermission(Reference.Permissions.BYPASS_COLOUR_LIMIT)) {
      throw new CommandException(this.getMessageController()
                                     .getMessage(Reference.ErrorMessages.TO_MANY_COLOURS)
                                     .apply(TemplateUtils.getParameters("amount", SpongeUtils.INSTANCE.getConfigController().maxColours()))
                                     .build());
    }

    if (styles.size() > SpongeUtils.INSTANCE.getConfigController().maxStyles() && !src.hasPermission(Reference.Permissions.BYPASS_STYLE_LIMIT)) {
      throw new CommandException(this.getMessageController()
                                     .getMessage(Reference.ErrorMessages.TO_MANY_STYLES)
                                     .apply(TemplateUtils.getParameters("amount", SpongeUtils.INSTANCE.getConfigController().maxColours()))
                                     .build());
    }

    // Verify the player is allowed to use the colours
    colours.removeIf(colour -> src.hasPermission(Reference.Permissions.COLOURS_PREFIX + colour.name().toLowerCase()));

    // Verify the player is allowed to use the styles
    styles.removeIf(style -> src.hasPermission(Reference.Permissions.STYLE_PREFIX + style.name().toLowerCase()));

    if (colours.size() + styles.size() > 0) {
      StringBuilder illegalStyles = new StringBuilder();
      for (Colour colour : colours) {
        illegalStyles.append(" &").append(colour.getColourChar());
      }
      for (Style style : styles) {
        illegalStyles.append(" &").append(style.getColourChar());
      }
      throw new CommandException(this.getMessageController()
                                     .getMessage(Reference.ErrorMessages.ILLEGAL_FORMAT)
                                     .apply(TemplateUtils.getParameters("style", illegalStyles.toString()))
                                     .build());
    }
  }

}
