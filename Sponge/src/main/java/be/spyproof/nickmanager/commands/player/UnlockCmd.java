package be.spyproof.nickmanager.commands.player;

import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.util.Reference;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

/**
 * Created by Spyproof on 01/11/2016.
 */
public class UnlockCmd implements CommandExecutor {

  private MessageController messageController;

  private UnlockCmd(MessageController messageController) {
    this.messageController = messageController;
  }

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
    src.sendMessage(this.messageController.getMessage(Reference.SuccessMessages.NICK_UNLOCK));
    return CommandResult.success();
  }

  public static CommandSpec getCommandSpec(MessageController messageController) {
    return CommandSpec.builder()
                      .executor(new UnlockCmd(messageController))
                      .permission(Reference.Permissions.GENERIC_PLAYER_COMMANDS)
                      .build();
  }

}
