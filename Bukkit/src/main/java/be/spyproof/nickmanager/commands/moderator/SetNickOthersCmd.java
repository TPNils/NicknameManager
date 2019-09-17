package be.spyproof.nickmanager.commands.moderator;

import be.spyproof.nickmanager.commands.AbstractCmd;
import be.spyproof.nickmanager.commands.checks.IBlacklistChecker;
import be.spyproof.nickmanager.commands.checks.IFormatChecker;
import be.spyproof.nickmanager.commands.checks.ILengthChecker;
import be.spyproof.nickmanager.commands.checks.IPermissionCheck;
import be.spyproof.nickmanager.controller.IBukkitNicknameController;
import be.spyproof.nickmanager.controller.MessageController;
import be.spyproof.nickmanager.model.NicknameData;
import be.spyproof.nickmanager.util.BukkitUtils;
import be.spyproof.nickmanager.util.Reference;
import be.spyproof.nickmanager.util.TabCompleteUtil;
import be.spyproof.nickmanager.util.TemplateUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Spyproof on 15/11/2016.
 */
public class SetNickOthersCmd extends AbstractCmd implements TabCompleter, IBlacklistChecker, IFormatChecker, ILengthChecker, IPermissionCheck {

  private static final String[] ARGS = new String[]{"player", "nickname"};

  public SetNickOthersCmd(MessageController messageController, IBukkitNicknameController playerController, String... keys) {
    super(messageController, playerController, keys);
  }

  @Override
  public void sendHelpMsg(CommandSender src) {
    src.sendMessage(this.messageController.getFormattedMessage(Reference.HelpMessages.ADMIN_NICK_SET));
  }

  @Override
  public void execute(CommandSender src, String cmd, String[] args) {
    checkPermission(src, Reference.Permissions.ADMIN_SET);

    if (args.length == 0 || args[0] == null) {
      throw new CommandException(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARGS[0]));
    }

    if (args.length == 1) {
      throw new CommandException(this.messageController.getFormattedMessage(Reference.ErrorMessages.MISSING_ARGUMENT).replace("{argument}", ARGS[1]));
    }

    Optional<? extends NicknameData> player = this.playerController.getPlayer(args[0]);
    String nick = args[1];

    if (!player.isPresent()) {
      throw new CommandException(this.messageController.getFormattedMessage(Reference.ErrorMessages.WRONG_ARGUMENT).replace("{argument}", args[0]));
    }

    if (src instanceof Player && player.get().getUuid().equals(((Player) src).getUniqueId())) {
      throw new CommandException(this.messageController.getFormattedMessage(Reference.ErrorMessages.CANT_FORCE_CHANGE_OWN_NICK));
    }

    checkBlacklist(src, nick);
    checkFormat(src, nick);
    checkLength(nick);

    // Apply
    player.get().setNickname(nick);
    player.get().setLastChanged();
    this.playerController.savePlayer(player.get());

    src.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.ADMIN_NICK_SET), player.get()));
    Player receiver = Bukkit.getPlayer(player.get().getUuid());
    if (receiver != null) {
      BukkitUtils.INSTANCE.applyNickname(player.get(), receiver);
      receiver.sendMessage(TemplateUtils.apply(this.messageController.getFormattedMessage(Reference.SuccessMessages.NICK_SET), player.get()));
    }
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
