package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.util.Reference;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

/**
 * Created by Spyproof on 01/11/2016.
 */
public class DisplayFormatsCmd implements CommandExecutor {

  private DisplayFormatsCmd() {

  }

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
    Text.Builder builder = Text.builder();

    builder.append(getColour(TextColors.BLACK));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.DARK_BLUE));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.DARK_GREEN));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.DARK_AQUA));
    builder.append(Text.NEW_LINE);
    builder.append(getColour(TextColors.DARK_RED));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.DARK_PURPLE));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.GOLD));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.GRAY));
    builder.append(Text.NEW_LINE);
    builder.append(getColour(TextColors.DARK_GRAY));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.BLUE));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.GREEN));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.AQUA));
    builder.append(Text.NEW_LINE);
    builder.append(getColour(TextColors.RED));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.LIGHT_PURPLE));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.YELLOW));
    builder.append(Text.of(" "));
    builder.append(getColour(TextColors.WHITE));

    builder.append(Text.NEW_LINE, Text.NEW_LINE);

    builder.append(getStyle(TextStyles.BOLD));
    builder.append(Text.of(" "));
    builder.append(getStyle(TextStyles.ITALIC));
    builder.append(Text.NEW_LINE);
    builder.append(getStyle(TextStyles.STRIKETHROUGH));
    builder.append(Text.of(" "));
    builder.append(getStyle(TextStyles.UNDERLINE));
    builder.append(Text.of(" "));
    builder.append(Text.of("&k").toBuilder().onHover(TextActions.showText(Text.of(TextStyles.OBFUSCATED, "Obfuscated"))).build());

    src.sendMessage(builder.build());

    return CommandResult.success();
  }

  private Text getColour(TextColor color) {
    return Text.of(color,
      TextSerializers.FORMATTING_CODE.serialize(Text.of(color)))
               .toBuilder()
               .onHover(TextActions.showText(Text.of(color, color.getName())))
               .build();
  }

  private Text getStyle(TextStyle.Base style) {
    return Text.of(style,
      TextSerializers.FORMATTING_CODE.serialize(Text.of(style)))
               .toBuilder()
               .onHover(TextActions.showText(Text.of(TextColors.YELLOW, style.getName())))
               .build();
  }

  public static CommandSpec getCommandSpec() {
    return CommandSpec.builder()
                      .executor(new DisplayFormatsCmd())
                      .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                      .build();
  }

}
