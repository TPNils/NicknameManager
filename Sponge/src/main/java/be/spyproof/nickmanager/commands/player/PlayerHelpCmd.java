package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.Reference;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spyproof on 04/11/2016.
 */
public class PlayerHelpCmd implements CommandExecutor {

  private MessageController messageController;

  public PlayerHelpCmd(MessageController messageController) {
    this.messageController = messageController;
  }

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
    PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
    PaginationList.Builder builder = paginationService.builder();
    String devs = "";

    for (int i = 0; i < Reference.MetaData.AUTHORS.length; i++) {
      devs += Reference.MetaData.AUTHORS[i];
      if (i != Reference.MetaData.AUTHORS.length - 1) {
        devs += ", ";
      }
    }

    builder.title(Text.of(TextColors.DARK_GREEN, Reference.MetaData.PLUGIN_NAME)
                      .toBuilder()
                      .onHover(TextActions.showText(
                        Text.of(TextColors.GOLD, "Developers: ", TextColors.YELLOW, devs, Text.NEW_LINE,
                          TextColors.GOLD, "Version: ", TextColors.YELLOW, Reference.MetaData.VERSION)))
                      .build());

    List<Text> content = new ArrayList<>();

    content.add(this.messageController.getMessage(Reference.HelpMessages.NICK_CHECK).apply().build());
    content.add(this.messageController.getMessage(Reference.HelpMessages.NICK_FORMAT).apply().build());
    content.add(this.messageController.getMessage(Reference.HelpMessages.NICK_PREVIEW).apply().build());
    content.add(this.messageController.getMessage(Reference.HelpMessages.NICK_SET).apply().build());
    content.add(this.messageController.getMessage(Reference.HelpMessages.NICK_RESET).apply().build());
    content.add(this.messageController.getMessage(Reference.HelpMessages.NICK_REAL_NAME).apply().build());
    content.add(this.messageController.getMessage(Reference.HelpMessages.NICK_RULES).apply().build());
    content.add(this.messageController.getMessage(Reference.HelpMessages.NICK_UNLOCK).apply().build());

    builder.contents(content);
    builder.padding(Text.of(TextColors.AQUA, "="));
    builder.sendTo(src);
    return CommandResult.success();
  }

  public static CommandSpec getCommandSpec(MessageController messageController) {
    return CommandSpec.builder()
                      .executor(new PlayerHelpCmd(messageController))
                      .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                      .build();
  }

}
