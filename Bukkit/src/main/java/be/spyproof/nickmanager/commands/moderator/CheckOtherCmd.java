package be.spyproof.nickmanager.commands.moderator;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TabCompleteUtil;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Spyproof on 15/11/2016.
 */
public class CheckOtherCmd extends AbstractCmd implements TabCompleter, IPermissionCheck {

  private static final String ARG = "player";

  public CheckOtherCmd(MessageController messageController, IBukkitNicknameController playerController, String... keys) {
    super(messageController, playerController, keys);
  }

  @Override
  public void sendHelpMsg(CommandSender src) {
    src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.ADMIN_NICK_CHECK));
  }

  @Override
  public void execute(CommandSender src, String cmd, String[] args) {
    checkPermission(src, Reference.Permissions.ADMIN_CHECK);

    if (args.length == 0 || args[0] == null) {
      throw new CommandException(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARG));
    }

    Optional<? extends NicknameData> player = this.playerController.getPlayer(args[0]);

    if (!player.isPresent()) {
      throw new CommandException(this.messageController.getFormattedMessage(Reference.ErrorMessages.WRONG_ARGUMENT).replace("{argument}", args[0]));
    }

    src.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.ADMIN_NICK_CHECK), player.get()).split("\\n"));
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    if (strings.length == 1 && strings[0] != null) {
      return TabCompleteUtil.getPlayers(strings[0]);
    } else {
      return new ArrayList<>();
    }
  }

}
